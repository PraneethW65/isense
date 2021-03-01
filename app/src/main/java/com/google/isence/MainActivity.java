package com.google.isence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String TAG="iii";
    private String email;
    private String password;
    private EditText emailText;
    private EditText pwText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.email);
        pwText = findViewById(R.id.pw);
    }

    public void signIn(View view) {

        email=emailText.getText().toString();
        password=pwText.getText().toString();

        if(email.isEmpty()){
            emailText.setError("Please Enter an email");

        }else if(password.isEmpty()){
            pwText.setError("Please Enter your password");

        }else if(password.length() < 7){

            pwText.setError("Please Enter password minimum of 8 Characters");

        }else{

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                        }
                    });
        }


    }

    private void updateUI(FirebaseUser user) {
        if(user == null){
            //code when there is no user
        }else {
            goScannerPage();
        }
    }

    public void goScannerPage(){

        Intent intent=new Intent(this, Master.class);
        startActivity(intent);
        finish();
    }

    public void goIOTemulator(View view){
        Intent intent=new Intent(this, Emulate.class);
        startActivity(intent);
        finish();
    }


}