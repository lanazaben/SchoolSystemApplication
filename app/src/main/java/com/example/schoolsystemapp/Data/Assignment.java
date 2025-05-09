package com.example.schoolsystemapp.Data;

import java.util.Date;

public class Assignment {
    private int id;
    private SchoolSubject subject;
    private Teacher teacher;
    private String title;
    private String description;
    private Date dueDate; // written like that: "2025-05-30"

    public Assignment(int id, SchoolSubject subject, Teacher teacher, String title, String description, Date dueDate) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SchoolSubject getSubject() {
        return subject;
    }

    public void setSubject(SchoolSubject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
