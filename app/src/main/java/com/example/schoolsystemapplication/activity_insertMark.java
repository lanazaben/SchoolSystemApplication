package com.example.schoolsystemapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolsystemapplication.Data.ScheduleEntry;
import com.example.schoolsystemapplication.Data.Student;
import com.example.schoolsystemapplication.Data.Student_Mark;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class activity_insertMark extends AppCompatActivity {

    private RecyclerView mainRecyclerView;
    private TextView typeExam;
    private TextView fullMark;
    private String exam_Type;
    private double full_Mark;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private static final String BASE_URL = "http://10.0.2.2:80/php_project/get_student_use_gradelLevel.php";
    private List<Student> students = new ArrayList<>();
    private Context context = this;
    private Adapter_insertMark adapter;
    private String classNum;
    private ProgressBar progressBar;
    private int subjectNum;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_mark);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        typeExam = findViewById(R.id.textView_typeExam);
        fullMark = findViewById(R.id.textView_fullMark);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        exam_Type = intent.getStringExtra("examType");
        full_Mark = intent.getDoubleExtra("fullMark", 0);
        classNum = intent.getStringExtra("classNum");

        subjectNum = intent.getIntExtra("subject", 0);

        typeExam.setText(exam_Type + ": ");
        fullMark.setText("_ /" + full_Mark);

        mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_insertMark(students, this, "mark", full_Mark);
        mainRecyclerView.setAdapter(adapter);
        loadStudents();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup NavigationView and its item listener
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent1 = new Intent(this, TeacherHome.class);
                startActivity(intent1);
            } else if (id == R.id.nav_dark_mode) {
                toggleDark();
                return true;
            } else if (id == R.id.nav_schedule) {
                Intent intent1 = new Intent(this, teacherSchedule.class);
                startActivity(intent1);
            } else if (id == R.id.nav_assignments) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                intent1.putExtra("nav", "assignments");
                startActivity(intent1);
            } else if (id == R.id.nav_marks) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                intent1.putExtra("nav", "marks");
                startActivity(intent1);
            } else if (id == R.id.nav_addMarks) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                intent1.putExtra("nav", "addmarks");
                startActivity(intent1);
            } else if (id == R.id.nav_logout) {
                Intent intent1 = new Intent(this, LogIn.class);
                startActivity(intent1);
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void loadStudents() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    students.clear();
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        int id_student = object.getInt("student_id");
                        String name = object.getString("name");
                        String email = object.getString("email");
                        int gradeLevel = object.getInt("grade_level");
                        int parentNum = object.getInt("parent_phone");
                        String BirthCertificate = "";//object.getString("birth_certificate");
                        List<ScheduleEntry> schedule = null;
                        double score = object.getDouble("score");
                        Student student = new Student(id_student, name, email, gradeLevel, parentNum, BirthCertificate, schedule, score);
                        students.add(student);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grade_level", classNum);
                params.put("subject_id", subjectNum+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
//    Volley.newRequestQueue(activity_insertMark.this).add(stringRequest);
    }

    public void btn_OnClick_back(View view) {
        Intent intent = new Intent(this, AddExamActivity.class);
        intent.putExtra("examType", exam_Type);
        intent.putExtra("fullMark", full_Mark);
        intent.putExtra("classNum", classNum);
        intent.putExtra("subject", subjectNum);
        startActivity(intent);
    }

    public void btn_OnClick_Save(View view){
        List<Student_Mark> marksToSend = adapter.getStudentMarks();
        if (!marksToSend.isEmpty()){
            JSONArray marksArray = new JSONArray();
            for (Student_Mark sm : marksToSend) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("student_id", sm.getStudentId());
                    obj.put("typeMark", typeExam);
                    obj.put("mark", sm.getMark());
                    obj.put("subject_id", subjectNum);
                    obj.put("maxMark", full_Mark);
                    marksArray.put(obj);
                } catch (JSONException e) {
                }
            }
            JSONObject finalData = new JSONObject();
            try {
                finalData.put("marks", marksArray);
                saveData(finalData);
            } catch (JSONException e) {
            }
        }
    }

    public void saveData(JSONObject finalData){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:80/php_project/insert_new_Mark.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, finalData,
                response -> {
                    Toast.makeText(context, "The tags have been saved successfully" , Toast.LENGTH_LONG).show();
                    Intent intent5 = new Intent(activity_insertMark.this, StudentList_insertMark.class);
                    intent5.putExtra("classNum", classNum);
                    intent5.putExtra("subject", subjectNum);
                    startActivity(intent5);
                },
                error -> {
                    error.printStackTrace();
                }) {
        };
        queue.add(request);
    }

    private void toggleDark() {
        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);
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

    @Override
    public void onBackPressed() {
        // If the drawer is open, close it; otherwise, exit the activity
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}