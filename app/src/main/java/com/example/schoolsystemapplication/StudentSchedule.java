package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

public class StudentSchedule extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    // Modify this with the real teacher's ID (maybe from SharedPreferences or Intent)
    private final int teacherId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_schedule);

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

        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            if (id == R.id.nav_home) {
                intent = new Intent(this, TeacherHome.class);
            } else if (id == R.id.nav_dark_mode) {
                Menu menu = navigationView.getMenu();
                MenuItem itemDark = menu.findItem(R.id.nav_dark_mode);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int currentNightMode = AppCompatDelegate.getDefaultNightMode();
                if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putString("mode", "night");
                    itemDark.setTitle("Dark Mode");
                    itemDark.setIcon(R.drawable.ic_dark_mode);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putString("mode", "dark");
                    itemDark.setTitle("Light Mode");
                    itemDark.setIcon(R.drawable.ic_light_mode);
                }
                editor.apply();
                return true;
            } else if (id == R.id.nav_schedule) {
                intent = new Intent(this, teacherSchedule.class);
            } else if (id == R.id.nav_assignments) {
                intent = new Intent(this, ClassList_Activity.class);
                intent.putExtra("nav", "assignments");
            } else if (id == R.id.nav_marks) {
                intent = new Intent(this, ClassList_Activity.class);
                intent.putExtra("nav", "marks");
            } else if (id == R.id.nav_logout) {
                intent = new Intent(this, LogIn.class);
            } else {
                return false;
            }
            startActivity(intent);
            drawerLayout.closeDrawers();
            return true;
        });

        // Load the teacher's schedule
        fetchTeacherSchedule(teacherId);
    }

    private void fetchTeacherSchedule(int teacherId) {
        String url = "http://10.0.2.2/phpFiles/teacherSched.php?teacher_id=" + teacherId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> populateScheduleTable(response),
                error -> Toast.makeText(this, "Failed to load schedule", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }

    private void populateScheduleTable(JSONObject scheduleData) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
        String[] timeSlots = {"8:00", "8:50", "9:35", "10:25", "11:35", "12:20", "1:05"};
        String[] timeKeys = {"08:00 - 09:00 AM", "09:00 - 10:00 AM", "10:15 - 11:15 AM", "11:30 - 12:30 PM",
                "12:30 - 01:30 PM", "01:30 - 02:30 PM", "02:30 - 03:30 PM"};

        TableLayout table = findViewById(R.id.tableLayout);

        for (int i = 1; i <= days.length; i++) {
            TableRow row = (TableRow) table.getChildAt(i); // Skip header
            String day = days[i - 1];

            for (int j = 1; j <= timeSlots.length; j++) {
                TextView cell = (TextView) row.getChildAt(j);
                String timeKey = timeKeys[j - 1];
                try {
                    String subject = scheduleData.has(day) && scheduleData.getJSONObject(day).has(timeKey)
                            ? scheduleData.getJSONObject(day).getString(timeKey)
                            : "";
                    cell.setText(subject);
                } catch (Exception e) {
                    cell.setText("");
                }
            }
        }
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
