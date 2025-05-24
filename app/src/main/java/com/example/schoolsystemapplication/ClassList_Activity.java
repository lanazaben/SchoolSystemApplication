package com.example.schoolsystemapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ClassList_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.activity_class_list);

        RecyclerView mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        String[] ClassName = {"First Grade", "Second Grade", "Third Grade", "Fourth Grade", "Fifth Grade"};
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter_recyclerview_className adapter = new Adapter_recyclerview_className(ClassName, this);
        mainRecyclerView.setAdapter(adapter);
    }
}