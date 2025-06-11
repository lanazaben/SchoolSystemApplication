package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolsystemapplication.Data.ScheduleEntry;
import com.example.schoolsystemapplication.Data.SchoolSubject;
import com.example.schoolsystemapplication.Data.Student;
import com.example.schoolsystemapplication.Data.Teacher;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

public class StudentClass_list extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private RecyclerView mainRecycler;
    private static final String BASE_URL = "http://10.0.2.2:80/php_project/get_student_use_gradelLevel.php";
    private Adapter_studentList adapter;
    private List<Student> students = new ArrayList<>();
    private SearchView searchView;
    NavigationView navigationView;
    private int gradeLevel;
    private int teacherId = -1;
    private boolean isRegistrarView = false;
    private static final String[] TIME_KEYS = {
            "08:00 - 09:00 AM",
            "09:00 - 10:00 AM",
            "10:15 - 11:15 AM",
            "11:30 - 12:30 PM",
            "12:30 - 01:30 PM",
            "01:30 - 02:30 PM",
            "02:30 - 03:30 PM"
    };

    private static final String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};

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

        String from = getIntent().getStringExtra("from");
        searchView = findViewById(R.id.searchView);
        mainRecycler=(RecyclerView) findViewById(R.id.parentRecyclerView);
        adapter = new Adapter_studentList(students, this,from);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        mainRecycler.setAdapter(adapter);
        gradeLevel = getIntent().getIntExtra("grade_level", -1);
        if (gradeLevel == -1) {
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

        isRegistrarView = "registrar".equals(from);

        if (!isRegistrarView) {
            SharedPreferences sp = getSharedPreferences("teacher_session", MODE_PRIVATE);
            teacherId = sp.getInt("teacher_id", -1);

            if (teacherId == -1 && getIntent() != null) {
                teacherId = getIntent().getIntExtra("teacher_id", -1);
            }

            if (teacherId == -1) {
                Toast.makeText(this, "No teacher ID provided.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        setupDrawer();}

    private void setupDrawer() {
        // Clear any existing menu (in case of reuse)
        navigationView.getMenu().clear();

        // Inflate appropriate menu
        if (isRegistrarView) {
            navigationView.inflateMenu(R.menu.drawer_menu);
        } else {
            navigationView.inflateMenu(R.menu.teacher);
        }

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (isRegistrarView) {
                if (id == R.id.nav_teacher)
                    intent = new Intent(this, teacher_list.class);
                else if (id == R.id.nav_GradeLevel) {
                    intent = new Intent(this, ClassList_Activity.class);
                    intent.putExtra("from", "registrar");
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
            } else {
                if (id == R.id.nav_home)
                    intent = new Intent(this, TeacherHome.class);
                else if (id == R.id.nav_schedule)
                    intent = new Intent(this, teacherSchedule.class);
                else if (id == R.id.nav_assignments)
                    intent = new Intent(this, ClassList_Activity.class).putExtra("nav", "assignments");
                else if (id == R.id.nav_marks)
                    intent = new Intent(this, ClassList_Activity.class).putExtra("nav", "marks");
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
            }

            if (intent != null) {
                startActivity(intent);
                drawerLayout.closeDrawers();
            }

            return true;
        });
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
                                List<ScheduleEntry> schedule = null;
                                Student student = new Student(id, name, email, gradeLevel, parentNum, BirthCertificate, schedule);
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



