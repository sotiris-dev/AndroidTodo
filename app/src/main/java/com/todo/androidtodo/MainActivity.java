package com.todo.androidtodo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        String UserId = auth.getCurrentUser().getUid();
        final DatabaseReference dbref = fdb.getReference("TASKS/"+UserId);
        ListView mainapplistview = (ListView)this.findViewById(R.id.listview);


        //firebase list adapter
        FirebaseListAdapter<Task> firebaseadapter = new FirebaseListAdapter<Task>(
                this,
                Task.class,
                R.layout.row,
                dbref
        ) {
            @Override
            protected void populateView(View v, Task model, int position) {
                TextView date = (TextView)v.findViewById(R.id.date);
                date.setText(model.getDate());
                TextView posttext = (TextView)v.findViewById(R.id.postText);
                posttext.setText(model.getText());
            }

        };
        mainapplistview.setAdapter(firebaseadapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Add new post");
                dialog.setContentView(R.layout.dialog_addnewpost);
                dialog.show();

                final Button submittask = (Button)dialog.findViewById(R.id.submitnewtask);
                final EditText newpost = (EditText)dialog.findViewById(R.id.postText);

                submittask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Task a = new Task(newpost.getText().toString().trim());
                        dbref.push().setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.cancel();
                            }
                        });
                    }
                });
            }



        });


    }

}
