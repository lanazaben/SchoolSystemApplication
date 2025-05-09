package com.example.schoolsystemapp.Data;

public class SchoolSubject {
    private int id;
    private String name;
    private int gradeLevel;
    private Teacher teacher;

    public SchoolSubject(int id, String name, int gradeLevel, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.gradeLevel = gradeLevel;
        this.teacher = teacher;
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

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
