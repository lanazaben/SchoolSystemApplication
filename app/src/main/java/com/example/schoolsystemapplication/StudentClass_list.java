package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.schoolsystemapplication.Data.Teacher;
//import com.example.schoolsystemapplication.Data.SchoolSubject;
//import com.example.schoolsystemapplication.Data.Teacher;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolsystemapplication.Data.ScheduleEntry;
import com.example.schoolsystemapplication.Data.SchoolSubject;
import com.example.schoolsystemapplication.Data.Student;
import com.example.schoolsystemapplication.Data.Teacher;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

public class StudentClass_list extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private RecyclerView mainRecycler;
    private static final String BASE_URL = "http://10.0.2.2:80/php_project/get_AllStudent.php";
    private Adapter_studentList adapter;
    private List<Student> students = new ArrayList<>();
    private SearchView searchView;
    private NavigationView navigationView;
    private String gradeLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_class_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        searchView = findViewById(R.id.searchView);
        mainRecycler=(RecyclerView) findViewById(R.id.parentRecyclerView);
        adapter = new Adapter_studentList(students, this, "registrar");
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        mainRecycler.setAdapter(adapter);
        gradeLevel = getIntent().getStringExtra("grade_level");
        if (gradeLevel == null) {
            Toast.makeText(this, "Missing grade level!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        loadStudent();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.filter(newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Setup NavigationView and its item listener
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;
            if (id == R.id.nav_teacher)
                intent = new Intent(this, teacher_list.class);
            else if (id == R.id.nav_GradeLevel) {
                intent = new Intent(this, ClassList_Activity.class);
                intent.putExtra("from", "registrar");
                intent.putExtra("nav", "view_student");
                startActivity(intent);
            } else if (id == R.id.nav_subject) {
                intent = new Intent(this, AddSubject.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout){
                intent = new Intent(this, LogIn.class);
                startActivity(intent);
            }
            else if (id == R.id.nav_dark_mode) {
                toggleDark();
                return true;
            }
            // Close the drawer after an item is selected
            drawerLayout.closeDrawers();
            return true;
        });
    }
    private void toggleDark() {
        MenuItem dark = navigationView.getMenu().findItem(R.id.nav_dark_mode);
        SharedPreferences sp = getSharedPreferences("Mode", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        int mode = AppCompatDelegate.getDefaultNightMode();
        if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            dark.setTitle("Dark Mode");
            dark.setIcon(R.drawable.ic_dark_mode);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            dark.setTitle("Light Mode");
            dark.setIcon(R.drawable.ic_light_mode);
        }
        ed.apply();
    }
    private void loadStudent() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            students.clear();
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt("student_id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                int gradeLevel = object.getInt("grade_level");
                                int parentNum = object.getInt("parent_phone");
                                String BirthCertificate = "";
                                List<ScheduleEntry> schedule = null; //score
                                double score = object.getDouble("score");
                                Student student = new Student(id, name, email, gradeLevel, parentNum, BirthCertificate, schedule, score);
                                students.add(student);
                            }
                        } catch (Exception e) {
                            Toast.makeText(StudentClass_list.this, "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                        adapter.setStudentss(new ArrayList<>(students));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentClass_list.this, "Volley error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("grade_level", String.valueOf(gradeLevel));
                return params;
            }
        };

        Volley.newRequestQueue(StudentClass_list.this).add(stringRequest);
    }


}



