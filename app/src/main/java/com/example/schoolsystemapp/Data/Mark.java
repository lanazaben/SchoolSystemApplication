package com.example.schoolsystemapp.Data;

public class Mark {
    private int id;
    private Student student;
    private SchoolSubject subject;
    private double score;

    public Mark(int id, Student student, SchoolSubject subject, double score) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public SchoolSubject getSubject() {
        return subject;
    }

    public void setSubject(SchoolSubject subject) {
        this.subject = subject;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
