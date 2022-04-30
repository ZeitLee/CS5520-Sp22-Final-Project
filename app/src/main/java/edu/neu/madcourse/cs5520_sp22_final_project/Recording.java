package edu.neu.madcourse.cs5520_sp22_final_project;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Recording extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 101;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Button startRecord;
    private Button play;
    private TextView timer;
    private boolean isRecording;
    private boolean isPlaying;

    private int seconds;
    private String path;

    int dummySeconds;
    Handler handler;
    int playableSeconds;

    ExecutorService executorService;

    private Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        initialElements();
        seconds = 0;
        dummySeconds = 0;
    }

    /**
     * Initial all elements in the screen.
     */
    private void initialElements() {
        startRecord = (Button) findViewById(R.id.start);
        play = (Button) findViewById(R.id.play);
        timer = (TextView) findViewById(R.id.timer);
        isRecording = false;
        isPlaying = false;
        executorService = Executors.newSingleThreadExecutor();

        mediaPlayer = new MediaPlayer();

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("localRecordingFile")) {
            path = extras.getString("localRecordingFile");
        }

        System.out.println("Current path: " + path);

        setStartButton();
        setPlayerButton();

        finish = (Button) findViewById(R.id.finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //backToTask();
            }
        });
    }

    private void backToTask() {
        Intent i = new Intent(this, createReminder.class);
        if (path != null) {
            i.putExtra("recordingFile", path);
        }
        // save location info.
        i.putExtra("loc", getIntent().
                getDoubleArrayExtra("currentLocation"));

        startActivity(i);
    }

    // set start button click listener.
    private void setStartButton() {
        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRecordingPermission()) {
                    recordAction();
                } else {
                    // request permission.
                    requestRecordingPermissions();
                }
            }
        });
    }

    // set player start.
    private void setPlayerButton() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(path);
                if (!isPlaying) {
                    if (path != null) {
                        try {
                            mediaPlayer.setDataSource(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Recording Present.",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        mediaPlayer.prepare();
                        playableSeconds = (Integer.valueOf(mediaPlayer.getDuration()) / 1000) + 1;
                        System.out.println(playableSeconds);
                        dummySeconds = playableSeconds;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    isPlaying = true;
                    runTimer();
                } else {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaPlayer = new MediaPlayer();
                    isPlaying = false;
                    seconds = 0;
                    handler.removeCallbacksAndMessages(null);
                }
            }
        });
    }

    private void runTimer() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int sec = seconds % 60;
                String time = String.format(Locale.getDefault(),"%02d:%02d", minutes, sec);
                timer.setText(time);

                if (isRecording || (isPlaying && playableSeconds != -1)) {
                    seconds++;
                    playableSeconds--;

                    if (isPlaying && playableSeconds == -1) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        mediaPlayer = new MediaPlayer();
                        playableSeconds = dummySeconds;
                        seconds = 0;
                        handler.removeCallbacksAndMessages(null);
                        return;
                    }
                }

                handler.postDelayed(this, 1000);
            }
        });
    }


    private boolean checkRecordingPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            requestRecordingPermissions();
            return false;
        } else {
            return true;
        }
    }

    private void recordAction() {
        // check recording state.
        if (!isRecording) {
            isRecording = true;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setOutputFile(getRecordingFilePath());
                    path = getRecordingFilePath();
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaRecorder.start();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seconds = 0;
                            dummySeconds = 0;
                            runTimer();
                        }
                    });
                }
            });
        } else {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            playableSeconds = seconds;
            dummySeconds = seconds;
            seconds = 0;
            isRecording = false;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.removeCallbacksAndMessages(null);
                }
            });
        }
    }

    private void requestRecordingPermissions() {
        String[] permission = new String[]{Manifest.permission.RECORD_AUDIO};
        ActivityCompat.requestPermissions(Recording.this, permission,
                REQUEST_AUDIO_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean permissionToRecording = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (permissionToRecording) {
                    Toast.makeText(getApplicationContext(), "Permission Given.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getRecordingFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, "recording" + getIntent().getExtras().getString("id")
                + ".mp3");
        path = file.getPath();
        return path;
    }



    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, createReminder.class);
        if (path != null) {
            i.putExtra("recordingFile", path);
        }
        System.out.println("save path: " + path);
        // save location info.
        //i.putExtra("loc", getIntent().getDoubleArrayExtra("currentLocation"));
        // give back this reminder id.
        i.putExtra("id", getIntent().getExtras().getString("id"));
        startActivity(i);
    }

}