package com.todo.androidtodo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText name      = (EditText)findViewById(R.id.name);
        final EditText lastname  = (EditText)findViewById(R.id.lastname);
        final EditText birthdate = (EditText)findViewById(R.id.birthdate);
        birthdate.setInputType(InputType.TYPE_NULL);
        final EditText email     = (EditText)findViewById(R.id.email);
        final EditText username  = (EditText)findViewById(R.id.username);
        final EditText password  = (EditText)findViewById(R.id.password);
        Button btnregister = (Button)findViewById(R.id.btnregister);
        final FirebaseAuth ayth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference dbref = db.getReference();

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog d = new DatePickerDialog(
                        Register.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        11,5,6

                );

                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker datePicker, int i , int i1, int i2){
                int year = i;
                int day = i2;
                int month = i1;
                month++;


                birthdate.setText(day+"/"+month+"/"+year);
            }
        };



        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String regname       = name.getText().toString().trim();
                final String reglastname   = lastname.getText().toString().trim();
                final String regbirthdate  = birthdate.getText().toString().trim();
                final String regemail      = email.getText().toString().trim();
                final String reguname      = username.getText().toString().trim();
                final String regpass       = password.getText().toString().trim();
                final User newuser         = new User(regname,reglastname,regbirthdate,regemail,reguname);
                if(newuser.RegValidator())
                {
                    ayth.createUserWithEmailAndPassword(regemail,regpass).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String uid  = ayth.getCurrentUser().getUid();

                            //save info to database after success register!!
                            dbref.child("USERS").child(uid).setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent aint = new Intent(Register.this, MainActivity.class);
                                    startActivity(aint);
                                }
                            });
                        }
                    });

                }else
                {
                    Toast.makeText(Register.this, "Please check your inputs!!!", Toast.LENGTH_LONG).show();
                }

            }
        });






    }


}
