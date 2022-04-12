package edu.neu.madcourse.cs5520_sp22_final_project.models;

import android.location.Location;
import android.media.Image;
import android.speech.tts.Voice;

import java.util.UUID;

public class Reminder {
    public String id;
    public String title;
    public String description;
    public Image image;
    public Voice voice;
    public String date;
    public String time;
    public Location location;

    public Reminder() {
        id = UUID.randomUUID().toString();
        title = "Task Name";
        description = "";
        image = null;
        voice = null;
        date = "";
        time = "";
        location = null;
    }

    public Reminder(String id, String title, String description, Image image, Voice voice, String date,
                    String time, Location location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.voice = voice;
        this.date = date;
        this.time = time;
        this.location = location;
    }
}
