package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
//import android.graphics.Camera;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import edu.neu.madcourse.cs5520_sp22_final_project.models.Reminder;

import edu.neu.madcourse.cs5520_sp22_final_project.Alarm.Alarm;
import edu.neu.madcourse.cs5520_sp22_final_project.Location.Loc;

public class createReminder extends AppCompatActivity {

    private EditText nameInput;
    private EditText mDescription;
    private TextView myTextDisplayDate;
    private TextView myTextDisplayTime;
    private DatePickerDialog.OnDateSetListener myDateSetListener;
    private TimePickerDialog.OnTimeSetListener myTimeSetListener;
    private ImageView addPhoto;
    private ImageView mapSelector;
    private ImageView mic;
    private ImageView photo;
    private ImageView person;
    private Button done;
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mSharedEditor;
    private Gson gson;
    private Reminder reminder;
    private TextView mRingtone;

    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;

    // id.
    private String id;
    // date and time.
    private String dateString;
    private String timeString;
    // recording.

    //location
    private Loc loc;
    private String address;
    private double[] geoLoc;
    private double[] currLoc;
    TextView locationView;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;

    //Alarm
    private Spinner repeat;
    private int existedAlarmNo;

    //alarm id
    private int Alarm_No;

    //hashTag
    private Spinner mHashtag;

    // Contact view.
    View contactView;
    //contact info.
    private String[] contact;

    private EditText contactNameText;
    private EditText contactPhoneText;
    private EditText contactEmailText;

    // Some local variables needed for saving pictures to storage
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath; //can be retrieved later
    String currentRecordingPath;

    MainActivity weakRefMain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);

        reminder = new Reminder();
        nameInput = (EditText) findViewById(R.id.editTextTaskName);
        myTextDisplayDate = (TextView) findViewById(R.id.dateSelector);
        myTextDisplayTime = (TextView) findViewById(R.id.timeSelector);
        addPhoto = (ImageView) findViewById(R.id.photoImageView);
        mDescription = (EditText) findViewById(R.id.description);
        mapSelector = (ImageView) findViewById(R.id.mapSelector);
        done = (Button) findViewById(R.id.saveData);
        mRingtone = (TextView) findViewById(R.id.selectRingtone);
        mic = (ImageView) findViewById(R.id.mic_icon);
        photo = (ImageView) findViewById(R.id.photo);
        person = (ImageView) findViewById(R.id.person);

        weakRefMain = MainActivity.getMyInstanceActivity();


        //Alarm
        repeat = (Spinner) findViewById(R.id.repeat_options);

        //Hash-Tag
        mHashtag = (Spinner) findViewById(R.id.hashTag);

        //location
        loc = new Loc(this);
        locationView = findViewById(R.id.location);

        geoLoc = new double[2];
        currLoc = getIntent().getDoubleArrayExtra("loc");
        address = "";
        intentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        System.out.println("+++++++++");
                        System.out.println("get back");
                        System.out.println(result.getResultCode());
                        if (result.getResultCode() == 1) {
                            assert result.getData() != null;
                            geoLoc = result.getData().getDoubleArrayExtra("address");
                            locationView.setText(Loc.geoToAddress(geoLoc[0], geoLoc[1], createReminder.this));
                        }
                    }
                });
        // Initialization.
        initialSetting();
        initialValue();

        System.out.println("geoloc");
        System.out.println(Arrays.toString(geoLoc));
        System.out.println("curLoc");
        System.out.println(Arrays.toString(currLoc));
        if (geoLoc[0] == 0 && geoLoc[1] == 0) {
            if (currLoc[0] == 0 && currLoc[1] == 0) {
                loc.setViewLocation(locationView);
            } else {
                locationView.setText(Loc.geoToAddress(currLoc[0], currLoc[1], this));
            }
        }
    }

    // Go back to the previous screen
    public void backtoMain(View V){
        System.out.println("back to main");
        String date = ((TextView)findViewById(R.id.dateSelector)).getText().toString();
        String time = ((TextView)findViewById(R.id.timeSelector)).getText().toString();
        if("".equals(date) || "".equals(time)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("time or date cannot be empty")
                    .setCancelable(true)
                    .create()
                    .show();
            return;
        }
        int[] dateSplit = toIntArray(date.split("/"));
        int[] timeSplit = toIntArray(time.split(":"));
        String des = mDescription.getText().toString();

        System.out.println(Arrays.toString(dateSplit));
        System.out.println(Arrays.toString(timeSplit));
        System.out.println(des);

        String repeatOption = repeat.getSelectedItem().toString();

        System.out.println("existed Alarm: " + existedAlarmNo);
        System.out.println("Ring Path: " + ringtonePath);

        Alarm_No = new Alarm(weakRefMain)
                .fireAlarm(des, repeatOption, existedAlarmNo, ringtonePath,
                        dateSplit[2], dateSplit[0], dateSplit[1],
                        timeSplit[0], timeSplit[1]);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //help converting string array to int array
    private int[] toIntArray(String[] strings) {
        int[] result = new int[strings.length];
        for(int i = 0; i < strings.length; i ++) {
           result[i] = Integer.parseInt(strings[i]);
        }
        return result;
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

        // on click listener for selecting ringtone
        mRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { selectRingtoneIntent(); }
        });

        // set date pick listener.
        myDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int showMonth = month + 1; // since the month is 0-based data.
                String dateString = String.format("%d/%d/%d", showMonth, day, year);
                reminder.date = dateString;
                myTextDisplayDate.setText(dateString);
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
                String timeString = String.format("%d:%d", hour, minute);
                reminder.time = timeString;
                myTextDisplayTime.setText(timeString);
            }
        };

        // jump to map screen when click this image.
        mapSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMapSelector();
            }
        });

        // done button action.
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call backtoMain before settingDone
                //do not call backtoMain twice
                backtoMain(view);
                settingDone();
            }
        });

        // person button action.
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog();
            }
        });

        // mic button action.
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecordingActivity();
            }
        });

        // initial local storage.
        mSharedPreference = getSharedPreferences("reminder_info", MODE_PRIVATE);
        mSharedEditor = mSharedPreference.edit();
        gson = new Gson();

        // initial contact dialog.
        initialDiglog();
    }

    /**
     * Initial value if we enter a existing task.
     */
    private void initialValue() {
        Bundle extras = getIntent().getExtras();
        if (!extras.containsKey("id")) {
            id = reminder.id;
        } else {
            id = extras.getString("id");
            String json = mSharedPreference.getString(id, null);
            if (json != null) {
                reminder = gson.fromJson(json, Reminder.class);
                // set text view.
                nameInput.setText(reminder.title);
                mDescription.setText(reminder.description);
                mHashtag.setSelection(reminder.hashtag);
                myTextDisplayDate.setText(reminder.date);
                myTextDisplayTime.setText(reminder.time);
                address = Loc.geoToAddress(reminder.location[0], reminder.location[1], this);
                geoLoc = new double[]{reminder.location[0], reminder.location[1]};
                locationView.setText(address);
                existedAlarmNo = reminder.Alarm_No;
                System.out.println("initialValue Alarm no " + existedAlarmNo);
                repeat.setSelection(reminder.repeat);
                ringtonePath = reminder.soundPath;
                if (ringtonePath != null) {
                    mRingtone.setText(ringtonePath);
                }
                showImage(reminder.image);
                if (reminder.contact != null) {
                    contact = new String[]{reminder.contact[0], reminder.contact[1],
                            reminder.contact[2]};

                    contactNameText.setText(contact[0]);
                    contactPhoneText.setText(contact[1]);
                    contactEmailText.setText(contact[2]);
                }
                currentRecordingPath = reminder.voice;
            }
        }
    }

    private void initialDiglog() {
        dialogBuilder = new AlertDialog.Builder(this);

        contactView = getLayoutInflater().inflate(R.layout.person_information, null);
        contactNameText = (EditText) contactView.findViewById(R.id.personName);
        contactPhoneText = (EditText) contactView.findViewById(R.id.personPhone);
        contactEmailText = (EditText) contactView.findViewById(R.id.personEmail);

        dialogBuilder.setView(contactView);

        dialogBuilder.setPositiveButton("OK" , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                // save contact information.
                contact = saveContactInfo();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog, do nothing.
            }
        });
        dialog = dialogBuilder.create();
    }

    private void showContactDialog() {
        dialog.show();
    }

    // Helper to save contact information.
    private String[] saveContactInfo() {
        String[] res = new String[3];
        res[0] = contactNameText.getText().toString();
        res[1] = contactPhoneText.getText().toString();
        res[2] = contactEmailText.getText().toString();
        return res;
    }

    private void showRecordingActivity() {
        Intent intent = new Intent(this, Recording.class);
        settingDone();
        intent.putExtra("id", reminder.id);
        intent.putExtra("localRecordingFile", currentRecordingPath);
        startActivity(intent);
    }

    // This is a helper method to show map selector screen.
    private void showMapSelector() {
        Intent intent = new Intent(this, MapActivity.class);
        if (geoLoc[0] == 0 && geoLoc[1] == 0) {
            intent.putExtra("loc", loc.getGeoLoc());
        } else {
            intent.putExtra("loc", geoLoc);
        }
        intentActivityResultLauncher.launch(intent);
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

    //This method selects the ringtone
    String ringtonePath;
    /*
    ringtonePath stores the path for user's selected ringtone.
     */
    private void selectRingtoneIntent(){
        Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone for reminder");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        startActivityForResult(intent, 999);
    }


    // Helper method to specify what to do after activityresult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // shows a preview of the image in the "Description" box
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            photo.setImageBitmap(imageBitmap);
//            Bitmap scaledImage = Bitmap.createScaledBitmap(imageBitmap, 400, 400, false);
            //addPhoto.setImageBitmap(imageBitmap);
            //Drawable d = new BitmapDrawable(getResources(), scaledImage);
            //mDescription.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

            toast = Toast.makeText(getApplicationContext(), "Image saved successfully!", Toast.LENGTH_SHORT);
            toast.show();
        }

        // show a preview of the ringtone in the textview
        if (requestCode == 999 && resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            mRingtone.setText(uri.getPath());
            ringtonePath = uri.toString();
//            MediaPlayer.create(this, Uri.parse("content://media/external_primary/audio/media/63?title=Your%20New%20Adventure&canonical=1")).start();
        }
    }

    // show image by path.
    private void showImage(String path) {
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            photo.setImageBitmap(bitmap);
            currentPhotoPath = path;
        }
    }

    /**
     * Helper method to handle done button.
     * convert all data to json and save date to shared preference.
     * user task id as key.
     */
    private void settingDone() {
        String value = buildJson();
        mSharedEditor.putString(id, value);
        mSharedEditor.apply();
    }

    //Helper to get recording path.
    private void getRecordingFilePath() {
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("recordingFile")) {
            currentRecordingPath = extras.getString("recordingFile");
        }
    }

    // Helper method build json string based on current data.
    private String buildJson() {
        String title = nameInput.getText().toString();
        reminder.title = !"".equals(title) ? title : "Task Name";
        reminder.description = mDescription.getText().toString();
        reminder.hashtag = mHashtag.getSelectedItemPosition();
        reminder.location = geoLoc;
        reminder.Alarm_No = Alarm_No;
        reminder.image = currentPhotoPath;
        reminder.repeat = repeat.getSelectedItemPosition();
        reminder.soundPath = ringtonePath;
        reminder.contact = contact;
        getRecordingFilePath();
        reminder.voice = currentRecordingPath;
        return gson.toJson(reminder);
    }

    /**
     * Handle user used back button.
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
//        exitDialog.setMessage("Your changes will not be saved.\n" +
//                "Do you want to back to main menu?");
        exitDialog.setMessage("Auto save changes......");

        exitDialog.setPositiveButton("OK" , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                // save contact information.
                settingDone();
                finish();
            }
        });
//        exitDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog, do nothing.
//                dialog.cancel();
//            }
//        });
        exitDialog.create().show();
    }
}













