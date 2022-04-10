package edu.neu.madcourse.cs5520_sp22_final_project.models;

import android.location.Location;
import android.media.Image;
import android.speech.tts.Voice;

import java.util.UUID;

public class Reminder {
    private String id;
    private String title;
    private String description;
    private Image image;
    private Voice voice;
    private String date;
    private String time;
    private Location location;

    public Reminder() {
        id = UUID.randomUUID().toString();
        title = "";
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
