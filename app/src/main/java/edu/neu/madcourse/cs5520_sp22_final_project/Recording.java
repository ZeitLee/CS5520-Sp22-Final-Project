package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Executable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Recording extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 101;
    private MediaRecorder mediaRecorder;
    private MediaPlayer meidaPlayer;
    private Button startRecord;
    private Button stopRecord;
    private TextView timer
    private boolean isRecording;
    private boolean isPlaying;

    private int second;
    private String path;

    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        initialElements();
    }

    /**
     * Initial all elements in the screen.
     */
    private void initialElements() {
        startRecord = (Button) findViewById(R.id.start);
        stopRecord = (Button) findViewById(R.id.stop);
        timer = (TextView) findViewById(R.id.timer);
        isRecording = false;
        isPlaying = false;
        executorService = Executors.newSingleThreadExecutor();

        meidaPlayer = new MediaPlayer();
    }

    // set start button click listener.
    private void setStartButton() {
        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRecordingPermission()) {
                    // check recording state.

                } else {
                    // request permission.
                    requestRecordingPermissions();
                }
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
}