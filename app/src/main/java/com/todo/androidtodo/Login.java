package com.todo.androidtodo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    FirebaseDatabase fdb;
    DatabaseReference fref;
    FirebaseAuth auth;
    Button btnlogin;
    Button btnregister;

    @Override
    public void onBackPressed() {
        //disable going back to lonin activity
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        btnlogin = (Button) findViewById(R.id.Button_login);
        btnregister = (Button) findViewById(R.id.Button_Register);


        //onclick authenticate with firebase
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.Email_login);
                EditText pass = (EditText) findViewById(R.id.Password_login);

                final ProgressDialog dialog = new ProgressDialog(Login.this);
                dialog.setTitle("Login");
                dialog.setMessage("Loading...Plz wait....");
                dialog.setCancelable(false);



                if(email.getText().toString().trim().length() > 5 && pass.getText().toString().trim().length() > 5) {
                    dialog.show();
                    auth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.cancel();
                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                            //if authentication is success display the main app
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    dialog.cancel();
                               Intent aint = new Intent(Login.this, MainActivity.class);
                               startActivity(aint);
                                }
                            });
                }else {
                    Toast.makeText(Login.this, "Wrong inputs", Toast.LENGTH_LONG).show();
                }
            }
        });


        //go to register activity
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  aint = new Intent(Login.this, Register.class);
                startActivity(aint);
            }
        });
    }
}
