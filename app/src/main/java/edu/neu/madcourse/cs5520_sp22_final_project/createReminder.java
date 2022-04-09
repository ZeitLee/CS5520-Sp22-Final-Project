package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class createReminder extends AppCompatActivity {

    private EditText nameInput;
    private TextView myTextDisplayDate;
    private ImageView myImageDisplayDate;
    private DatePickerDialog.OnDateSetListener myDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);

        nameInput = (EditText) findViewById(R.id.editTextTaskName);
        myTextDisplayDate = (TextView) findViewById(R.id.dateSelector);
        myImageDisplayDate = (ImageView) findViewById(R.id.dataImageView);

        // set click listener to date text and image to open date setting dialog.
        myTextDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateSelecotr();
            }
        });

        myImageDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateSelecotr();
            }
        });

        // set date picker listener.
        myDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int showMonth = month + 1; // since the month is 0-based data.
                String date = String.format("%s/%s/%s", showMonth, day, year);
                myTextDisplayDate.setText(date);
            }
        };
    }

    public void backtoMain(View V){
        //view occupies a rectangular area on the screen and
        // is responsible for drawing and event handling
        //Toast.makeText(getApplicationContext(), "wiggle wiggle", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // This is a helper method to show date selector screen.
    private void showDateSelecotr() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day  = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(createReminder.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, myDateSetListener,
                year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }



}