package com.example.schoolsystemapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolsystemapp.Data.ScheduleEntry;
import com.example.schoolsystemapp.Data.SchoolSubject;
import com.example.schoolsystemapp.Data.Teacher;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

public class teacher_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recycler = findViewById(R.id.teacherList);

        List<Teacher> teachers = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            int id = i;
            String name = "Teacher " + i;
            String email = "teacher" + i + "@school.com";

            Teacher teacher = null;

            List<SchoolSubject> subjects = new ArrayList<>();
            SchoolSubject subject1 = new SchoolSubject(i * 10, "Math", 10, null);
            SchoolSubject subject2 = new SchoolSubject(i * 10 + 1, "Science", 10, null);
            subjects.add(subject1);
            subjects.add(subject2);

            teacher = new Teacher(id, name, email, subjects, new ArrayList<>());

            subject1 = new SchoolSubject(i * 10, "Math", 10, teacher);
            subject2 = new SchoolSubject(i * 10 + 1, "Science", 10, teacher);
            subjects.set(0, subject1);
            subjects.set(1, subject2);

            List<ScheduleEntry> schedule = new ArrayList<>();
            schedule.add(new ScheduleEntry(i * 100, subject1, "Sunday", "08:00"));
            schedule.add(new ScheduleEntry(i * 100 + 1, subject2, "Monday", "10:00"));

            teacher = new Teacher(id, name, email, subjects, schedule);
            teachers.add(teacher);
        }


        Teacher[] captions = new Teacher[teachers.size()];
        int[] ids = new int[teachers.size()];

        for(int i = 0; i<captions.length;i++){
            captions[i] = teachers.get(i);
        }
        recycler.setLayoutManager(new LinearLayoutManager(this));
        CaptionedTeacherAdapter adapter = new CaptionedTeacherAdapter(captions, this);
        recycler.setAdapter(adapter);

    }
}