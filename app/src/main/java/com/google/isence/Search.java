package com.google.isence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Search extends AppCompatActivity {

    private EditText customReg;
    private FirebaseFirestore db;
    public String TAG="SRA";
    private LinearLayout LL;
    private String reg;
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
        setContentView(R.layout.activity_search);

        nic = findViewById(R.id.nic);
        na = findViewById(R.id.name);
        carname = findViewById(R.id.surname);
        address = findViewById(R.id.address);
        color = findViewById(R.id.color);
        model = findViewById(R.id.model);
        type = findViewById(R.id.type);
        year = findViewById(R.id.year);
        feee = findViewById(R.id.fee);
        from = findViewById(R.id.from);

        customReg=findViewById(R.id.search);
        db = FirebaseFirestore.getInstance();

    }


    public void Search(View view){

        reg=customReg.getText().toString();
        reg.toUpperCase();
        Log.d(TAG, "reg: " + reg);
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
                        carname.setText("Registration No : "+reg);
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

}