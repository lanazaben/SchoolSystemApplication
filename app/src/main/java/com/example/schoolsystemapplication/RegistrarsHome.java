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

public class RegistrarsHome extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrars_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        // ActionBarDrawerToggle allows us to link the Toolbar with the Drawer
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
                Toast.makeText(RegistrarsHome.this, "Teacher selected", Toast.LENGTH_SHORT).show();
            //Lana
                Intent intent = new Intent(RegistrarsHome.this, teacher_list.class);
                startActivity(intent);
            } else if (id == R.id.nav_student) {
                // Handle Student item click
                Toast.makeText(RegistrarsHome.this, "Student selected", Toast.LENGTH_SHORT).show();
           //Lana
                Intent intent = new Intent(RegistrarsHome.this, StudentClass_list.class);
                startActivity(intent);
            } else if (id == R.id.nav_subject) {
                // Handle Subject item click
                Toast.makeText(RegistrarsHome.this, "Subject selected", Toast.LENGTH_SHORT).show();
            //Lana
                Intent intent = new Intent(RegistrarsHome.this, AddSubject.class);
                startActivity(intent);
            }

            // Close the drawer after an item is selected
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
