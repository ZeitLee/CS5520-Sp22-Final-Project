package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import edu.neu.madcourse.cs5520_sp22_final_project.Alarm.Alarm;
import edu.neu.madcourse.cs5520_sp22_final_project.Location.Loc;

public class MainActivity extends AppCompatActivity {
    private Loc loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loc = new Loc(this);
        TextView view = findViewById(R.id.textView);
        loc.setViewLocation(view);
    }

    public void clickListener(View v) {
        new Alarm(this).fireAlarm(loc.getLocation());
        TextView view = findViewById(R.id.textView);
        loc.setViewLocation(view);
    }
}