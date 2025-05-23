package com.example.schoolsystemapplication.Data;

import java.util.Date;

public class Mark {
    private int id;
    private Student student;
    private SchoolSubject subject;
    private double score,min,max;
    private String Name, Type;
    private Date DateOfPublish;


    public Mark(int id, Student student, SchoolSubject subject, double score, double min, double max, String name, String type, Date dateOfPublish) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.score = score;
        this.min = min;
        this.max = max;
        Name = name;
        Type = type;
        DateOfPublish = dateOfPublish;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Date getDateOfPublish() {
        return DateOfPublish;
    }

    public void setDateOfPublish(Date dateOfPublish) {
        DateOfPublish = dateOfPublish;
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
