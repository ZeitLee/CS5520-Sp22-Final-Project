package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newReminder(View V){
        //view occupies a rectangular area on the screen and
        // is responsible for drawing and event handling
        //Toast.makeText(getApplicationContext(), "wiggle wiggle", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, createReminder.class);
        startActivity(intent);
    }


}