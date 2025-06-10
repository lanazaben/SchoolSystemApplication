package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSubject extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private Button select,add;
    private Spinner teacherSpinner,subjectSpinner;
    private EditText gradeLevel;

    private int selectedTeacherId = -1;
    private String selectedSubjectName = null;
    private static final int REQUEST_CODE_SCHEDULE = 1001;
    private String selectedScheduleJson = null;  // Store schedule JSON from ScheduleSelector
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_subject);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         teacherSpinner = findViewById(R.id.teacher_name_spinner);
         subjectSpinner = findViewById(R.id.subject_name_spinner);
         select=findViewById(R.id.button_select_time);
         add=findViewById(R.id.buttonAdd);
         gradeLevel=findViewById(R.id.editTextGrade);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fetchTeachers(teacherSpinner);
        fetchSubjects(subjectSpinner);

        select.setOnClickListener(v -> {
            String grade = gradeLevel.getText().toString().trim();

            if (grade.isEmpty()) {
                Toast.makeText(AddSubject.this, "Please enter a grade level", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedTeacherId == -1 || selectedSubjectName == null) {
                Toast.makeText(AddSubject.this, "Please select a teacher and a subject", Toast.LENGTH_SHORT).show();
                return;
            }
            if (grade.toLowerCase().startsWith("grade")) {
                grade = grade.replaceAll("[^0-9]", ""); // keep only digits
            }

            Intent intent = new Intent(AddSubject.this, ScheduleSelector.class);
            intent.putExtra("grade_level", grade);
            intent.putExtra("subject_name", selectedSubjectName);
            intent.putExtra("teacher_id", selectedTeacherId);
            startActivityForResult(intent, REQUEST_CODE_SCHEDULE);
        });

        add.setOnClickListener(v -> {
            String grade = gradeLevel.getText().toString().trim();

            if (grade.isEmpty()) {
                Toast.makeText(AddSubject.this, "Please enter a grade level", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedTeacherId == -1 || selectedSubjectName == null) {
                Toast.makeText(AddSubject.this, "Please select a teacher and a subject", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedScheduleJson == null) {
                Toast.makeText(AddSubject.this, "Please select schedule by pressing the 'Select' button.", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("Other".equals(selectedSubjectName)) {
                EditText subjectOther = findViewById(R.id.SubjectOther);
                selectedSubjectName = subjectOther.getText().toString().trim();
                if (selectedSubjectName.isEmpty()) {
                    Toast.makeText(this, "Enter custom subject name", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            sendSubjectToServer(selectedSubjectName, grade, selectedTeacherId, selectedScheduleJson);
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;
            if (id == R.id.nav_teacher)
                intent = new Intent(this, teacher_list.class);
            else if (id == R.id.nav_GradeLevel) {
                intent = new Intent(this, ClassList_Activity.class);
                intent.putExtra("user_type", "registrar");
                intent.putExtra("nav", "view_student");
            } else if (id == R.id.nav_subject)
                intent = new Intent(this, AddSubject.class);
            else if (id == R.id.nav_logout)
                intent = new Intent(this, LogIn.class);
            else if (id == R.id.nav_dark_mode) {
                Menu menu = navigationView.getMenu();
                MenuItem item_dark = menu.findItem(R.id.nav_dark_mode);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int currentNightMode = AppCompatDelegate.getDefaultNightMode();
                if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putString("mode", "night");
                    item_dark.setTitle("Dark Mode");
                    item_dark.setIcon(R.drawable.ic_dark_mode);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putString("mode", "dark");
                    item_dark.setTitle("Light Mode");
                    item_dark.setIcon(R.drawable.ic_light_mode);
                }
                editor.apply();
            }
            startActivity(intent);


            drawerLayout.closeDrawers();
            return true;
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCHEDULE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("schedule_json")) {
                selectedScheduleJson = data.getStringExtra("schedule_json");
                Toast.makeText(this, "Schedule selected successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void sendSubjectToServer(String subjectName, String gradeLevel, int teacherId, String scheduleJson) {
        String url = "http://10.0.2.2/php_project/add_subject_with_schedule.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Subject and schedule saved successfully!", Toast.LENGTH_SHORT).show(),
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 409) {
                        String errorMsg = new String(error.networkResponse.data);
                        Toast.makeText(this, "Conflict: " + errorMsg, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Network error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sp = getSharedPreferences("registrar_session", MODE_PRIVATE);
                int registrarId = sp.getInt("registrar_id", 1); // fallback to 1

                params.put("subject_name", subjectName);
                params.put("grade_level", gradeLevel);
                params.put("teacher_id", String.valueOf(teacherId));
                params.put("registrar_id", String.valueOf(registrarId));
                params.put("schedule", scheduleJson);

                return params;
            }
        };

        queue.add(request);
    }

    private void fetchTeachers(Spinner spinner) {
        String url = "http://10.0.2.2/php_project/get_AllTeacher.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    ArrayList<String> teacherNames = new ArrayList<>();
                    ArrayList<Integer> teacherIds = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            teacherNames.add(obj.getString("name"));
                            teacherIds.add(obj.getInt("teacher_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, teacherNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedTeacherId = teacherIds.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                },
                error -> Toast.makeText(this, "Failed to load teachers", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }

    private void fetchSubjects(Spinner spinner) {
        String url = "http://10.0.2.2/php_project/getSubjects.php"; // Replace if hosted externally
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    ArrayList<String> subjects = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            subjects.add(response.getJSONObject(i).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    subjects.add("Other"); // Add "Other" option at the end

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, subjects);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    EditText subjectOther = findViewById(R.id.SubjectOther);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedSubjectName = subjects.get(position);
                            if ("Other".equals(selectedSubjectName)) {
                                subjectOther.setEnabled(true);
                                subjectOther.setVisibility(View.VISIBLE);
                            } else {
                                subjectOther.setEnabled(false);
                                subjectOther.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                },
                error -> Toast.makeText(this, "Failed to load subjects", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

}
