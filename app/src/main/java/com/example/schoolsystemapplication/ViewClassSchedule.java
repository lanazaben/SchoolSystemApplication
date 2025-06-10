package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ViewClassSchedule extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    NavigationView navigationView;
    Button btnView,btnAdd,btnEdit;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class_schedule);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        // ActionBarDrawerToggle allows us to link the Toolbar with the Drawer
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        btnView=findViewById(R.id.buttonViewStu);
        btnAdd=findViewById(R.id.buttonAddStudent);
        btnEdit=findViewById(R.id.buttonEditSub);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(ViewClassSchedule.this,StudentClass_list.class);
                intent.putExtra("user_type", "registrar");
                intent.putExtra("nav", "view_student");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(ViewClassSchedule.this, ScheduleSelector.class);
                intent.putExtra("subjectName","null");

            }
        });

        // Setup NavigationView and its item listener
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_teacher) {
                intent = new Intent(this, teacher_list.class);
                intent.putExtra("user_type", "registrar");
            }  else if (id == R.id.nav_GradeLevel) {
                intent = new Intent(this, ClassList_Activity.class);
                intent.putExtra("user_type", "registrar");
                intent.putExtra("nav", "view_student");
            }else if (id == R.id.nav_subject) {
                intent = new Intent(this, AddSubject.class);
                intent.putExtra("user_type", "registrar");
            } else if (id == R.id.nav_logout) {
                intent = new Intent(this, LogIn.class);
            } else if (id == R.id.nav_dark_mode) {
                toggleDark();
                return true;
            }

            if (intent != null)
                startActivity(intent);

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
}
