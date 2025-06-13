package com.example.schoolsystemapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassList_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ProgressBar progressBar;
    private RecyclerView mainRecyclerView;
    private static String BASE_URL = "http://10.0.2.2/php_project/get_grade_level.php";
    private List<String> gradeLevels = new ArrayList<>();
    private Adapter_recyclerview_className adapter;
    private Context context = this;
    private String grade_level = null;
    private String nav;
    private String from = "teacher";
    private SharedPreferences sharedPreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);

        Intent intent = getIntent();
        nav = intent.getStringExtra("nav");
        if (nav.equals("view_student")) {
            from = getIntent().getStringExtra("from");
        }

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        progressBar = findViewById(R.id.progressBar);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        adapter = new Adapter_recyclerview_className(gradeLevels, this, nav);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(grade -> {
            Intent intent1;
            if (from.equals("registrar")) {
                // Registrar: go to ViewClassSchedule and pass selected grade level
                intent1 = new Intent(ClassList_Activity.this, ViewClassSchedule.class);
                intent1.putExtra("grade_level", grade_level);
                intent1.putExtra("from", "registrar");
                startActivity(intent1);
            }
        });

        if (from.equals("registrar")){
            // Load grade levels from PHP backend
            loadClassName();
        } else {
            BASE_URL = "http://10.0.2.2/php_project/get_grade_level_use_teacherID.php";
            // Load grade levels from PHP backend
            loadClassName_byID();
        }

        // Setup Toolbar and Navigation Drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupDrawer();
    }
    private void setupDrawer() {
        // Clear any existing menu (in case of reuse)
        navigationView.getMenu().clear();

        // Inflate appropriate menu
        if (from.equals("registrar")) {
            navigationView.inflateMenu(R.menu.drawer_menu);
        } else {
            navigationView.inflateMenu(R.menu.teacher);
        }

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (from.equals("registrar")) {
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
                    toggleDark();
                    return true;
                }
                if (intent != null){
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                    return true;
                }
            } else {
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
            }
            return true;
        });
    }

    private void loadClassName() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                response -> {
                    try {
                        gradeLevels.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String className = object.getString("grade_level");
                            gradeLevels.add(className);
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(context, "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadClassName_byID() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sp = context.getSharedPreferences("teacher_session", context.MODE_PRIVATE);
        int id_teacher = Integer.parseInt(sp.getString("teacher_id", "0"));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                response -> {
                    try {
                        gradeLevels.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String className = object.getString("grade_level");
                            gradeLevels.add(className);
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("teacher_id", String.valueOf(id_teacher));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void toggleDark() {
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}