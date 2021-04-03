package com.google.isence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Profile extends AppCompatActivity {

    private FirebaseFirestore db;
    public String TAG="SRA";
    private String pName;
    private String pRank;
    private String pId;
    private FirebaseAuth mAuth;
    private String email;
    private TextView idText;
    private TextView nameText;
    private TextView rankText;
    private TextView emailText;
    private TextView phoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        email=user.getEmail();

        idText = (TextView)findViewById(R.id.id);
        nameText = (TextView)findViewById(R.id.name);
        rankText = (TextView)findViewById(R.id.rank);
        emailText = (TextView)findViewById(R.id.email);
        phoneText = (TextView)findViewById(R.id.address);
        Log.d(TAG, "EMAIL : "+email);
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Police").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        pName=document.get("Name").toString();
                        pRank=document.get("Rank").toString();
                        pId=document.get("ID").toString();
                        String Phone = document.get("Mobile").toString();

                        idText.setText("ID :"+pId);
                        nameText.setText("Name : "+pName);
                        rankText.setText("Rank : "+pRank);
                        emailText.setText("Email : "+email);
                        phoneText.setText("Phone No : "+Phone);

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