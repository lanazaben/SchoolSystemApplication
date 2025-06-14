package com.example.schoolsystemapplication.Data;

import java.util.Date;

public class Assignment {

    private int assignmentId;
    private int gradeLevel;
    private CharSequence subject;
    private Teacher teacher;
    private String title;
    private String description;
    private CharSequence dueDate;

    public Assignment(){}

    public Assignment(int assignmentId, int gradeLevel, CharSequence subject, Teacher teacher, String title, String description, CharSequence dueDate) {
        this.assignmentId = assignmentId;
        this.gradeLevel = gradeLevel;
        this.subject = subject;
        this.teacher = teacher;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public Assignment(String title, String subject, String dueDate) {
        this.title = title;
        this.subject = subject;
        this.dueDate = dueDate;
    }

    public Assignment( CharSequence subject, String title) {
        this.subject = subject;
        this.title = title;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public CharSequence getSubject() {
        return subject;
    }

    public void setSubject(CharSequence subject) {
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

    public CharSequence getDueDate() {
        return dueDate;
    }

    public void setDueDate(CharSequence dueDate) {
        this.dueDate = dueDate;
    }
}
