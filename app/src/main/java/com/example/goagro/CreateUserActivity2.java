package com.example.goagro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CreateUserActivity2 extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText phone;
    EditText licence;
    EditText pass1;
    EditText pass2;
    Button newUser;
    ToggleButton seePass1;
    ToggleButton seePass2;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user2);
        name = findViewById(R.id.Name);
        email = findViewById(R.id.emailAddress);
        phone = findViewById(R.id.phoneNumber);
        licence = findViewById(R.id.liscenceNumber);
        pass1 = findViewById(R.id.password1);
        pass2 = findViewById(R.id.password2);
        newUser = findViewById(R.id.signUpButton);
        seePass1 = findViewById(R.id.toggleButton);
        seePass2 = findViewById(R.id.toggleButton2);
        auth = FirebaseAuth.getInstance();

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_phone = phone.getText().toString();
                String txt_licence = licence.getText().toString();
                String txt_pass1 = pass1.getText().toString();
                String txt_pass2 = pass2.getText().toString();

                if (!txt_pass1.equals(txt_pass2)){
                    Toast.makeText(CreateUserActivity2.this, "Make sure passwords in both the fields are matching to each other", Toast.LENGTH_SHORT).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
                    Toast.makeText(CreateUserActivity2.this, "Enter email address in valid format", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass1) || TextUtils.isEmpty(txt_name)){
                    Toast.makeText(CreateUserActivity2.this, "Don't leave any of the field empty", Toast.LENGTH_SHORT).show();
                }else{
                    createUser(txt_name, txt_email, txt_phone, txt_licence, txt_pass1);
                }

            }
        });

        seePass1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    pass1.setTransformationMethod(null);
                }
                else{
                    pass1.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        seePass2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    pass2.setTransformationMethod(null);
                }
                else{
                    pass2.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });


    }
    public void createUser(String name, String mail, String phone, String licence, String pass){
        auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (auth.fetchSignInMethodsForEmail(user.getEmail()) != null){
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(CreateUserActivity2.this, "Email message for verification sent successfully verify the email and then you can log into the account", Toast.LENGTH_LONG).show();
//                        storeData(name, mail, phone, licence);
                            sendDataToMainActivity(name, mail, phone, licence);
//                        startActivity(new Intent(CreateUserActivity2.this, MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateUserActivity2.this, "Some error occurred while verification email sending", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(CreateUserActivity2.this, "Email already registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateUserActivity2.this, MainActivity.class));
                }

            }
        });
    }

    public void sendDataToMainActivity(String name, String email, String phone, String licence){
        Intent intent = new Intent(CreateUserActivity2.this, MainActivity.class);
        intent.putExtra("C_name", name);
        intent.putExtra("C_email", email);
        intent.putExtra("C_phone", phone);
        intent.putExtra("C_licence", licence);

        startActivity(intent);

    }

    public void storeData(String name, String mail, String phone, String licence){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();

        String C_mail = mail;
        String C_name = name;
        String C_phone = phone;
        String C_licence = licence;

        map.put("Mail",mail);
        map.put("Name",name);
        map.put("Phone", phone);
        map.put("licence", licence);

        db.collection(uid).document(name).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CreateUserActivity2.this, "Information added into database", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CreateUserActivity2.this, "Some error while adding information into database", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}