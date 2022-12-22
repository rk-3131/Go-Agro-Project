package com.example.goagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateUserActivity2 extends AppCompatActivity {
    EditText email;
    EditText phone;
    EditText licence;
    EditText pass1;
    EditText pass2;
    Button newUser;

    FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user2);
        email = findViewById(R.id.emailAddress);
        phone = findViewById(R.id.phoneNumber);
        licence = findViewById(R.id.liscenceNumber);
        pass1 = findViewById(R.id.password1);
        pass2 = findViewById(R.id.password2);
        newUser = findViewById(R.id.signUpButton);
        auth = FirebaseAuth.getInstance();
        


        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_phone = phone.getText().toString();
                String txt_licence = licence.getText().toString();
                String txt_pass1 = pass1.getText().toString();
                String txt_pass2 = pass2.getText().toString();

//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                
                if (txt_pass1.equals(txt_pass2) && txt_pass1.length() >= 6){
                   createUser(txt_email, txt_pass1);
                }else{
                    Toast.makeText(CreateUserActivity2.this, "Make sure passwords in both the fields are same and again lenght of password is larger than 6", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public void createUser(String mail, String pass){
        auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CreateUserActivity2.this, "Email message for verification sent successfully verify the email and then you can log into the account", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CreateUserActivity2.this, MainActivity.class));
//                        finish();
                    }
                });
                user.sendEmailVerification().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateUserActivity2.this, "Some error occurred while verification email sending", Toast.LENGTH_SHORT).show();
                    }
                });
                if (task.isComplete()){
                    Toast.makeText(CreateUserActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CreateUserActivity2.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}