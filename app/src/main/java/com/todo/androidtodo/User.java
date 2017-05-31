package com.todo.androidtodo;

/**
 * Created by ubuntu on 27/5/2017.
 */

public class User {

    public String name,lastname,birthdate,email,username;

    public User(String Name, String Lastname, String Birthdate, String Email, String Username )
    {
        this.name      = Name;
        this.lastname  = Lastname;
        this.birthdate = Birthdate;
        this.email     = Email;
        this.username  = Username;
    }

    //function validator
    public boolean RegValidator()
    {
        if(this.name.length() < 5 || this.lastname.length() < 5 || this.birthdate.length() < 8
                || this.email.length()< 5 || this.username.length()< 5 )
        {
            return false;
        }else
        {
            return true;
        }
    }
}
