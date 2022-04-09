package Model;

import android.location.Location;
import android.media.Image;
import android.speech.tts.Voice;

public class TaskImpl implements Task {
    private String title;
    private String description;
    private Image image;
    private Voice voice;
    private String date;
    private String time;
    private Location location;



    public TaskImpl(String title, String description, Image image, Voice voice, String date,
                    String time, Location location) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.voice = voice;
        this.date = date;
        this.time = time;
        this.location = location;
    }
}
