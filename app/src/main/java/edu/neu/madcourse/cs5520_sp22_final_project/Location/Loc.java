package edu.neu.madcourse.cs5520_sp22_final_project.Location;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import javax.security.auth.callback.Callback;

public class Loc {
    private final FusedLocationProviderClient fusedLoc;
    private double lo;
    private double la;
    private String address = "";
    private Activity activity;

    public Loc(Activity activity) {
        this.activity = activity;
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
        mLocationRequest.setInterval(15000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);

        fusedLoc.requestLocationUpdates(mLocationRequest, callback(), null);

    }

    public String getLocation() {
        return address;
    }

    public void setViewLocation(TextView view) {
        getCurr(view);
    }

    private LocationCallback callback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                getCurr(null);
            }
        };
    }

    @SuppressLint("MissingPermission")
    private void getCurr(TextView view) {
        CancellationTokenSource tokenSource = new CancellationTokenSource();
        fusedLoc.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, tokenSource.getToken())
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        System.out.println("+++++++++++++++++");
                        System.out.println("curr");
                        System.out.println(location.getLatitude());
                        System.out.println(location.getLongitude());
                        la = location.getLatitude();
                        lo = location.getLongitude();
                        System.out.println("+++++++++++++++++");

                        Geocoder geocoder = new Geocoder(activity);
                        try {
                            List<Address> addresses = geocoder.getFromLocation(la, lo, 1);
                            System.out.println(addresses.get(0));
                            address = addresses.size() > 0 ? addresses.get(0).getAddressLine(0)
                                    : "Invalid";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (view != null) {
                            view.setText(address);
                        }
                    }
                });
    }
}
