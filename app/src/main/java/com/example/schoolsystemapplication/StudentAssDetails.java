package com.example.schoolsystemapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolsystemapplication.Data.Assignment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StudentAssDetails extends AppCompatActivity {
    Assignment ass = new Assignment();
    private EditText editTextId, editTextSubject, editTextTeacher, editTextTitle, editTextDescription;
    private TextView textViewDueDate;
    private Button buttonSubmit;
    private static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_ass_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextId = findViewById(R.id.editTextId);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextTeacher = findViewById(R.id.editTextTeacher);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewDueDate = findViewById(R.id.textViewDueDate);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);
        });

        String title = getIntent().getStringExtra("title");
        if (title == null || title.isEmpty()) {
            Toast.makeText(this, "No title received", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String encodedTitle = URLEncoder.encode(title, "UTF-8");
            String url = "http://10.0.2.2/phpFiles/update_assignment.php?title=" + encodedTitle;

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                JSONObject obj = jsonArray.getJSONObject(0);
                                editTextId.setText(obj.getString("assignment_id"));
                                editTextSubject.setText(obj.getString("subject_name"));
                                editTextTeacher.setText(obj.getString("teacher_name"));
                                editTextTitle.setText(obj.getString("title"));
                                editTextDescription.setText(obj.getString("description"));
                                textViewDueDate.setText(obj.getString("due_date"));
                            } else {
                                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }

                    },
                    error -> Toast.makeText(this, "Connection error: " + error.getMessage(), Toast.LENGTH_LONG).show());

            Volley.newRequestQueue(this).add(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Encoding failed", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            Toast.makeText(this, "File selected: " + fileUri.getPath(), Toast.LENGTH_SHORT).show();
        }
    }
}