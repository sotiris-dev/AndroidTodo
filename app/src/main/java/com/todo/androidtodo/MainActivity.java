package com.todo.androidtodo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.todo.androidtodo.R.id.about;
import static com.todo.androidtodo.R.id.checkBox;
import static com.todo.androidtodo.R.id.date;
import static com.todo.androidtodo.R.id.delete;
import static com.todo.androidtodo.R.id.edit;
import static com.todo.androidtodo.R.id.logout;
import static com.todo.androidtodo.R.id.save;
import static com.todo.androidtodo.R.id.text;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        //disable going back to lonin activity

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about_contacts, menu);
        return true;
    }

    //handles the topbar actions
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case logout:
                //popup a logout loading view
                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("Logout");
                dialog.setMessage("Logout...Plz wait....");
                dialog.setCancelable(false);
                dialog.show();

                //logout and redirect to Login page in 5 sec
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dialog.cancel();
                                FirebaseAuth.getInstance().signOut();
                                Intent aint1 = new Intent(MainActivity.this, Login.class);
                                startActivity(aint1);
                            }
                        },
                        3200
                );

                break;
            case about:
                Intent  aint2 = new Intent(MainActivity.this, About.class);
                startActivity(aint2);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        final String UserId = auth.getCurrentUser().getUid();
        final DatabaseReference dbref = fdb.getReference();
        final ListView listview;



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //display the addtask form
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Task");
                dialog.setContentView(R.layout.dialog_addnewpost);
                dialog.show();

                final Button submittask = (Button)dialog.findViewById(R.id.submitnewtask);
                final EditText newpost = (EditText)dialog.findViewById(R.id.postText);

                submittask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Task a = new Task();
                        a.setText(Date.getDate()+"u"+newpost.getText().toString().trim());
                        final String key = dbref.child("USERS").child(UserId).child("TASKS").push().getKey();
                        a.setId(key);
                        if(a.getText().toString().length() < 17){
                            Toast.makeText(MainActivity.this, "At least 6 chars!!!", Toast.LENGTH_LONG).show();
                        }else{
                            //save task object with a new firebase key
                            dbref.child("USERS").child(UserId).child("TASKS").child(key).setValue(a);
                            //close the dialog
                            dialog.cancel();
                        }

                    }
                });



            }

        });
    listview = (ListView)findViewById(R.id.list);

        final DatabaseReference db2 = dbref.child("USERS").child(UserId).child("TASKS");
        //connect the listview adapter with the gui
        final FirebaseListAdapter<Task> fadpt = new FirebaseListAdapter<Task>(
                this,
                Task.class,
                R.layout.row,
                db2
        ) {
            @Override
            protected void populateView(View v, final Task model, final int position) {

                TextView datetext  = (TextView)v.findViewById(date);
                CheckBox done      = (CheckBox)v.findViewById(checkBox);
                ImageButton del    = (ImageButton)v.findViewById(delete);
                TextView textview  = (TextView)v.findViewById(text);
                ImageButton pencil = (ImageButton)v.findViewById(edit);


                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbref.child("USERS").child(UserId).child("TASKS").child(model.getId()).removeValue();
                    }
                });



                /// implement edit function
                final String date = model.getText().substring(0,10);
                datetext.setText(date);
                final String textpost = model.getText().substring(11);
                textview.setText(textpost);

                //read the done status from encoded text!
                final char donestatus = model.getText().toString().charAt(10);
                //if is undone make the text white and uncheck the checkbutton
                if(donestatus == 'u'){
                    done.setChecked(false);
                    textview.setTextColor(Color.parseColor("#000000"));
                }else{
                    //if is done make the text green and make checkbox checked
                    done.setChecked(true);
                    textview.setTextColor(Color.parseColor("#33cc33"));
                }
                //when checkbox pressed, change the value on the database
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //replace a new Task with other done status encoded into it's text!!!!
                        Task newtask = new Task();
                        newtask.setId(model.getId());
                        char newdonestatus;
                        if(donestatus =='u'){
                            newdonestatus = 'd';
                        }else{
                            newdonestatus = 'u';
                        }
                        String newdonestatus2 = newdonestatus+"";
                        //Encode the new text for new task to save on the Firebase Database
                        newtask.setText(date+newdonestatus2+textpost);
                        //Replace the old task with the new task thas has other done status!!!
                        dbref.child("USERS").child(UserId).child("TASKS").child(model.getId()).setValue(newtask);
                    }
                });

                //dspaly the modal for change text to a task
                pencil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog  edit = new  Dialog(MainActivity.this);
                        edit.setTitle("Task");
                        edit.setContentView(R.layout.dialogtask);
                        final EditText newtext = (EditText)edit.findViewById(text);
                        Button savebtn = (Button)edit.findViewById(save);

                        //Retrive the date and the done status from the old task before edit
                        final String taskdate  = model.getText().substring(0,10);
                        final char taskdone    = model.getText().charAt(10);
                        String tasktext  = model.getText().substring(11);
                        newtext.setText(tasktext);
                        //Pop up the modal dialog
                        edit.show();
                        //save button lsitener
                        savebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //new String text inputed by user \\trim the spaces
                                String neweditedtext = newtext.getText().toString().trim();
                                String newtasktext   = taskdate+taskdone+neweditedtext;
                                //validate text length
                                if(newtasktext.length() < 17){
                                    Toast.makeText(MainActivity.this, "At least 6 chars!!!", Toast.LENGTH_LONG).show();
                                }else{
                                    //Make a new Task object
                                    Task newtask = new Task();
                                    newtask.setId(model.getId());
                                    newtask.setText(newtasktext);
                                    //put the new edited task into firebase and replace the old one
                                    dbref.child("USERS").child(UserId).child("TASKS").child(model.getId()).setValue(newtask);
                                    newtext.setText("");
                                    edit.cancel();
                                }
                            }
                        });
                    }
                });
            }
        };
    //Connect the adapter to listview
    listview.setAdapter(fadpt);
   }

}