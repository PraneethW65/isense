package com.google.isence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Dash extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int PERMISSIONS_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        mAuth = FirebaseAuth.getInstance();
        startTrackerService();
    }

    public void goMap(View view){
        Intent intent=new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void goScanner(View view){
        Intent intent=new Intent(this, BarcodeScanner.class);
        startActivity(intent);
    }

    public void goSearch(View view){
        Intent intent=new Intent(this, Search.class);
        startActivity(intent);
    }

    public void goHistory(View view){
        Intent intent=new Intent(this, History.class);
        startActivity(intent);
    }

    public void goMyProfile(View view){
        Intent intent=new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void goEmulate(View view){
        Intent intent=new Intent(this, Emulate.class);
        startActivity(intent);
    }

    public void logOut(View view){
        mAuth.signOut();
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTrackerService() {
        stopService(new Intent(this, LocationServiceNotifi.class));
        startService(new Intent(this, LocationServiceNotifi.class));
        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();
    }

    public void stopTrackerService() {
        stopService(new Intent(this, LocationServiceNotifi.class));
        Toast.makeText(this, "GPS tracking disabled", Toast.LENGTH_SHORT).show();
    }

    public void shareLocation(){

        //Check whether GPS tracking is enabled//
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }

        //Check whether this app has access to the location permission//
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        //If the location permission has been granted, then start the TrackerService//
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            //If the app doesn’t currently have access to the user’s location, then request access//
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

}