package edu.neu.madcourse.cs5520_sp22_final_project;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.droidsonroids.gif.GifImageView;

public class Recording extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 101;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private ImageView startRecord;
    private ImageView play;
    private TextView timer;
    private boolean isRecording;
    private boolean isPlaying;

    private GifImageView gif;
    private ImageView micIdle;

    private int seconds;
    private String path;

    int dummySeconds;
    Handler handler;
    int playableSeconds;

    //button animation.
    Animation scaleUp, scaleDown;

    ExecutorService executorService;

    private Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        initialElements();
        seconds = 0;
        dummySeconds = 0;

        setImageViewAnimaiton(startRecord);
        setImageViewAnimaiton(play);
    }

    /**
     * Initial all elements in the screen.
     */
    private void initialElements() {
        startRecord = (ImageView) findViewById(R.id.recording_button);
        play = (ImageView) findViewById(R.id.play_icon);
        timer = (TextView) findViewById(R.id.timer);
        isRecording = false;
        isPlaying = false;
        executorService = Executors.newSingleThreadExecutor();
        gif = (GifImageView) findViewById(R.id.gifImageView);
        gif.setVisibility(View.INVISIBLE);
        micIdle = (ImageView) findViewById(R.id.mic_idle);

        // set animaiton.
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        mediaPlayer = new MediaPlayer();

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("localRecordingFile")) {
            path = extras.getString("localRecordingFile");
        }

        System.out.println("Current path: " + path);

        setStartButton();
        setPlayerButton();

        //finish = (Button) findViewById(R.id.finish);

//        finish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

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
                        noRecordingFileAlertDialog();
                        return;
                    }
                    try {
                        mediaPlayer.prepare();
                        playableSeconds = (Integer.valueOf(mediaPlayer.getDuration()) / 1000) + 1;
                        dummySeconds = playableSeconds;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    isPlaying = true;
                    // set icon image.
                    play.setImageResource(R.drawable.stop_button);
                    runTimer();
                } else {
                    play.setImageResource(R.drawable.play_button);
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

    private void noRecordingFileAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Recording not found, please try after recording!");
        dialogBuilder.create().show();
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
            // switch animation.
            gif.setVisibility(View.VISIBLE);
            micIdle.setVisibility(View.INVISIBLE);

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
            gif.setVisibility(View.INVISIBLE);
            micIdle.setVisibility(View.VISIBLE);
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

    //Helper to set textView animation.
    private void setImageViewAnimaiton(ImageView img) {
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    img.startAnimation(scaleUp);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    img.startAnimation(scaleDown);
                }
                return false;
            }
        });
    }



    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        if (path != null) {
            returnIntent.putExtra("recordingFile", path);
            setResult(1, returnIntent);
        } else {
            setResult(0, returnIntent);
        }
        super.onBackPressed();
        finish();
    }

}