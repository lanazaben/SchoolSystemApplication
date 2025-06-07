package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AddExamActivity extends AppCompatActivity {

    EditText editText_typeExam;
    EditText editText_fullMark;

    String exam_Type;
    int full_Mark;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_exam);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.activity_add_exam);
        editText_typeExam = findViewById(R.id.editTextTypeExam);
        editText_fullMark = findViewById(R.id.editTextFullMark);

        Intent intent = getIntent();
        exam_Type = intent.getStringExtra("examType");
        full_Mark = intent.getIntExtra("fullMark", 0);
        if (exam_Type != null) {
            editText_typeExam.setText(exam_Type);
            editText_fullMark.setText(full_Mark);
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);

        // Setup NavigationView and its item listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent1 = new Intent(this, TeacherHome.class);
                startActivity(intent1);
            } else if (id == R.id.nav_dark_mode) {
                Menu menu = navigationView.getMenu();
                MenuItem item_dark = menu.findItem(R.id.nav_dark_mode);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int currentNightMode = AppCompatDelegate.getDefaultNightMode();
                if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putString("mode", "night");
                    item_dark.setTitle("Dark Mode");
                    item_dark.setIcon(R.drawable.ic_dark_mode);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putString("mode", "dark");
                    item_dark.setTitle("Light Mode");
                    item_dark.setIcon(R.drawable.ic_light_mode);
                }
                editor.apply();
            } else if (id == R.id.nav_schedule) {
                Intent intent1 = new Intent(this, teacherSchedule.class);
                startActivity(intent1);
            } else if (id == R.id.nav_assignments) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                intent1.putExtra("nav", "assignments");
                startActivity(intent1);
            } else if (id == R.id.nav_marks) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                intent1.putExtra("nav", "marks");
                startActivity(intent1);
            } else if (id == R.id.nav_logout) {
                Intent intent1 = new Intent(this, LogIn.class);
                startActivity(intent1);
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    public void btn_OnClick_back(View view) {
        Intent intent = new Intent(this, markOrStudent_activity.class);
        startActivity(intent);
    }

    public void btn_OnClick_next(View view) {
        String exam_type = editText_typeExam.getText().toString();
        String maxMark = editText_fullMark.getText().toString();
        if (!exam_type.equals("") && !maxMark.equals("")) {
            Intent intent = new Intent(this, activity_insertMark.class);
            intent.putExtra("examType", exam_type);
            intent.putExtra("fullMark", Integer.parseInt(maxMark));
            startActivity(intent);
        }else {
            Toast.makeText(this, "Error: Missing or invalid input. Please check all fields.", Toast.LENGTH_SHORT).show();
        }
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