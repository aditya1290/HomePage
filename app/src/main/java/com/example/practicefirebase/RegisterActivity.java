package com.example.practicefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText email,password;
    FirebaseAuth auth;
    FirebaseUser user;

    public void createUser(View view)
    {
        String id = "adityag.cs.17@nitj.ac.in";
        String pass = password.getText().toString();

        System.out.println(id + pass);
        if( pass.equals(""))
        {
            Toast.makeText(this, "Email or password can't be empty", Toast.LENGTH_SHORT).show();
        }
        else {


            auth.createUserWithEmailAndPassword(id, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Please Verify Your email address", Toast.LENGTH_SHORT).show();
                        user = auth.getCurrentUser();
                        if(user!=null) {


                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                            finish();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText)findViewById(R.id.RegisterNameInput);
        password = (EditText)findViewById(R.id.RegisterPasswordInput);
        auth = FirebaseAuth.getInstance();
    }
}
