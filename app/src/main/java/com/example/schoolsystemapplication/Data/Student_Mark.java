package com.example.schoolsystemapplication.Data;

public class Student_Mark {
    private int studentId;
    private double mark;

    public Student_Mark(int studentId, double mark) {
        this.studentId = studentId;
        this.mark = mark;
    }

    public void setMark(double mark){
        this.mark = mark;
    }

    public int getStudentId(){
        return studentId;
    }

    public double getMark(){
        return mark;
    }

}
