package com.example.schoolsystemapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;



    public class StudentHome extends AppCompatActivity {
        private DrawerLayout drawerLayout;
        private ActionBarDrawerToggle toggle;
        private Toolbar toolbar;

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

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_scheduleStud) {
                    Intent schedule=new Intent (StudentHome.this,StudentSchedule.class);
                    startActivity(schedule);

                } else if (id == R.id.nav_assignment) {
                    Intent allAssignemnt=new Intent (StudentHome.this,allAssignmentPage.class);
                    startActivity(allAssignemnt);

                } else if (id == R.id.nav_mark) {
                    //opens subjects list of this student, to show marks for this subject alone
                    Intent subjectBeforeMark=new Intent (StudentHome.this, SubjectsList.class);
                    startActivity(subjectBeforeMark);

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



