package com.example.android.travelrecorder;

/**
 * Created by a123 on 24/06/2018.
 */

public class users {
    @com.google.gson.annotations.SerializedName("id")
    private String mId;
    private String email;
    private String password;
    public users(String memail,String hasdpass){
        email=memail;
        password=hasdpass;
    }
    public String getId(){
        return mId;
    }

    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }


}
