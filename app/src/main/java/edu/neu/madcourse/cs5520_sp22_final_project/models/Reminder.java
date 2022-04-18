package edu.neu.madcourse.cs5520_sp22_final_project.models;

import android.location.Location;
import android.media.Image;
import android.speech.tts.Voice;

import java.util.UUID;

public class Reminder {
    public String id;
    public String title;
    public String description;
    public String image;
    public Voice voice;
    public String date;
    public String time;
    public String location;
    public boolean completed;

    public Reminder() {
        id = UUID.randomUUID().toString();
        title = "Task Name";
        description = "";
        image = null;
        voice = null;
        date = "";
        time = "";
        location = null;
        completed = false;
    }

    public Reminder(String id, String title, String description, String image, Voice voice, String date,
                    String time, String location, boolean complete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.voice = voice;
        this.date = date;
        this.time = time;
        this.location = location;
        this.completed = complete;
    }
}
