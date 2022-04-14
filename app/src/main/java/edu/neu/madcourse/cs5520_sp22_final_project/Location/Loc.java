package edu.neu.madcourse.cs5520_sp22_final_project.Location;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;

import javax.security.auth.callback.Callback;

public class Loc {
    private final FusedLocationProviderClient fusedLoc;

    public Loc(Activity activity) {
        fusedLoc = LocationServices.getFusedLocationProviderClient(activity);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);

        fusedLoc.requestLocationUpdates(mLocationRequest, callback(), null);

    }

    public void getLocation() {

    }

    private LocationCallback callback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                getCurr();
            }
        };
    }

    @SuppressLint("MissingPermission")
    private void getCurr() {
        CancellationTokenSource tokenSource = new CancellationTokenSource();
        fusedLoc.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, tokenSource.getToken())
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        System.out.println("+++++++++++++++++");
                        System.out.println("curr");
                        System.out.println(location.getLatitude());
                        System.out.println(location.getLongitude());
                        System.out.println("+++++++++++++++++");
                    }
                });
    }
}
