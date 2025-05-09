package com.example.schoolsystemapplication.Data;

public class ScheduleEntry {
    private int id;
    private SchoolSubject subject;
    private String dayOfWeek;
    private String timeSlot; // e.g., "09:00 - 10:00 AM"

    public ScheduleEntry(int id, SchoolSubject subject, String dayOfWeek, String timeSlot) {
        this.id = id;
        this.subject = subject;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
