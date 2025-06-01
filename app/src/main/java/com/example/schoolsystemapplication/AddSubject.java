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

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

public class AddSubject extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_subject);
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

        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Setup NavigationView and its item listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_teacher) {
                // Handle Teacher item click
                Toast.makeText(AddSubject.this, "Teacher selected", Toast.LENGTH_SHORT).show();
                //Lana
                Intent intent = new Intent(AddSubject.this, teacher_list.class);
                startActivity(intent);
            } else if (id == R.id.nav_student) {
                // Handle Student item click
                Toast.makeText(AddSubject.this, "Student selected", Toast.LENGTH_SHORT).show();
                //Lana
                Intent intent = new Intent(AddSubject.this, StudentClass_list.class);
                startActivity(intent);
            } else if (id == R.id.nav_subject) {
                // Handle Subject item click
                Toast.makeText(AddSubject.this, "Subject selected", Toast.LENGTH_SHORT).show();
                //Lana
                Intent intent = new Intent(AddSubject.this, AddSubject.class);
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