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
                + TravelEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_LOCATIONS_TABLE);

        String SQL_CREATE_USERS_TABLE="CREATE TABLE USERS ("+ TravelContract.UsersEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL, password TEXT NOT NULL); ";
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
