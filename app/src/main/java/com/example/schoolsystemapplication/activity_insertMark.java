package com.example.schoolsystemapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class activity_insertMark extends AppCompatActivity {

    RecyclerView mainRecyclerView;
    TextView typeExam;
    TextView fullMark;
    String exam_Type;
    int full_Mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_mark);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.activity_insert_mark);
        typeExam = findViewById(R.id.textView_typeExam);
        fullMark = findViewById(R.id.textView_fullMark);

        Intent intent = getIntent();
        exam_Type = intent.getStringExtra("examType");
        full_Mark = intent.getIntExtra("fullMark", 0);
        typeExam.setText(exam_Type + ": ");
        fullMark.setText(" /" + full_Mark);

        mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        String[] studentName = {"Doaa Assi", "Lana Zaben", "Yara Hamad", "Hiba Awwad"};
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter_insertMark adapter = new Adapter_insertMark(studentName, this);
        mainRecyclerView.setAdapter(adapter);
    }

    public void btn_OnClick_back(View view) {
        Intent intent = new Intent(this, AddExamActivity.class);
        intent.putExtra("examType", exam_Type);
        intent.putExtra("fullMark", full_Mark);
        startActivity(intent);
    }
}