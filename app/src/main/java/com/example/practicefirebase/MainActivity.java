package com.example.practicefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    EditText id,pass;
    TextView ForgotPassword;
    GoogleSignInClient googleSignInClient ;
    SignInButton signInButton;
    Button forgotPassword;
    DatabaseReference rootReference;
    FirebaseUser user;

    public void startRegisterActivity(View view)
    {
        Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(i);
    }
    public void login(View view)
    {
        if(id.getText().toString().equals("") || pass.getText().toString().equals(""))
        {
            Toast.makeText(this, "email or password can't be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //System.out.println(id.getText().toString()+pass.getText().toString());
            auth.signInWithEmailAndPassword(id.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    user = auth.getCurrentUser();
                    if(task.isSuccessful() && user.isEmailVerified())
                    {
                        //Log.i("yes logged in","user");
                        Toast.makeText(MainActivity.this, "Welcome " ,Toast.LENGTH_SHORT).show();
                        finish();
                        /*String id = user.getEmail();
                        String password = "1234";
                        rootReference= FirebaseDatabase.getInstance().getReference();
                        User obj=new User(id,password);


                        rootReference.child(user.getUid()).setValue("ankit").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this, "Stored in db successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/

                        Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                        startActivity(i);

                        //updateUI()
                    }
                    else if(!task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                    }
                    else if(task.isSuccessful() && !user.isEmailVerified())
                    {
                        Toast.makeText(MainActivity.this, "Please Verify your Email first", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public void startRegister(View view)
    {
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        id = (EditText)findViewById(R.id.LoginNameInput);
        pass = (EditText)findViewById(R.id.LoginPasswordInput);
        ForgotPassword = (TextView) findViewById(R.id.ForgetPassword);

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this , gso);
        signInButton = (SignInButton)findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 9001);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                account.getEmail();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Error occured Try Again", Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //  Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = auth.getCurrentUser();
                            Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                            Toast.makeText(MainActivity.this, "logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(i);


                            //updateUI(user);
                        } else {
                            Toast.makeText(MainActivity.this, "Error occured try again", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
