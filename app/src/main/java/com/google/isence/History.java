package com.google.isence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class History extends AppCompatActivity {

    private String nic;
    public String TAG="SRA";
    private LinearLayout LL;
    private FirebaseFirestore db;
    public EditText SearchTxt;
    private FirebaseAuth mAuth;
    private String email;
    public String add;


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

                                tv.setText(" You Report "+document.get("Vehicle").toString()+ " at  "+strDate+" from "+getAddress(document.getDouble("latitude"),document.getDouble("longitude"))+".");
                                LL.addView(tv);

                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(History.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses.isEmpty()){
                add="Location Unknown(Longitute"+lng+",Latitude"+lat+")";
            }else {
                Address obj = addresses.get(0);
                add = obj.getAddressLine(0);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return add;
    }



}