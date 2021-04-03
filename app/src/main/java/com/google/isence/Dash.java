package com.google.isence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Dash extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        mAuth = FirebaseAuth.getInstance();
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

}