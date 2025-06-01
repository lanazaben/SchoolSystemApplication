package com.example.schoolsystemapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;

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

import com.example.schoolsystemapplication.Data.Teacher;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class teacher_list extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private RecyclerView mainRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_list);

        mainRecycler = findViewById(R.id.teacherList);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_teacher) {
                startActivity(new Intent(this, teacher_list.class));
            } else if (id == R.id.nav_student) {
                startActivity(new Intent(this, StudentClass_list.class));
            } else if (id == R.id.nav_subject) {
                startActivity(new Intent(this, AddSubject.class));
            }
            drawerLayout.closeDrawers();
            return true;
        });

        String[] teacherNames = {"Yara Hamad", "Doaa Assi", "Lana Zaben","Hiba Awwad"}; // Example names
        Adapter_teacherList adapter = new Adapter_teacherList(teacherNames, this, new Adapter_teacherList.OnItemClickListener() {
            @Override
            public void onItemClick(String teacherName) {
                Toast.makeText(teacher_list.this, "Clicked: " + teacherName, Toast.LENGTH_SHORT).show();
Intent schedule=new Intent(teacher_list.this,teacherSchedule.class);
            startActivity(schedule);}
        });

        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        mainRecycler.setAdapter(adapter);
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
