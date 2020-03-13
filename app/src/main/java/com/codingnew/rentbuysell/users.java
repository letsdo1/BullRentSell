package com.codingnew.rentbuysell;

import android.content.Context;
import android.content.SharedPreferences;

public class users {

   /* String NAME;
    String HOSTEL;
    String ROLL_NUMBER;
    String EMAIL;
    String MOILE_NUMBER;
    String IMAGE_URL;*/



    String ID;
    Context context;
    public static SharedPreferences sharedPreferences;
    public  static SharedPreferences.Editor editor;


    public users(){

    }

    public users(Context cont)
    {
        this.context=cont;
        sharedPreferences = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }

    public  void removeUser(){
        editor.clear();
        editor.apply();
    }





   /* public users(String name, String hostel, String RollNumber, String Email, String mobileno, String ImageUrl){
        this.NAME=name;
        this.EMAIL=Email;
        this.HOSTEL=hostel;
        this.ROLL_NUMBER=RollNumber;
        this.MOILE_NUMBER=mobileno;
        this.IMAGE_URL=ImageUrl;
    }*/


    public void createSession(String name,String hostel,String RollNumber, String Email, String mobileno, String ImageUrl,String UID )
    {
        editor.putString("NAME",name);
        editor.putString("HOSTEL",hostel);
        editor.putString("ROLL_NUMBER",RollNumber);
        editor.putString("EMAIL",Email);
        editor.putString("MOBILE_NUMBER",mobileno);
        editor.putString("IMAGE_URL",ImageUrl);
        editor.putString("UID",UID);
        editor.putBoolean("STATUS",true);
        editor.apply();
    }

    public String getNAME() {
        String NAME = sharedPreferences.getString("NAME", "");

        return NAME;}


    public static users getInstance(Context context) {
        return new users(context);
    }




/*


    }

    public void setNAME(String NAME) {
        editor= sharedPreferences.edit();
        this.NAME = NAME;
        editor.putString("NAME",NAME).apply();
        editor.putBoolean("LOGIN_STATUS",true);
    }


    public String getID() {
        ID= sharedPreferences.getString("ID","");
        return ID;
    }

    public void setID(String ID) {
        editor= sharedPreferences.edit();
        this.ID = ID;
        editor.putString("ID",ID).apply();
    }

    public String getHOSTEL() {
        HOSTEL= sharedPreferences.getString("HOSTEL","");
        return HOSTEL;

    }

    public void setHOSTEL(String HOSTEL) {
        editor= sharedPreferences.edit();
        this.HOSTEL = HOSTEL;
        editor.putString("HOSTEL",HOSTEL).apply();

    }

    public String getROLL_NUMBER() {
        ROLL_NUMBER= sharedPreferences.getString("ROLL_NUMBER","");
        return ROLL_NUMBER;
    }

    public void setROLL_NUMBER(String ROLL_NUMBER) {
        editor= sharedPreferences.edit();
        this.ROLL_NUMBER = ROLL_NUMBER;
        editor.putString("ROLL_NUMBER",ROLL_NUMBER).apply();

    }

    public String getEMAIL() {
       EMAIL= sharedPreferences.getString("EMAIL","");
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        editor= sharedPreferences.edit();
        this.EMAIL = EMAIL;
       editor.putString("EMAIL",EMAIL).apply();

    }

    public String getMOILE_NUMBER() {
        MOILE_NUMBER= sharedPreferences.getString("MOILE_NUMBER","");
        return MOILE_NUMBER;
    }

    public void setMOILE_NUMBER(String MOILE_NUMBER) {
        editor= sharedPreferences.edit();
        this.MOILE_NUMBER = MOILE_NUMBER;
        editor.putString("MOILE_NUMBER",MOILE_NUMBER).apply();

    }
    public String getIMAGE_URL() {
        IMAGE_URL= sharedPreferences.getString("IMAGE_URL","");

        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        editor= sharedPreferences.edit();
        this.IMAGE_URL = IMAGE_URL;
        editor.putString("IMAGE_URL",IMAGE_URL).apply();
    }*/

    public void logout () {
        editor.clear();
        editor.commit();
    }


    public void put(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();

    }


    public String getString(String key)
    {
        return sharedPreferences.getString(key,"");
    }



    public boolean checkloginstatus ()
    {
        return sharedPreferences.getBoolean("STATUS", false);
    }


}
