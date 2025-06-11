package com.example.schoolsystemapplication;

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
import com.example.schoolsystemapplication.Data.Teacher;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class teacher_list extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private RecyclerView mainRecycler;
    private Adapter_teacherList adapter;
    private List<Teacher> teachers = new ArrayList<>();
    private SearchView searchView;
    private static final String BASE_URL = "http://10.0.2.2:80/php_project/get_AllTeacher.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mainRecycler = findViewById(R.id.teacherList);
        searchView = findViewById(R.id.searchView);

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
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);

        NavigationView navigationView = findViewById(R.id.drawer_nav);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;
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
        });

        adapter = new Adapter_teacherList(teachers, this);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        mainRecycler.setAdapter(adapter);
        loadTeacher();
        adapter.setOnItemClickListener(teacher -> {
            Intent intent = new Intent(teacher_list.this, teacherSchedule.class);
            intent.putExtra("teacher_id", teacher.getId()); // Send full teacher ID
            startActivity(intent);
        });

    }

    private void loadTeacher() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            teachers.clear();
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                int id = object.getInt("teacher_id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                List<SchoolSubject> subjects = null;
                                List<ScheduleEntry> schedule = null;
                                Teacher teacher = new Teacher(id, name, email, subjects, schedule);
                                teachers.add(teacher);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(teacher_list.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataSetChanged();
                        adapter.setTeachers(new ArrayList<>(teachers));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(teacher_list.this, error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(teacher_list.this).add(stringRequest);
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
