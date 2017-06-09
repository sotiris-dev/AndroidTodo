package com.todo.androidtodo;

import java.util.Calendar;

/**
 * Created by ubuntu on 9/6/2017.
 */

public class Date {
    //return the current day in format dd/mm/yy
    public static String getDate()
    {

            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            String mday2="";
            String month2="";


            if(mMonth < 10){
                 month2 = "0"+mMonth;
            }else{
                 month2 = ""+mMonth;
            }
            if(mDay < 10){
                 mday2 = "0"+mDay;
            }else{
                 month2 = ""+mDay;
            }
            return mday2+"/"+month2+"/"+mYear;

    }
}
