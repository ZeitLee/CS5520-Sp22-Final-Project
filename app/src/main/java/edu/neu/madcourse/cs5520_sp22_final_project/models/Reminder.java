package edu.neu.madcourse.cs5520_sp22_final_project.models;

public class Reminder {
    private String title;
    private String description;
    private String image;
    private String date;
    private String time;
    private String location;

    public Reminder() {

    }

    public Reminder(String title, String description, String image, String date, String time, String location) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
        this.time = time;
        this.location = location;
    }
}
