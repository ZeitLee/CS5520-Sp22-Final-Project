package edu.neu.madcourse.cs5520_sp22_final_project.Location;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;

public class Location {
    private final FusedLocationProviderClient fusedLoc;
    private static final int accuracy = 104; //city level accuracy 10km, 100 - high accuracy

    public Location(Activity activity) {
        fusedLoc = LocationServices.getFusedLocationProviderClient(activity);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        CancellationTokenSource tokenSource = new CancellationTokenSource();

        fusedLoc.getLastLocation().addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                System.out.println("+++++++++++++++++");
                System.out.println(location);
                System.out.println("+++++++++++++++++");
            }
        });
    }

    public void getLocation() {

    }
}
