package com.todo.androidtodo;

import java.util.Calendar;

/**
 * Created by ubuntu on 7/6/2017.
 */

public class Task {

    private String  text;
    private String id;

    public Task()
    {


    }
    public Task(String id, String text)
    {
        this.id = id;
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }
    public void setText(String text){
        this.text = text;
    }
    public String getId(){
        return this.id ;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    private static String date()
    {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        return mDay+"/"+mMonth+"/"+mYear;
    }

}
