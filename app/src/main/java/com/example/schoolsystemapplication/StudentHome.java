package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Map;


public class StudentHome extends AppCompatActivity {
        private DrawerLayout drawerLayout;
        private ActionBarDrawerToggle toggle;
        private Toolbar toolbar;
        NavigationView navigationView;
    int studentId = -1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_student_home);

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawerLayout = findViewById(R.id.drawer_layout);

            // ActionBarDrawerToggle allows us to link the Toolbar with the Drawer
            toggle = new ActionBarDrawerToggle(this, drawerLayout,
                    toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            // Add the toggle to the drawer
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            SharedPreferences spStudent = getSharedPreferences("student_session", MODE_PRIVATE);

// DEBUG: check all stored keys and values
            Map<String, ?> allPrefs = spStudent.getAll();
            for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
                Log.d("PrefsCheck", entry.getKey() + ": " + entry.getValue().toString());
            }

            try {
                String studentIdStr = spStudent.getString("student_id", "-1");
                studentId = Integer.parseInt(studentIdStr);
            } catch (Exception e) {
                Log.e("SessionDebug", "Failed to parse student_id from SharedPreferences", e);
            }

            Log.d("SessionDebug", "student_id from SharedPreferences: " + studentId);


             navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_scheduleStud) {
                    Intent schedule=new Intent (StudentHome.this,StudentSchedule.class);
                    //schedule.putExtra("from","student");
                    schedule.putExtra("student_id",studentId);

                    startActivity(schedule);

                } else if (id == R.id.nav_assignment) {
                    Intent allAssignemnt=new Intent (StudentHome.this,allAssignmentPage.class);
                    startActivity(allAssignemnt);

                } else if (id == R.id.nav_mark) {
                    //opens subjects list of this student, to show marks for this subject alone
                    Intent subjectBeforeMark=new Intent (StudentHome.this, SubjectsList.class);
                    startActivity(subjectBeforeMark);

                }
                else if (id == R.id.nav_dark_mode) {
                    toggleDark();
                    return true;
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
    }



