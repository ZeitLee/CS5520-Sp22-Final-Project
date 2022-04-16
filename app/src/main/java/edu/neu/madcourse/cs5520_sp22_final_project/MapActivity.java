package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;

import edu.neu.madcourse.cs5520_sp22_final_project.Location.Loc;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private double[] loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        loc = (double[]) getIntent().getSerializableExtra("loc");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng currLoc = new LatLng(loc[1], loc[0]);
        googleMap.addMarker(new MarkerOptions()
                .position(currLoc)
                .title("Current Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
    }
}