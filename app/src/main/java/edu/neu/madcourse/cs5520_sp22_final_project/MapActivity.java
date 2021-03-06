package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        loc = getIntent().getDoubleArrayExtra("loc");
        Loc.geoToAddress(loc[0], loc[1], MapActivity.this);
        System.out.println("+++++++++++++++++");
        System.out.println("Map address");
        System.out.println(address);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        System.out.println("+++++++++++++++");
        System.out.println("back");
        Intent returnIntent = new Intent();
        returnIntent.putExtra("address", loc);
        setResult(1, returnIntent);
        super.onBackPressed();
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng currLoc = new LatLng(loc[0], loc[1]);
        googleMap.addMarker(new MarkerOptions()
                .position(currLoc)
                .title(address));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLoc,8));;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions option = new MarkerOptions();
                address = Loc.geoToAddress(latLng.latitude, latLng.longitude, MapActivity.this);
                loc[0] = latLng.latitude;
                loc[1] = latLng.longitude;
                option.position(latLng)
                        .title(address);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));
                // Add marker on map
                googleMap.addMarker(option);
            }
        });
    }
}