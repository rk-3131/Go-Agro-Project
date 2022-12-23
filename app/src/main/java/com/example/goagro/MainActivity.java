package com.example.goagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    TextView newUserReg;
    Button login;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.loginPassword);
        newUserReg = findViewById(R.id.newUser);
        login = findViewById(R.id.button);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        if (user != null && user.isEmailVerified()){
            startActivity(new Intent(MainActivity.this, IntroActivity2.class));
            Toast.makeText(this, "User logged in with previous entries", Toast.LENGTH_SHORT).show();
        }

        newUserReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateUserActivity2.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = email.getText().toString();
                String password_text = password.getText().toString();
                if (TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text)){
                    Toast.makeText(MainActivity.this, "Don't keep the password and email field empty", Toast.LENGTH_SHORT).show();
                }else{
                   loginUser(email_text, password_text);
                }
            }
        });
    }

    public void loginUser(String email, String password){
        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, IntroActivity2.class));
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Verify your email first and then come back", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}