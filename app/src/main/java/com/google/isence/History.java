package com.google.isence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History extends AppCompatActivity {

    private String nic;
    public String TAG="SRA";
    private LinearLayout LL;
    private FirebaseFirestore db;
    public EditText SearchTxt;
    private FirebaseAuth mAuth;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LL=(LinearLayout) this.findViewById(R.id.ll);
        List<String> vehicles = new ArrayList<>();
        SearchTxt = findViewById(R.id.reg);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        email=user.getEmail();

        db = FirebaseFirestore.getInstance();
        db.collection("Report")
                .whereEqualTo("Police", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                TextView tv = new TextView(History.this);
                                lparams.gravity = Gravity.CENTER;
                                lparams.setMargins(10, 40, 10, 10);
                                //tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vehicle, 0, 0, 0);
                                tv.setTextSize(15);
                                tv.setLayoutParams(lparams);
                                tv.setBackgroundColor(Color.GRAY);
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Timestamp timestamp=(Timestamp)document.get("Date");
                                Date date = timestamp.toDate();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String strDate = formatter.format(date);
                                tv.setText(" <-| "+strDate+ " | "+document.get("Vehicle").toString()+" | "+document.get("NIC").toString()+" |-> ");
                                LL.addView(tv);

                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}