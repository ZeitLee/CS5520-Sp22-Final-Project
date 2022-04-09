package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import edu.neu.madcourse.cs5520_sp22_final_project.Alarm.Alarm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickListener(View v) {
        new Alarm(this).fireAlarm();
    }
}