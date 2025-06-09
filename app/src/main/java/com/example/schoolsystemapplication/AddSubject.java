package com.example.schoolsystemapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddSubject extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    private int selectedTeacherId = -1;
    private String selectedSubjectName = null;

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

        Spinner teacherSpinner = findViewById(R.id.teacher_name_spinner);
        Spinner subjectSpinner = findViewById(R.id.subject_name_spinner);

        fetchTeachers(teacherSpinner);
        fetchSubjects(subjectSpinner);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;
            if (id == R.id.nav_teacher)
                intent = new Intent(this, teacher_list.class);
            else if (id == R.id.nav_view_student)
                intent = new Intent(this, StudentClass_list.class);
            else if (id == R.id.nav_add_student) {
                intent=new Intent(this,AddStudent.class);

            } else if (id == R.id.nav_subject)
                intent = new Intent(this, AddSubject.class);
            else if (id == R.id.nav_logout)
                intent = new Intent(this, LogIn.class);
            startActivity(intent);


            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void fetchTeachers(Spinner spinner) {
        String url = "http://10.0.2.2/php_project/AllTeacher.php";
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
        String url = "http://10.0.2.2/php_project/get_subjects.php"; // Replace if hosted externally
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, subjects);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedSubjectName = subjects.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                },
                error -> Toast.makeText(this, "Failed to load subjects", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
}
