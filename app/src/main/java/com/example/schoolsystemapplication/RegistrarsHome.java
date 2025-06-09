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
            Intent intent = null;
            if (id == R.id.nav_teacher)
                intent = new Intent(this, teacher_list.class);
            else if (id == R.id.nav_view_student)
                intent = new Intent(this, StudentClass_list.class);
            else if (id == R.id.nav_add_student) {
                intent=new Intent(this,AddStudent.class);

            } else if (id == R.id.nav_subject)
                intent = new Intent(this, AddSubject.class);
            else if (id == R.id.nav_logout)
                intent = new Intent(this, LogIn.class);

            startActivity(intent);

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
