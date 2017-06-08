package com.todo.androidtodo;

import java.util.Calendar;

/**
 * Created by ubuntu on 7/6/2017.
 */

public class Task {

    private String  text;
    private String  date;
    private String  done;

    public Task(String text)
    {
        this.done = "false";
        this.text = text;
        this.date = this.date();

    }

    public String getText()
    {
        return this.text;
    }
    public void setText(String text){
        this.text = text;
    }
    public String getDate()
    {
        return this.date;
    }
    public void setDate(){
        this.date = date;
    }
    public String getDone()
    {
        return this.done;
    }
    public void setDone(String done){
        this.done = done;
    }

    private String date()
    {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        return mDay+"/"+mMonth+"/"+mYear;
    }

}
