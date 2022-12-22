package com.example.goagro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity2 extends AppCompatActivity {
    Button logOutButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro2);
        logOutButton = findViewById(R.id.logOut);
        auth = FirebaseAuth.getInstance();



        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(IntroActivity2.this, MainActivity.class));
                Toast.makeText(IntroActivity2.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
//                finish();
            }
        });
    }
}