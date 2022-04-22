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
    public String hashtag;
    public double[] location;
    public boolean completed;
    public int Alarm_No;
    public int repeat;

    public Reminder() {
        id = UUID.randomUUID().toString();
        title = "Task Name";
        description = "";
        hashtag = "";
        image = null;
        voice = null;
        date = "";
        time = "";
        location = new double[2];
        completed = false;
        repeat = 0;
    }

    public Reminder(String id, String title, String description, String hashtag, String image,
                    Voice voice, String date, int Alarm_No, int repeat,
                    String time, double[] location, boolean complete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.hashtag = hashtag;
        this.image = image;
        this.voice = voice;
        this.date = date;
        this.time = time;
        this.location = location;
        this.completed = complete;
        this.Alarm_No = Alarm_No;
        this.repeat = repeat;
    }
}
