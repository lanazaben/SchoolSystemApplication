package com.example.schoolsystemapplication;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class TeacherHome extends AppCompatActivity {
        private DrawerLayout drawerLayout;
        private ActionBarDrawerToggle toggle;
        private Toolbar toolbar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_teacher_home);

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
                if (id == R.id.nav_Schadual) {

                } else if (id == R.id.nav_Assignment) {

                } else if (id == R.id.nav_Marks) {

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




