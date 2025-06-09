package com.example.schoolsystemapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScheduleSelector extends AppCompatActivity {

    private String subjectName = "Math";         // Replace with real selection
    private int gradeLevel = 7;                  // Replace with real selection
    private int teacherId = 5;                   // Replace with real selection
    private int registrarId = 1;                 // Replace with logged-in registrar

    private String[][] timeSlots = {
            {"sun_1", "Sunday", "8:00 - 8:50"},
            {"sun_2", "Sunday", "8:50 - 9:35"},
            {"sun_3", "Sunday", "9:35 - 10:25"},
            {"sun_4", "Sunday", "10:25 - 11:35"},
            {"sun_5", "Sunday", "11:35 - 12:20"},
            {"sun_6", "Sunday", "12:20 - 1:05"},
            {"sun_7", "Sunday", "1:05 - 1:50"},
            {"mon_1", "Monday", "8:00 - 8:50"},
            {"mon_2", "Monday", "8:50 - 9:35"},
            {"mon_3", "Monday", "9:35 - 10:25"},
            {"mon_4", "Monday", "10:25 - 11:35"},
            {"mon_5", "Monday", "11:35 - 12:20"},
            {"mon_6", "Monday", "12:20 - 1:05"},
            {"mon_7", "Monday", "1:05 - 1:50"},
            {"tue_1", "Tuesday", "8:00 - 8:50"},
            {"tue_2", "Tuesday", "8:50 - 9:35"},
            {"tue_3", "Tuesday", "9:35 - 10:25"},
            {"tue_4", "Tuesday", "10:25 - 11:35"},
            {"tue_5", "Tuesday", "11:35 - 12:20"},
            {"tue_6", "Tuesday", "12:20 - 1:05"},
            {"tue_7", "Tuesday", "1:05 - 1:50"},
            {"wed_1", "Wednesday", "8:00 - 8:50"},
            {"wed_2", "Wednesday", "8:50 - 9:35"},
            {"wed_3", "Wednesday", "9:35 - 10:25"},
            {"wed_4", "Wednesday", "10:25 - 11:35"},
            {"wed_5", "Wednesday", "11:35 - 12:20"},
            {"wed_6", "Wednesday", "12:20 - 1:05"},
            {"wed_7", "Wednesday", "1:05 - 1:50"},
            {"thu_1", "Thursday", "8:00 - 8:50"},
            {"thu_2", "Thursday", "8:50 - 9:35"},
            {"thu_3", "Thursday", "9:35 - 10:25"},
            {"thu_4", "Thursday", "10:25 - 11:35"},
            {"thu_5", "Thursday", "11:35 - 12:20"},
            {"thu_6", "Thursday", "12:20 - 1:05"},
            {"thu_7", "Thursday", "1:05 - 1:50"}
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

        Button buttonAdd = findViewById(R.id.buttonaddsub);
        buttonAdd.setOnClickListener(view -> sendScheduleToServer());
    }

    private void sendScheduleToServer() {
        String url = "http://10.0.2.2/php_project/schedule_selector.php";

        JSONArray scheduleArray = new JSONArray();

        for (String[] slot : timeSlots) {
            String checkboxIdStr = slot[0];
            String day = slot[1];
            String time = slot[2];

            int resID = getResources().getIdentifier(checkboxIdStr, "id", getPackageName());
            CheckBox cb = findViewById(resID);
            if (cb != null && cb.isChecked()) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("day", day);
                    obj.put("time", time);
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

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Schedule submitted!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("subject_name", subjectName);
                params.put("grade_level", String.valueOf(gradeLevel));
                params.put("teacher_id", String.valueOf(teacherId));
                params.put("registrar_id", String.valueOf(registrarId));
                params.put("schedule", scheduleArray.toString());
                return params;
            }
        };

        queue.add(request);
    }
}
