package com.example.schoolsystemapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

public class ClassList_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

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

        RecyclerView mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        String[] ClassName = {"First Grade", "Second Grade", "Third Grade", "Fourth Grade", "Fifth Grade"};
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter_recyclerview_className adapter = new Adapter_recyclerview_className(ClassName, this);
        mainRecyclerView.setAdapter(adapter);

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
            if (id == R.id.nav_home) {
                Intent intent1 = new Intent(this, TeacherHome.class);
                startActivity(intent1);
            } else if (id == R.id.nav_dark_mode) {
                //
            } else if (id == R.id.nav_schedule) {
                Intent intent1 = new Intent(this, teacherSchedule.class);
                startActivity(intent1);
            } else if (id == R.id.nav_assignments) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                startActivity(intent1);
            } else if (id == R.id.nav_marks) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                startActivity(intent1);
            } else if (id == R.id.nav_logout) {
                Intent intent1 = new Intent(this, LogIn.class);
                startActivity(intent1);
            }
            drawerLayout.closeDrawers();
            return true;
        });
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