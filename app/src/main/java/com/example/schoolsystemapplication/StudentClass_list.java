package com.example.schoolsystemapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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
        adapter = new Adapter_studentList(students, this);
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
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_teacher) {
                // Handle Teacher item click
                Toast.makeText(StudentClass_list.this, "Teacher selected", Toast.LENGTH_SHORT).show();
                //Lana
                Intent intent = new Intent(StudentClass_list.this, teacher_list.class);
                startActivity(intent);
            } else if (id == R.id.nav_student) {
                // Handle Student item click
                Toast.makeText(StudentClass_list.this, "Student selected", Toast.LENGTH_SHORT).show();
                //Lana
                Intent intent = new Intent(StudentClass_list.this, StudentClass_list.class);
                startActivity(intent);
            } else if (id == R.id.nav_subject) {
                // Handle Subject item click
                Toast.makeText(StudentClass_list.this, "Subject selected", Toast.LENGTH_SHORT).show();
                //Lana
                Intent intent = new Intent(StudentClass_list.this, AddSubject.class);
                startActivity(intent);
            }

            // Close the drawer after an item is selected
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void loadStudent() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            students.clear();
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
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
                        }catch (Exception e){
                        }
                        adapter.notifyDataSetChanged();
                        adapter.setStudentss(new ArrayList<>(students));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentClass_list.this, error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(StudentClass_list.this).add(stringRequest);
    }

}



