package com.example.goagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IntroActivity2 extends AppCompatActivity {
    Button logOutButton;
    Button weather;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);
        logOutButton = findViewById(R.id.logOut);
        weather = findViewById(R.id.getData);



        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(IntroActivity2.this, MainActivity.class));
                Toast.makeText(IntroActivity2.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity2.this, WeatherActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (user != null){
            Toast.makeText(this, "Log Out to go back", Toast.LENGTH_SHORT).show();
        }else{
            super.onBackPressed();
        }
    }
}