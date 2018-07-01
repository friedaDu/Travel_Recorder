package com.example.android.travelrecorder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.travelrecorder.data.TravelContract.TravelEntry;

public class TravelDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="travelRecord.db";
    private static final Integer Version = 1;
            public TravelDbHelper(Context context){
        super (context,DATABASE_NAME, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_LOCATIONS_TABLE =  "CREATE TABLE " + TravelEntry.TABLE_NAME + " ("
                + TravelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TravelEntry.COLUMN_LATITUDE + " DOUBLE, "
                 + TravelEntry.COLUMN_LONGITUDE+" DOUBLE, "
                +TravelEntry.COLUMN_TRAVEL+" TEXT, "
                + TravelEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL);";


        db.execSQL(SQL_CREATE_LOCATIONS_TABLE);

        String SQL_CREATE_USERS_TABLE2="CREATE TABLE USERS ("+ TravelContract.UsersEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL, password TEXT NOT NULL); ";
        db.execSQL(SQL_CREATE_USERS_TABLE2);
        String SQL_CREATE_USERS_TABLE3="CREATE TABLE Travels ("+ TravelContract.LinkEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, userId TEXT NOT NULL, travelId TEXT NOT NULL, status INTEGER); ";
        db.execSQL(SQL_CREATE_USERS_TABLE3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Created by a123 on 24/06/2018.
     */

    public static class users {
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
}
