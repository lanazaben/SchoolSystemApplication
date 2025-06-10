package com.example.schoolsystemapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleSelector extends AppCompatActivity {

    private String subjectName;
    private int gradeLevel;
    private int teacherId;

    private final String[][] timeSlots = {
            {"sun_1", "Sunday", "08:00 - 09:00 AM"},
            {"sun_2", "Sunday", "09:00 - 10:00 AM"},
            {"sun_3", "Sunday", "10:15 - 11:15 AM"},
            {"sun_4", "Sunday", "11:30 - 12:30 PM"},
            {"sun_5", "Sunday", "12:30 - 01:30 PM"},
            {"sun_6", "Sunday", "01:30 - 02:30 PM"},
            {"sun_7", "Sunday", "02:30 - 03:30 PM"},
            {"mon_1", "Monday", "08:00 - 09:00 AM"},
            {"mon_2", "Monday", "09:00 - 10:00 AM"},
            {"mon_3", "Monday", "10:15 - 11:15 AM"},
            {"mon_4", "Monday", "11:30 - 12:30 PM"},
            {"mon_5", "Monday", "12:30 - 01:30 PM"},
            {"mon_6", "Monday", "01:30 - 02:30 PM"},
            {"mon_7", "Monday", "02:30 - 03:30 PM"},
            {"tue_1", "Tuesday", "08:00 - 09:00 AM"},
            {"tue_2", "Tuesday", "09:00 - 10:00 AM"},
            {"tue_3", "Tuesday", "10:15 - 11:15 AM"},
            {"tue_4", "Tuesday", "11:30 - 12:30 PM"},
            {"tue_5", "Tuesday", "12:30 - 01:30 PM"},
            {"tue_6", "Tuesday", "01:30 - 02:30 PM"},
            {"tue_7", "Tuesday", "02:30 - 03:30 PM"},
            {"wed_1", "Wednesday", "08:00 - 09:00 AM"},
            {"wed_2", "Wednesday", "09:00 - 10:00 AM"},
            {"wed_3", "Wednesday", "10:15 - 11:15 AM"},
            {"wed_4", "Wednesday", "11:30 - 12:30 PM"},
            {"wed_5", "Wednesday", "12:30 - 01:30 PM"},
            {"wed_6", "Wednesday", "01:30 - 02:30 PM"},
            {"wed_7", "Wednesday", "02:30 - 03:30 PM"},
            {"thu_1", "Thursday", "08:00 - 09:00 AM"},
            {"thu_2", "Thursday", "09:00 - 10:00 AM"},
            {"thu_3", "Thursday", "10:15 - 11:15 AM"},
            {"thu_4", "Thursday", "11:30 - 12:30 PM"},
            {"thu_5", "Thursday", "12:30 - 01:30 PM"},
            {"thu_6", "Thursday", "01:30 - 02:30 PM"},
            {"thu_7", "Thursday", "02:30 - 03:30 PM"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_selector);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        subjectName = getIntent().getStringExtra("subject_name");
        teacherId = getIntent().getIntExtra("teacher_id", -1);
        try {
            gradeLevel = Integer.parseInt(getIntent().getStringExtra("grade_level"));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid grade level", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(view -> finish());

        Button buttonAdd = findViewById(R.id.buttonaddsub);
        buttonAdd.setOnClickListener(view -> returnSelectedSchedule());
    }

    private void returnSelectedSchedule() {
        JSONArray scheduleArray = new JSONArray();

        for (String[] slot : timeSlots) {
            int resID = getResources().getIdentifier(slot[0], "id", getPackageName());
            CheckBox cb = findViewById(resID);
            if (cb != null && cb.isChecked()) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("day", slot[1]);
                    obj.put("time", slot[2]);
                    scheduleArray.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (scheduleArray.length() == 0) {
            Toast.makeText(this, "Please select at least one time slot.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("schedule_json", scheduleArray.toString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
