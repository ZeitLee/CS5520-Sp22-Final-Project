package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class createReminder extends AppCompatActivity {

    private EditText nameInput;
    private TextView myTextDisplayDate;
    private ImageView myImageDisplayDate;
    private TextView myTextDisplayTime;
    private ImageView myImageDisplayTime;
    private DatePickerDialog.OnDateSetListener myDateSetListener;
    private TimePickerDialog.OnTimeSetListener myTimeSetListener;
    private ImageView addPhoto;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);

        nameInput = (EditText) findViewById(R.id.editTextTaskName);
        myTextDisplayDate = (TextView) findViewById(R.id.dateSelector);
        myTextDisplayTime = (TextView) findViewById(R.id.timeSelector);
        addPhoto = (ImageView) findViewById(R.id.photoImageView);
        description = (EditText) findViewById(R.id.editTextTextMultiLine);

        initialSetting();

    }

    public void backtoMain(View V){
        //view occupies a rectangular area on the screen and
        // is responsible for drawing and event handling
        //Toast.makeText(getApplicationContext(), "wiggle wiggle", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * A helper method to set all functions in this activity. (Now set time and date clickable to
     * set time and date).
     *
     */
    private void initialSetting() {
        // set click listener to date text and image to open date setting dialog.
        myTextDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateSelecotr();
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                //onActivityResult();
            }
        });

        // set date pick listener.
        myDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int showMonth = month + 1; // since the month is 0-based data.
                String date = String.format("%d/%d/%d", showMonth, day, year);
                myTextDisplayDate.setText(date);
            }
        };

        // set click listener to time text and image to open time setting dialog.
        myTextDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeSelector();
            }
        });

        // set time pick listener.
        myTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = String.format("%d:%d", hour, minute);
                myTextDisplayTime.setText(time);
            }
        };
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

    // This is a helper method to show time selector screen.
    private void showTimeSelector() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(createReminder.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, myTimeSetListener,
                hour, minute, true);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }




    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap scaledImage = Bitmap.createScaledBitmap(imageBitmap,400,400, false);
            //addPhoto.setImageBitmap(imageBitmap);
            Drawable d = new BitmapDrawable(getResources(), scaledImage);
            description.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
        }
    }




}