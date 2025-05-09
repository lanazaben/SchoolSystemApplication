package com.example.schoolsystemapp.Data;

import java.util.List;

public class Teacher {
    private int id;
    private String name;
    private String email;
    private List<SchoolSubject> subjects;
    private List<ScheduleEntry> schedule;

    public Teacher(int id, String name, String email, List<SchoolSubject> subjects, List<ScheduleEntry> schedule) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subjects = subjects;
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<SchoolSubject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SchoolSubject> subjects) {
        this.subjects = subjects;
    }

    public List<ScheduleEntry> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleEntry> schedule) {
        this.schedule = schedule;
    }
}
