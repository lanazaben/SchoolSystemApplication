package com.example.schoolsystemapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewMarks extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private static final String BASE_URL = "http://10.0.2.2:80/php_project/get_mark.php";
    private TableLayout tableLayout;
    private TextView subjectName_TextView;
    private int student_id;
    private int subject_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_marks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tableLayout = (TableLayout) findViewById(R.id.marksTable);
        subjectName_TextView = (TextView) findViewById(R.id.subjectTextView);

        drawerLayout = findViewById(R.id.drawer_layout);

        Intent intent10 = getIntent();
        student_id = intent10.getIntExtra("student_id",0);
        subject_id = intent10.getIntExtra("subject_id",0);

        loadMark();

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
                Intent schedule=new Intent (ViewMarks.this,StudentSchedule.class);
                startActivity(schedule);

            } else if (id == R.id.nav_assignment) {
                Intent allAssignemnt=new Intent (ViewMarks.this,allAssignmentPage.class);
                startActivity(allAssignemnt);

            } else if (id == R.id.nav_mark) {
                //opens subjects list of this student, to show marks for this subject alone
                Intent subjectBeforeMark=new Intent (ViewMarks.this, SubjectsList.class);
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

    public void loadMark(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);
                            subjectName_TextView.setText(array.getJSONObject(0).getString("subjectName"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                TableRow tableRow = new TableRow(ViewMarks.this);
                                tableRow.setLayoutParams(new TableRow.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT));

                                TextView examTextView = new TextView(ViewMarks.this);
                                examTextView.setText(i + 1 + "");
                                examTextView.setPadding(16, 16, 16, 16);
                                examTextView.setBackgroundResource(R.drawable.cell_border);

                                TextView typeTextView = new TextView(ViewMarks.this);
                                typeTextView.setText(object.getString("type_mark"));
                                typeTextView.setPadding(16, 16, 16, 16);
                                typeTextView.setBackgroundResource(R.drawable.cell_border);

                                TextView maxTextView = new TextView(ViewMarks.this);
                                maxTextView.setText(object.getDouble("maxMark") + "");
                                maxTextView.setPadding(16, 16, 16, 16);
                                maxTextView.setBackgroundResource(R.drawable.cell_border);

                                EditText markEditText = new EditText(ViewMarks.this);
                                markEditText.setText(object.getDouble("mark") + "");
                                markEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                markEditText.setPadding(16, 16, 16, 16);
                                markEditText.setBackgroundResource(R.drawable.cell_border);

                                TextView dateTextView = new TextView(ViewMarks.this);
                                dateTextView.setText(object.getString("created_at"));
                                dateTextView.setPadding(16, 16, 16, 16);
                                dateTextView.setBackgroundResource(R.drawable.cell_border);

                                TableRow.LayoutParams cellParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                                examTextView.setLayoutParams(cellParams);
                                typeTextView.setLayoutParams(cellParams);
                                maxTextView.setLayoutParams(cellParams);
                                markEditText.setLayoutParams(cellParams);
                                dateTextView.setLayoutParams(cellParams);

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

                                tableLayout.setShrinkAllColumns(false);
                                tableLayout.setStretchAllColumns(true);
                                tableLayout.addView(tableRow);
                            }
                        } catch (Exception e) {
                            Toast.makeText(ViewMarks.this, "Grades have not been entered yet", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewMarks.this, "Volley error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("student_id", String.valueOf(student_id));
                params.put("subject_id", String.valueOf(subject_id));
                return params;
            }
        };
        Volley.newRequestQueue(ViewMarks.this).add(stringRequest);
    }

    public void btn_OnClick_back(View view) {
        Intent intent = new Intent(this, SubjectsList.class);
        startActivity(intent);
    }

}