package com.sourcey.materiallogindemo.MYSQL;

import android.text.format.DateFormat;
import android.util.Log;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by River on 2018/3/21.
 */

public class buffer {
    private static String name;
    private static String account;
    private static String password;
    private static String ServerPosition = "http://140.116.82.102:8080";
    private static String alert_question_voice;
    private static ArrayList<String[]> dailyMood, sleepTime, getupTime, GPS;
    private static int type = 1,Max = 0,count=1;

    public static void setname(String Sname) {
        name = Sname;
    }

    public static void setaccount(String Saccount) {
        account = Saccount;
    }

    public static void setpassword(String Spassword) {
        password = Spassword;
    }

    public static void setgetcount(){ count++;}

    public static void setArraList(ArrayList<ArrayList<String[]>> arraList) {
        dailyMood = arraList.get(0);
        sleepTime = arraList.get(1);
        getupTime = arraList.get(2);
//        GPS = arraList.get(3);
    }

    public static void setGPSData(ArrayList<String[]> gps){
        GPS = gps;
    }

    public static void setAlert_question_voice(String offon){ alert_question_voice = offon;}

    public static String getTime() {
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());
        String time = s.toString();
        return time;
    }
    public static String getTimeM() {
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("yyyy", mCal.getTime());
        String time = s.toString();
        return time;
    }
    public static String getTimeSP() {
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("yyyy-MM-dd+kk:mm:ss", mCal.getTime());
        String time = s.toString();
        return time;
    }
    public static String getEmotion() {
        String[] mood = new String[7];
        String emotion = "0,0,0,0,0,0,0";
        mood[0] = "0";
        mood[1] = "0";
        mood[2] = "0";
        mood[3] = "0";
        mood[4] = "0";
        mood[5] = "0";
        mood[6] = "0";
        return emotion;
    }

    public static String getName() {
        return name;
    }

    public static String getAccount() {
        return account;
    }
    public static String getServerPosition() {
        return ServerPosition;
    }
    public static String getPassword() {
        return password;
    }

    public static int getcount(){ return count; }

    public static ArrayList<String[]> getArrayList() {
        switch (type) {
            case 1:
                return dailyMood;
            case 2:
                return sleepTime;
            case 3:
                return getupTime;
            case 4:
                return GPS;
            default:
                return null;
        }
    }

    public static void typeadd(){
        type++;
    }

    public static void typezero(){
        type = 1;
    }

    public static int getType(){
        return type;
    }

    public static void Renderer_Max(int value){
        Max = value;
    }

    public static int get_Renderer_Max(){
        return Max;
    }

    public static String getAlert_question_voice(){ return alert_question_voice;}
}
