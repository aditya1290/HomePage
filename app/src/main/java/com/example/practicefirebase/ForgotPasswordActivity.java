package com.example.practicefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {


    EditText email;
    Button forgotPassword;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText)findViewById(R.id.email);
        forgotPassword = (Button)findViewById(R.id.forgotPassword);

        auth = FirebaseAuth.getInstance();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = email.getText().toString();

                auth.sendPasswordResetEmail(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email sent for password reset", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "Email not sent for password reset", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
