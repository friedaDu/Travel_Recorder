package com.example.android.travelrecorder;


import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.microsoft.windowsazure.mobileservices.*;




import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelrecorder.data.TravelContract;
import com.example.android.travelrecorder.data.TravelDbHelper;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TravelDbHelper mDbHelper;
    private MobileServiceClient mClient;




    private String hasActiveTravel(String user) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM Travels WHERE userId=? AND status=1", new String[] {user});

        if(cursor.getCount()==0){
            Toast.makeText(this, "No active travel found.", Toast.LENGTH_SHORT).show();

            return null;
        }
        String id=null;
        if(cursor.moveToNext()){

        int userColumnIndex = cursor.getColumnIndex(TravelContract.LinkEntry.COLUMN_TRAVEL);
        id=cursor.getString(userColumnIndex);
        cursor.close();

        }
        return id;

    }

    private String createTravel(String user){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String travelId;
        travelId=user+"&"+String.valueOf(System.currentTimeMillis());
        ContentValues values = new ContentValues();
        values.put(TravelContract.LinkEntry.COLUMN_USER,user);
        values.put(TravelContract.LinkEntry.COLUMN_TRAVEL,travelId);
        values.put(TravelContract.LinkEntry.COLUMN_STATUS,1);
        long newRowId = db.insertOrThrow(TravelContract.LinkEntry.TABLE_NAME, null, values);
        return travelId;

    }

    public void startNewIntent(){
        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", null);
        String token = preferences.getString("token", null);
        if (userId==null) {
            Toast.makeText(MainActivity.this,"Please log in or register to begin recording",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity2.class);
            startActivity(intent);
            return;
        }
//            String hashId = BCrypt.hashpw(String.valueOf(userId), BCrypt.gensalt());
        if(!BCrypt.checkpw(userId, token)) {
            Toast.makeText(MainActivity.this,"wrong token",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity2.class);
            startActivity(intent);
            return;
        }
        else if(BCrypt.checkpw(userId, token)) {
            Toast.makeText(MainActivity.this,"access succeed",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            if(hasActiveTravel(userId)!=null){
                String travelId=hasActiveTravel(userId);
                intent.putExtra("travelId", travelId);

            }
            else{
                String travelId=createTravel(userId);
                intent.putExtra("travelId", travelId);
            }
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{mClient = new MobileServiceClient(
                "https://travelrecorder.azurewebsites.net",
                this
        );
        } catch (MalformedURLException e){
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startNewIntent();
            }
        });
       Button logButton = (Button) findViewById(R.id.logButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity2.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDbHelper = new TravelDbHelper(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//    private void displayDatabaseInfo() {
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                TravelContract.TravelEntry._ID,
//                TravelContract.TravelEntry.COLUMN_LATITUDE,
//                TravelContract.TravelEntry.COLUMN_LONGTITUDE,
//                TravelContract.TravelEntry.COLUMN_TIMESTAMP };
//
//        // Perform a query on the pets table
//        Cursor cursor = db.query(
//                TravelContract.TravelEntry.TABLE_NAME,   // The table to query
//                projection,            // The columns to return
//                null,                  // The columns for the WHERE clause
//                null,                  // The values for the WHERE clause
//                null,                  // Don't group the rows
//                null,                  // Don't filter by row groups
//                null);                   // The sort order
//
//        TextView displayView = (TextView) findViewById(R.id.main_textview);
//
//        try {
//            // Create a header in the Text View that looks like this:
//            //
//            // The pets table contains <number of rows in Cursor> pets.
//            // _id - name - breed - gender - weight
//            //
//            // In the while loop below, iterate through the rows of the cursor and display
//            // the information from each column in this order.
//            displayView.setText("The location table contains " + cursor.getCount() + " locations.\n\n");
//            displayView.append(TravelContract.TravelEntry._ID + " - " +
//                    TravelContract.TravelEntry.COLUMN_LATITUDE + " - " +
//                    TravelContract.TravelEntry.COLUMN_LONGTITUDE + " - " +
//                    TravelContract.TravelEntry.COLUMN_TIMESTAMP + "\n");
//
//            // Figure out the index of each column
//            int idColumnIndex = cursor.getColumnIndex(TravelContract.TravelEntry._ID);
//            int latitudeColumnIndex = cursor.getColumnIndex(TravelContract.TravelEntry.COLUMN_LATITUDE);
//            int longtitudeColumnIndex = cursor.getColumnIndex(TravelContract.TravelEntry.COLUMN_LONGTITUDE);
//            int timeColumnIndex = cursor.getColumnIndex(TravelContract.TravelEntry.COLUMN_TIMESTAMP);
//
//
//            // Iterate through all the returned rows in the cursor
//            while (cursor.moveToNext()) {
//                // Use that index to extract the String or Int value of the word
//                // at the current row the cursor is on.
//                int currentID = cursor.getInt(idColumnIndex);
//                double currentLatitude = cursor.getDouble(latitudeColumnIndex);
//                double currentLongtitude = cursor.getDouble(longtitudeColumnIndex);
//                int currentTime = cursor.getInt(timeColumnIndex);
//
//                // Display the values from each column of the current row in the cursor in the TextView
//                displayView.append(("\n" + currentID + " - " +
//                        currentLatitude + " - " +
//                        currentLongtitude + " - " +
//                        currentTime ));
//            }
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
    }
    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Create and/or open a database to read from it


        // Define a projection that specifies which columns from the database
//         you will actually use after this query.
        String[] projection = {
                TravelContract.UsersEntry._ID,
                TravelContract.UsersEntry.COLUMN_EAMIL,
                TravelContract.UsersEntry.COLUMN_PASSWORD,
        };

        Cursor cursor = db.query(
                TravelContract.UsersEntry.TABLE_NAME,   // The table to query
                projection,      // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.main_textview);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The users table contains " + cursor.getCount() + " users.\n\n");
            displayView.append(TravelContract.UsersEntry._ID + " - " +
                    TravelContract.UsersEntry.COLUMN_EAMIL + " - " +
                    TravelContract.UsersEntry.COLUMN_PASSWORD + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(TravelContract.UsersEntry._ID);
            int latitudeColumnIndex = cursor.getColumnIndex(TravelContract.UsersEntry.COLUMN_EAMIL);
            int longtitudeColumnIndex = cursor.getColumnIndex(TravelContract.UsersEntry.COLUMN_PASSWORD);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentLatitude = cursor.getString(latitudeColumnIndex);
                String currentLongtitude = cursor.getString(longtitudeColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentLatitude + " - " +
                        currentLongtitude));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

//    private void displayDatabaseInfo() {
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        // Create and/or open a database to read from it
//
//
//        // Define a projection that specifies which columns from the database
////         you will actually use after this query.
//        String[] projection = {
//                TravelContract.LinkEntry._ID,
//                TravelContract.LinkEntry.COLUMN_USER,
//                TravelContract.LinkEntry.COLUMN_TRAVEL,
//                TravelContract.LinkEntry.COLUMN_STATUS,
//        };
//
//        Cursor cursor = db.query(
//                TravelContract.LinkEntry.TABLE_NAME,   // The table to query
//                projection,      // The columns to return
//                null,                  // The columns for the WHERE clause
//                null,                  // The values for the WHERE clause
//                null,                  // Don't group the rows
//                null,                  // Don't filter by row groups
//                null);                   // The sort order
//
//        TextView displayView = (TextView) findViewById(R.id.main_textview);
//
//        try {
//            // Create a header in the Text View that looks like this:
//            //
//            // The pets table contains <number of rows in Cursor> pets.
//            // _id - name - breed - gender - weight
//            //
//            // In the while loop below, iterate through the rows of the cursor and display
//            // the information from each column in this order.
//            displayView.setText("The users table contains " + cursor.getCount() + " travels.\n\n");
////            displayView.append(TravelContract.UsersEntry._ID + " - " +
////                    TravelContract.UsersEntry.COLUMN_EAMIL + " - " +
////                    TravelContract.UsersEntry.COLUMN_PASSWORD + "\n");
////
////            // Figure out the index of each column
//            int idColumnIndex = cursor.getColumnIndex(TravelContract.LinkEntry._ID);
//            int latitudeColumnIndex = cursor.getColumnIndex("userId");
//            int longtitudeColumnIndex = cursor.getColumnIndex("travelId");
//            int statusCol=cursor.getColumnIndex("status");
////
////            // Iterate through all the returned rows in the cursor
//            while (cursor.moveToNext()) {
//                // Use that index to extract the String or Int value of the word
//                // at the current row the cursor is on.
//                int currentID = cursor.getInt(idColumnIndex);
//                String currentLatitude = cursor.getString(latitudeColumnIndex);
//                String currentLongtitude = cursor.getString(longtitudeColumnIndex);
//                int status=cursor.getInt(statusCol);
//                // Display the values from each column of the current row in the cursor in the TextView
//                displayView.append(("\n" + currentID + " - " +
//                        currentLatitude + " - " +
//                        currentLongtitude+" - "+status));
//            }
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//    }
}
