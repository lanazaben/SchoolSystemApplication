package com.example.schoolsystemapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddExamActivity extends AppCompatActivity {

    EditText editText_typeExam;
    EditText editText_fullMark;

    String exam_Type;
    int full_Mark;

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
}