package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StudentSchedule extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView nav;

    private int teacherId = -1;
    private boolean isRegistrarView = false;

    private static final String[] TIME_KEYS = {
            "08:00 - 09:00 AM",
            "09:00 - 10:00 AM",
            "10:15 - 11:15 AM",
            "11:30 - 12:30 PM",
            "12:30 - 01:30 PM",
            "01:30 - 02:30 PM",
            "02:30 - 03:30 PM"
    };

    private static final String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};

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

        String from = getIntent().getStringExtra("from");
        isRegistrarView = "registrar".equals(from);

        SharedPreferences sp = getSharedPreferences("teacher_session", MODE_PRIVATE);
        teacherId = sp.getInt("teacher_id", -1);

        if (teacherId == -1 && getIntent() != null) {
            teacherId = getIntent().getIntExtra("teacher_id", -1);
        }

        if (teacherId == -1) {
            Toast.makeText(this, "No teacher ID provided.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        nav = findViewById(R.id.navigation_view);

        setupDrawer();

        fetchTeacherSchedule();
    }

    private void setupDrawer() {
        // Clear any existing menu (in case of reuse)
        nav.getMenu().clear();

        // Inflate appropriate menu
        if (isRegistrarView) {
            nav.inflateMenu(R.menu.teacher);
        } else {
            nav.inflateMenu(R.menu.drawer_menu);
        }

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);

        nav.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (!isRegistrarView) {
                if (id == R.id.nav_teacher)
                    intent = new Intent(this, teacher_list.class);
                else if (id == R.id.nav_GradeLevel) {
                    intent = new Intent(this, ClassList_Activity.class);
                    intent.putExtra("user_type", "registrar");
                    intent.putExtra("nav", "view_student");
                } else if (id == R.id.nav_subject)
                    intent = new Intent(this, AddSubject.class);
                else if (id == R.id.nav_logout)
                    intent = new Intent(this, LogIn.class);
                else if (id == R.id.nav_dark_mode) {
                    Menu menu = nav.getMenu();
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
                }
            } else {
                if (id == R.id.nav_home)
                    intent = new Intent(this, TeacherHome.class);
                else if (id == R.id.nav_schedule)
                    intent = new Intent(this, teacherSchedule.class);
                else if (id == R.id.nav_assignments)
                    intent = new Intent(this, ClassList_Activity.class).putExtra("nav", "assignments");
                else if (id == R.id.nav_marks)
                    intent = new Intent(this, ClassList_Activity.class).putExtra("nav", "marks");
                else if (id == R.id.nav_logout)
                    intent = new Intent(this, LogIn.class);
                else if (id == R.id.nav_dark_mode) {
                    Menu menu = nav.getMenu();
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
                }
            }

            if (intent != null) {
                startActivity(intent);
                drawerLayout.closeDrawers();
            }

            return true;
        });
    }

    private void toggleDark() {
        MenuItem dark = nav.getMenu().findItem(R.id.nav_dark_mode);
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

    private void fetchTeacherSchedule() {
        String url = "http://10.0.2.2/php_project/teacherSched.php?teacher_id=" + teacherId;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                this::populateScheduleTable,
                error -> Toast.makeText(this, "Failed to load schedule", Toast.LENGTH_SHORT).show());
        queue.add(request);
    }

    private void populateScheduleTable(JSONObject data) {
        TableLayout table = findViewById(R.id.tableLayout);
        try {
            Map<String, Map<String, String>> scheduleMap = new HashMap<>();
            Iterator<String> dayKeys = data.keys();
            while (dayKeys.hasNext()) {
                String day = dayKeys.next();
                JSONObject times = data.getJSONObject(day);
                Map<String, String> timeMap = new HashMap<>();
                Iterator<String> timeKeys = times.keys();
                while (timeKeys.hasNext()) {
                    String time = timeKeys.next();
                    timeMap.put(time, times.getString(time));
                }
                scheduleMap.put(day, timeMap);
            }

            for (int r = 1; r <= DAYS.length; r++) {
                TableRow row = (TableRow) table.getChildAt(r);
                String day = DAYS[r - 1];
                for (int c = 1; c <= TIME_KEYS.length; c++) {
                    TextView cell = (TextView) row.getChildAt(c);
                    String timeKey = TIME_KEYS[c - 1];
                    String subject = scheduleMap.containsKey(day) && scheduleMap.get(day).containsKey(timeKey)
                            ? scheduleMap.get(day).get(timeKey)
                            : "";
                    cell.setText(subject);
                }
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
