package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import edu.neu.madcourse.cs5520_sp22_final_project.Location.Loc;


public class LocationActivity extends AppCompatActivity {
    private Loc loc;
    private String address;
    TextView view;
    ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    System.out.println("+++++++++");
                    System.out.println("get back");
                    System.out.println(result.getResultCode());
                    if (result.getResultCode() == 1) {
                        assert result.getData() != null;
                        address = result.getData().getStringExtra("address");
                        view.setText(address);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        loc = new Loc(this);
        view = findViewById(R.id.textView);
        loc.setViewLocation(view);
        address = "";
    }

    public void clickListener(View v) {
//        new Alarm(this).fireAlarm(loc.getLocation());


        Intent intent = new Intent(this, MapActivity.class);
        address = loc.getAddress();
        intent.putExtra("loc", (Serializable) loc.getGeoLoc());
        intent.putExtra("address", address);
        intentActivityResultLauncher.launch(intent);
    }
}