package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolsystemapplication.Data.ScheduleEntry;
import com.example.schoolsystemapplication.Data.Student;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class viewMark_andEdit_activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private TableLayout tableLayout;
    private int studentId;
    private int subjectId;
    private String classNum;
    private TextView studentName_TextView;
    private static final String BASE_URL = "http://10.0.2.2:80/php_project/get_mark.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_mark_and_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tableLayout = (TableLayout) findViewById(R.id.marksTable);
        studentName_TextView = (TextView) findViewById(R.id.subjectTextView);

        Intent intent10 = getIntent();
        studentId = intent10.getIntExtra("student_id", 0);
        subjectId = intent10.getIntExtra("subject_id", 0);
        classNum = intent10.getStringExtra("classNum");

        loadMark();

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
            } else if (id == R.id.nav_addMarks) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                intent1.putExtra("nav", "addmarks");
                startActivity(intent1);
            } else if (id == R.id.nav_logout) {
                Intent intent1 = new Intent(this, LogIn.class);
                startActivity(intent1);
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    public void loadMark(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            students.clear();
                            JSONArray array = new JSONArray(response);
                            studentName_TextView.setText(array.getJSONObject(0).getString("studentName"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                TableRow tableRow = new TableRow(viewMark_andEdit_activity.this);
                                tableRow.setLayoutParams(new TableRow.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT));

                                TextView examTextView = new TextView(viewMark_andEdit_activity.this);
                                examTextView.setText(i + 1 + "");
                                examTextView.setPadding(16, 16, 16, 16);
                                examTextView.setBackgroundResource(R.drawable.cell_border);

                                TextView typeTextView = new TextView(viewMark_andEdit_activity.this);
                                typeTextView.setText(object.getString("type_mark"));
                                typeTextView.setPadding(16, 16, 16, 16);
                                typeTextView.setBackgroundResource(R.drawable.cell_border);

                                TextView maxTextView = new TextView(viewMark_andEdit_activity.this);
                                maxTextView.setText(object.getDouble("maxMark") + "");
                                maxTextView.setPadding(16, 16, 16, 16);
                                maxTextView.setBackgroundResource(R.drawable.cell_border);

                                EditText markEditText = new EditText(viewMark_andEdit_activity.this);
                                markEditText.setText(object.getDouble("mark") + "");
                                markEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                markEditText.setPadding(16, 16, 16, 16);
                                markEditText.setBackgroundResource(R.drawable.cell_border);

                                TextView dateTextView = new TextView(viewMark_andEdit_activity.this);
                                dateTextView.setText(object.getString("created_at"));
                                dateTextView.setPadding(16, 16, 16, 16);
                                dateTextView.setBackgroundResource(R.drawable.cell_border);

                                TextView editTextView  = new TextView (viewMark_andEdit_activity.this);
                                editTextView.setText(R.string.edit);
                                editTextView.setPadding(16, 16, 16, 16);
                                editTextView.setBackgroundResource(R.drawable.cell_border);
                                editTextView.setTextColor(ContextCompat.getColor(viewMark_andEdit_activity.this, R.color.blue));
                                editTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String newMarkStr = markEditText.getText().toString().trim();
                                        if (newMarkStr.isEmpty()) {
                                            Toast.makeText(viewMark_andEdit_activity.this, "Please enter a valid mark", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        double newMark = Double.parseDouble(newMarkStr);
                                        String url = "http://10.0.2.2:80/php_project/update_mark.php";
                                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                                response -> {
                                                    Toast.makeText(viewMark_andEdit_activity.this, "The modification has been saved successfully.", Toast.LENGTH_SHORT).show();
                                                },
                                                error -> {
                                                    Toast.makeText(viewMark_andEdit_activity.this, "Failed to save", Toast.LENGTH_SHORT).show();
                                                }
                                        ) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("student_id", String.valueOf(studentId));
                                                params.put("subject_id", String.valueOf(subjectId));
                                                try {
                                                    params.put("old_mark", String.valueOf(object.getDouble("mark")));
                                                    params.put("typeMark", object.getString("type_mark"));
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                params.put("new_mark", String.valueOf(newMark));
                                                return params;
                                            }
                                        };
                                        Volley.newRequestQueue(viewMark_andEdit_activity.this).add(request);
                                    }
                                });

                                TableRow.LayoutParams cellParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                                examTextView.setLayoutParams(cellParams);
                                typeTextView.setLayoutParams(cellParams);
                                maxTextView.setLayoutParams(cellParams);
                                markEditText.setLayoutParams(cellParams);
                                dateTextView.setLayoutParams(cellParams);
                                editTextView.setLayoutParams(cellParams);

                                examTextView.setGravity(Gravity.CENTER);
                                typeTextView.setGravity(Gravity.CENTER);
                                maxTextView.setGravity(Gravity.CENTER);
                                markEditText.setGravity(Gravity.CENTER);
                                dateTextView.setGravity(Gravity.CENTER);

                                examTextView.setTextSize(14);
                                typeTextView.setTextSize(14);
                                maxTextView.setTextSize(14);
                                markEditText.setTextSize(14);
                                dateTextView.setTextSize(14);

                                typeTextView.setSingleLine(true);
                                typeTextView.setEllipsize(TextUtils.TruncateAt.END);
                                dateTextView.setSingleLine(true);
                                dateTextView.setEllipsize(TextUtils.TruncateAt.END);

                                tableRow.addView(examTextView);
                                tableRow.addView(typeTextView);
                                tableRow.addView(maxTextView);
                                tableRow.addView(markEditText);
                                tableRow.addView(dateTextView);
                                tableRow.addView(editTextView);

                                tableLayout.setShrinkAllColumns(false);
                                tableLayout.setStretchAllColumns(true);
                                tableLayout.addView(tableRow);
                            }
                        } catch (Exception e) {
                            Toast.makeText(viewMark_andEdit_activity.this, "Grades have not been entered yet", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(viewMark_andEdit_activity.this, "Volley error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("student_id", String.valueOf(studentId));
                params.put("subject_id", String.valueOf(subjectId));
                return params;
            }
        };
        Volley.newRequestQueue(viewMark_andEdit_activity.this).add(stringRequest);
    }

    public void btn_OnClick_back(View view) {
        Intent intent = new Intent(this, StudentList_insertMark.class);
        intent.putExtra("classNum", classNum);
        intent.putExtra("subject", subjectId);
        startActivity(intent);
    }

//    public void btn_OnClick_cancel(View view) {
//        Intent intent = new Intent(this, StudentList_insertMark.class);
//        intent.putExtra("classNum", classNum);
//        intent.putExtra("subject", subjectId);
//        startActivity(intent);
//    }

//    public void btn_OnClick_Save(View view) {
//
//
//
//        Intent intent = new Intent(this, StudentList_insertMark.class);
//        intent.putExtra("classNum", classNum);
//        intent.putExtra("subject", subjectId);
//        startActivity(intent);
//    }

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