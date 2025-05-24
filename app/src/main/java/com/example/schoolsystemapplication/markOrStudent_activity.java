package com.example.schoolsystemapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class markOrStudent_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mark_or_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.activity_mark_or_student);
    }

    public void btn_OnClick_addMark(View view) {
        Intent intent = new Intent(this, AddExamActivity.class);
        startActivity(intent);
    }

    public void btn_OnClick_listStudent(View view) {
        Intent intent = new Intent(this, StudentList_insertMark.class);
        startActivity(intent);
    }

    public void btn_OnClick_back(View view) {
        Intent intent = new Intent(this, ClassList_Activity.class);
        startActivity(intent);
    }
}