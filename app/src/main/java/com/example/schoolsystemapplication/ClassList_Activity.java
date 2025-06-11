package com.example.schoolsystemapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassList_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    NavigationView navigationView;

    private RecyclerView mainRecyclerView;
    private static final String BASE_URL = "http://10.0.2.2/php_project/get_grade_level.php";
    private List<String> gradeLevels = new ArrayList<>();
    private Adapter_recyclerview_className adapter;
    private Context context = this;
    private String grade_level = null;
    private boolean isRegistrarView = false;

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

        String from = getIntent().getStringExtra("from");
        isRegistrarView = "registrar".equals(from);
        if (!isRegistrarView) {
            SharedPreferences sp = getSharedPreferences("teacher_session", MODE_PRIVATE);
            grade_level = sp.getString("grade_level", null);

            if (grade_level == null && getIntent() != null) {
                grade_level = getIntent().getStringExtra("grade_level");
            }

            if (grade_level == null) {
                Toast.makeText(this, "No grade level provided for teacher view.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }


        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);


        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        adapter = new Adapter_recyclerview_className(gradeLevels, this, from);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(grade -> {
            Intent intent;
            if (isRegistrarView) {
                // Registrar: go to ViewClassSchedule and pass selected grade level
                intent = new Intent(ClassList_Activity.this, ViewClassSchedule.class);
                intent.putExtra("grade_level", grade_level);
                intent.putExtra("from", "registrar");
                startActivity(intent);

            }
        });


        // Load grade levels from PHP backend
        loadClassName();


        // Setup Toolbar and Navigation Drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupDrawer();


    }
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

            if (!isRegistrarView) {
                if (id == R.id.nav_teacher)
                    intent = new Intent(this, teacher_list.class);
                else if (id == R.id.nav_GradeLevel) {
                    intent = new Intent(this, ClassList_Activity.class);
                    intent.putExtra("from", "registrar");
                    intent.putExtra("nav", "view_student");
                } else if (id == R.id.nav_subject){
                    intent = new Intent(this, AddSubject.class);
               }
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
            startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void loadClassName() {
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
                    } catch (Exception e) {
                        Toast.makeText(context, "Parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(context, "Volley error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(stringRequest);
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
