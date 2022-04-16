package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.Camera;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
//import android.graphics.Camera;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    // Go back to the previous screen
    public void backtoMain(View V){
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

        // on click listener for adding images to the description
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(); //take pictures
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


    // Some local variables needed for saving pictures to storage
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath; //can be retrieved later
    /*
    Default path for the images:
    Android/data/edu.neu.madcourse.cs5520_sp22_final_project/files/Pictures
     */
    Toast toast;

    // Create temporary images files in the designated path
    // this temp image will be replaced by the image taken by the user
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,   //prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // This method opens up the camera app, and saves the images to storage
    Uri photoURI;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile =  null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                toast = Toast.makeText(getApplicationContext(), "failed to create image file", Toast.LENGTH_LONG);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Helper method that shows a preview of the image in the "Description" box
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap scaledImage = Bitmap.createScaledBitmap(imageBitmap, 400, 400, false);
            //addPhoto.setImageBitmap(imageBitmap);
            Drawable d = new BitmapDrawable(getResources(), scaledImage);
            description.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

            toast = Toast.makeText(getApplicationContext(), "Image saved successfully!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }











}













