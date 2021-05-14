package com.google.isence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Vehicle extends AppCompatActivity {

    private String reg;
    private FirebaseFirestore db;
    public String TAG="SRA";
    public TextView li;
    public TextView nic;
    public TextView na;
    public TextView carname;
    public TextView regis;
    public TextView address;
    public TextView color;
    public TextView model;
    public TextView type;
    public TextView year;
    public TextView feee;
    public TextView from;
    public String NIC;
    private FirebaseAuth mAuth;
    private String email;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        nic = findViewById(R.id.nic);
        na = findViewById(R.id.name);
        carname = findViewById(R.id.textView6);
        address = findViewById(R.id.address);
        regis = findViewById(R.id.surname);
        color = findViewById(R.id.color);
        model = findViewById(R.id.model);
        type = findViewById(R.id.type);
        year = findViewById(R.id.year);
        feee = findViewById(R.id.fee);
        from = findViewById(R.id.from);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        email=user.getEmail();

        reg = getIntent().getStringExtra("Vehicle");
        regis.setText("Registration No : "+reg);
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Register").document(reg);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        nic.setText("NIC : "+document.get("NIC").toString());
                        color.setText("COLOR : "+document.get("Color").toString());
                        carname.setText(document.get("Model").toString());
                        model.setText("MODEL : "+document.get("Make").toString());
                        type.setText("TYPE : "+document.get("Type").toString());
                        year.setText("YEAR : "+document.get("Year").toString());
                        NIC=document.get("NIC").toString();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docRef2 = db.collection("license").document(reg);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        na.setText("NAME : "+document.get("oName").toString());
                        address.setText("ADDRESS : "+document.get("address").toString());
                        feee.setText("ANNUAL FEE : "+document.get("AnnualFee").toString());
                        Timestamp timestamp=(Timestamp)document.get("From");
                        Date date = timestamp.toDate();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = formatter.format(date);
                        from.setText("VALID FROM : "+ strDate);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    public void report(View view){

        SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
        longitude=sharePref.getFloat("Longitude",80);
        latitude=sharePref.getFloat("Latitude",7);

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Click OK to confirm report");
        dlgAlert.setTitle("Report violator");

        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Map<String, Object> user = new HashMap<>();
                        user.put("NIC", NIC);
                        user.put("Vehicle", reg);
                        user.put("Date", new Timestamp(new Date()));
                        user.put("Police", email);
                        user.put("Paid", false);
                        user.put("longitude", longitude);
                        user.put("latitude", latitude);

                        // Add a new document with a generated ID
                        db.collection("Report")
                                .add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        Toast.makeText(Vehicle.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(Vehicle.this, Dash.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                        Toast.makeText(Vehicle.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                });

        dlgAlert.setCancelable(true);
        dlgAlert.create().show();



    }
}