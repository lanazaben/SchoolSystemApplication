package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewClassSchedule extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Button btnView, btnAdd, btnEdit;

    private String gradeLevelStr;
    private String subjectName;
    private String teacherId ;
    private String currentRegistrarId ;
    private String currentScheduleJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class_schedule);
        Intent intent = getIntent();

        // Retrieve grade level
         gradeLevelStr = intent.getStringExtra("grade_level");

        // Get teacherId and registrarId if passed
        teacherId = intent.getStringExtra("teacher_id");
        if (teacherId == null) {
            teacherId = "1"; // fallback or handle accordingly
        }

        currentRegistrarId = intent.getStringExtra("registrar_id");
        if (currentRegistrarId == null) {
            currentRegistrarId = "1"; // fallback or handle accordingly
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        btnView = findViewById(R.id.buttonViewStu);
        btnAdd = findViewById(R.id.buttonAddStudent);
        btnEdit = findViewById(R.id.buttonEditSub);


        loadScheduleData(gradeLevelStr);

        btnView.setOnClickListener(v -> {
            Intent intent1 = new Intent(ViewClassSchedule.this, StudentClass_list.class);
            intent1.putExtra("from", "registrar");
            intent1.putExtra("nav", "view_student");
            intent1.putExtra("grade_level", gradeLevelStr);
            Log.d("btn", "222222");
            startActivity(intent1);
        });

        btnEdit.setOnClickListener(v -> {
            if (subjectName != null && currentScheduleJson != null) {
                Intent intent2 = new Intent(ViewClassSchedule.this, ScheduleSelector.class);
                intent2.putExtra("subject_name", subjectName);
                intent2.putExtra("teacher_id", teacherId);
                intent2.putExtra("grade_level", gradeLevelStr);
                intent2.putExtra("selected_schedule", currentScheduleJson);
                startActivityForResult(intent2, 1001);
            } else {
                Toast.makeText(this, "Please select a subject from the table first", Toast.LENGTH_SHORT).show();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ViewClassSchedule.this, AddStudent.class);
                intent1.putExtra("from", "registrar");
                intent1.putExtra("nav", "add_student");
                intent1.putExtra("grade_level", gradeLevelStr);
                Log.d("btn", "222222");
                startActivity(intent1);
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent navIntent = null;

            if (id == R.id.nav_teacher) {
                navIntent = new Intent(this, teacher_list.class);
                navIntent.putExtra("from", "registrar");
            } else if (id == R.id.nav_GradeLevel) {
                navIntent = new Intent(this, ClassList_Activity.class);
                navIntent.putExtra("from", "registrar");
                navIntent.putExtra("nav", "view_student");
            } else if (id == R.id.nav_subject) {
                navIntent = new Intent(this, AddSubject.class);
            } else if (id == R.id.nav_logout) {
                navIntent = new Intent(this, LogIn.class);
            } else if (id == R.id.nav_dark_mode) {
                toggleDark();
                return true;
            }

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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadScheduleData(String gradeLevel) {
        String url = "http://10.0.2.2:80/php_project/get_schedule.php?grade_level=" + gradeLevelStr;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    TableLayout tableLayout = findViewById(R.id.tableLayout);

                    String[] days = {"sunday", "monday", "tuesday", "wednesday", "thursday"};
                    String[] timeSlots = {"8:00", "8:50", "9:35", "10:25", "11:35", "12:20", "1:05"};

                    JSONObject scheduleObj = response.optJSONObject("schedule");
                    if (scheduleObj == null) {
                        Toast.makeText(this, "No schedule data found", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (int i = 0; i < days.length; i++) {
                        TableRow row = (TableRow) tableLayout.getChildAt(i + 1);
                        JSONObject dayObject = scheduleObj.optJSONObject(capitalize(days[i]));
                        if (dayObject != null) {
                            for (int j = 0; j < timeSlots.length; j++) {
                                String timeSlot = timeSlots[j];
                                String subject = dayObject.optString(timeSlot, "");
                                TextView cell = (TextView) row.getChildAt(j + 1);
                                cell.setText(subject);

                                if (!subject.isEmpty()) {
                                    cell.setOnClickListener(v -> {
                                        subjectName = subject.split(" \\(")[0].trim();
                                        JSONArray selectedSlots = new JSONArray();
                                        try {
                                            for (String day : days) {
                                                JSONObject dObj = scheduleObj.optJSONObject(capitalize(day));
                                                if (dObj == null) continue;
                                                for (String slot : timeSlots) {
                                                    String val = dObj.optString(slot, "");
                                                    if (val.startsWith(subjectName)) {
                                                        JSONObject timeObj = new JSONObject();
                                                        timeObj.put("day", capitalize(day));
                                                        timeObj.put("time", convertSlotToTime(slot));
                                                        selectedSlots.put(timeObj);
                                                    }
                                                }
                                            }
                                            currentScheduleJson = selectedSlots.toString();
                                            Toast.makeText(this, "Slots selected for edit", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(this, "Failed to prepare edit data", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            }
                        }
                    }
                },
                error -> Toast.makeText(this, "Error loading schedule", Toast.LENGTH_LONG).show()
        );

        queue.add(request);
    }

    private String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    private String convertSlotToTime(String slot) {
        switch (slot) {
            case "8:00": return "08:00 - 09:00 AM";
            case "8:50": return "09:00 - 10:00 AM";
            case "9:35": return "10:15 - 11:15 AM";
            case "10:25": return "11:30 - 12:30 PM";
            case "11:35": return "12:30 - 01:30 PM";
            case "12:20": return "01:30 - 02:30 PM";
            case "1:05": return "02:30 - 03:30 PM";
            default: return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            String updatedScheduleJson = data.getStringExtra("schedule_json");
            if (updatedScheduleJson != null) {
                sendUpdatedScheduleToServer(updatedScheduleJson);
            }
        }
    }

    private void sendUpdatedScheduleToServer(String scheduleJson) {
        String url = "http://10.0.2.2:80/php_project/update_schedule.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Schedule updated successfully", Toast.LENGTH_SHORT).show();
                    loadScheduleData(gradeLevelStr);
                },
                error -> {
                    String msg = "Error updating schedule";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String body = new String(error.networkResponse.data);
                        try {
                            JSONObject jo = new JSONObject(body);
                            msg = jo.optString("error", msg);
                        } catch (JSONException e) { }
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", subjectName);
                params.put("teacher_id", teacherId);
                params.put("grade_level", gradeLevelStr);
                params.put("registrar_id", currentRegistrarId);
                params.put("schedule", scheduleJson);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
