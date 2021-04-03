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
    public String reg;
    public String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        customReg=findViewById(R.id.search);
        db = FirebaseFirestore.getInstance();

        LL=(LinearLayout) this.findViewById(R.id.ll2);

    }


    public void Search(View view){

        reg=customReg.getText().toString();
        reg.toUpperCase();
        Log.d(TAG, "reg: " + reg);
        DocumentReference docRef = db.collection("license").document(reg);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView tv = new TextView(Search.this);
                        lparams.gravity = Gravity.CENTER;
                        lparams.setMargins(10, 60, 10, 10);
                        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.car_b, 0, 0, 0);
                        tv.setTextSize(25);
                        tv.setLayoutParams(lparams);
                        tv.setBackgroundColor(Color.GRAY);

                        Timestamp timestamp=(Timestamp)document.get("From");
                        Date date = timestamp.toDate();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        year = formatter.format(date);

                        DocumentReference docRef = db.collection("Register").document(reg);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        tv.setText(document.get("Model").toString() + "  "+year+ "  "+reg);
                                        LL.addView(tv);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });

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