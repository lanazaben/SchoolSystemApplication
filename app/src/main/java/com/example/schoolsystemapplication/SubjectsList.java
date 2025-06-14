package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SubjectsList extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private OptionsAdapter_recyclerview adapter;
    private RecyclerView mainRecycler;
    private List<SchoolSubject> subjects = new ArrayList<>();
    private ProgressBar progressBar;
    private SearchView searchView;
    private NavigationView navigationView;

    private static final String BASE_URL = "http://10.0.2.2:80/php_project/get_subjects_use_gradeLevel.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subjects_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.searchView);

        mainRecycler=(RecyclerView) findViewById(R.id.subject_markList);
        adapter = new OptionsAdapter_recyclerview(subjects, this, "studentWantsSubjects", "");
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        mainRecycler.setAdapter(adapter);
        loadStudent();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        // ActionBarDrawerToggle allows us to link the Toolbar with the Drawer
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_scheduleStud) {
                Intent schedule=new Intent (SubjectsList.this,StudentSchedule.class);
                startActivity(schedule);

            } else if (id == R.id.nav_assignment) {
                Intent allAssignemnt=new Intent (SubjectsList.this,allAssignmentPage.class);
                startActivity(allAssignemnt);

            } else if (id == R.id.nav_mark) {
                //opens subjects list of this student, to show marks for this subject alone
                Intent subjectBeforeMark=new Intent (SubjectsList.this, SubjectsList.class);
                startActivity(subjectBeforeMark);

            } else if (id == R.id.nav_dark_mode) {
                toggleDark();
            }
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

    @Override
    public void onBackPressed() {
        // If the drawer is open, close it; otherwise, exit the activity
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadStudent() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sp = getSharedPreferences("student_session", MODE_PRIVATE);
        int student_id = Integer.parseInt(sp.getString("student_id", "0"));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            subjects.clear();
                            Log.e("resp", response);
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt("subject_id");
                                String name = object.getString("name");
                                int gradeLevel = object.getInt("grade_level");
                                Teacher teacher = null;
                                SchoolSubject subject = new SchoolSubject(id, name, gradeLevel, teacher);
                                subjects.add(subject);
                            }
                        } catch (Exception e) {
                            Toast.makeText(SubjectsList.this, "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                        adapter.setStudents(new ArrayList<>(subjects));
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SubjectsList.this, "Volley error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("student_id", String.valueOf(student_id));
                return params;
            }
        };

        Volley.newRequestQueue(SubjectsList.this).add(stringRequest);
    }
}