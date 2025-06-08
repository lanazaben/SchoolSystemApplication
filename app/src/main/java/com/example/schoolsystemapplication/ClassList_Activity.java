package com.example.schoolsystemapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolsystemapplication.Data.ScheduleEntry;
import com.example.schoolsystemapplication.Data.Student;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassList_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private RecyclerView mainRecyclerView;
    private static  final String BASE_URL = "http://10.0.2.2:80/php_project/get_grade_level.php";
    private List<String> gradeLevels = new ArrayList<>();
    private Context context = this;
    private Adapter_recyclerview_className adapter;

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
        setContentView(R.layout.activity_class_list);

        Intent intent = getIntent();
        String nav = intent.getStringExtra("nav");

        mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
//        String[] ClassName = {"First Grade", "Second Grade", "Third Grade", "Fourth Grade", "Fifth Grade"};
        adapter = new Adapter_recyclerview_className(gradeLevels, this, nav);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setAdapter(adapter);
        loadClassName();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);

        // Setup NavigationView and its item listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent1 = new Intent(this, TeacherHome.class);
                startActivity(intent1);
            } else if (id == R.id.nav_dark_mode) {
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
            } else if (id == R.id.nav_logout) {
                Intent intent1 = new Intent(this, LogIn.class);
                startActivity(intent1);
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void loadClassName() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            gradeLevels.clear();
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String className = object.getString("grade_level");
                                gradeLevels.add(className);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
//                    Adapter_insertMark adapter = new Adapter_insertMark(students, context);
//                    mainRecyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(ClassList_Activity.this).add(stringRequest);
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