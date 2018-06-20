package com.example.android.travelrecorder;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.travelrecorder.data.TravelContract;
import com.example.android.travelrecorder.data.TravelDbHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private  TravelDbHelper mDbHelper=new TravelDbHelper(this);
    private String travelId=null;


    LocationManager locationManager;
    private Button btn1, btn2;

    private void initView() {
        btn1 = (Button) findViewById(R.id.RestartButton);
        btn2=(Button)findViewById(R.id.Start);

    }

    public void setTravelId(String str){
        travelId=str;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RestartButton:
                String sql1 = "DELETE from Location_Trace";
                String sql2 = "DELETE FROM sqlite_sequence";
                TravelDbHelper mDbHelper=new TravelDbHelper(this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.execSQL(sql1);
                db.execSQL(sql2);
                break;
            case R.id.Start:
                break;
            case R.id.Stop:
                break;
        }
    }
    private void insertLocation(LatLng lat,String travelId) {
        TravelDbHelper  mDbHelper=new TravelDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TravelContract.TravelEntry.COLUMN_LATITUDE,lat.latitude);
        values.put(TravelContract.TravelEntry.COLUMN_LONGITUDE,lat.longitude);
        values.put(TravelContract.TravelEntry.COLUMN_TRAVEL,travelId);
        values.put(TravelContract.TravelEntry.COLUMN_TIMESTAMP,System.currentTimeMillis());

        long newRowId = db.insertOrThrow(TravelContract.TravelEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving location", Toast.LENGTH_SHORT).show();}

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            boolean grant = grantResults[0] == PackageManager.PERMISSION_GRANTED;//是否授权，可以根据permission作为标记
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setContentView(R.layout.activity_maps);
        TextView textView=(TextView) this.findViewById(R.id.trip_id);
        Intent intent=getIntent();
        final String travelId=intent.getStringExtra("travelId");
        setTravelId(travelId);

        textView.setText(travelId);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longtitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longtitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    insertLocation(latLng,travelId);
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longtitude, 1);
                        String str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();
//                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
//                        insertLocation(latLng);
                    } catch (IOException E) {
                        E.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 20, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longtitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longtitude);
                    addPath(mMap,travelId);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    insertLocation(latLng,travelId);
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longtitude, 1);
                        String str = addressList.get(0).getLocality() + ",";
                        str += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException E) {
                        E.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

//    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Finish) {
            SQLiteDatabase db=mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("status", 0);

            db.update("Travels",values,"travelId=?",new String[] { travelId });

            Toast.makeText(this, "This Trip has been finished", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    public void addPath(GoogleMap map,String travelId) {

        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .width(5)
                .color(Color.BLUE);
        List<LatLng> points = new ArrayList<>();

        points=getAllPoints(travelId);

        for(int index=0;index<points.size();index++){
            rectOptions.add(points.get(index));
            Polyline polyline = map.addPolyline(rectOptions);
        }

//        polyline.setPoints(points);
    }
    public List<LatLng> getAllPoints(String travelId) {
        String sql = "select * from Location_Trace WHERE travelId=?";
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        List<LatLng> temp=new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,  new String[] {travelId});
        while (cursor.moveToNext()) {
            int latitudeColumnIndex = cursor.getColumnIndex(TravelContract.TravelEntry.COLUMN_LATITUDE);
            int longtitudeColumnIndex = cursor.getColumnIndex(TravelContract.TravelEntry.COLUMN_LONGITUDE);
            double currentLatitude = cursor.getDouble(latitudeColumnIndex);
            double currentLongitude = cursor.getDouble(longtitudeColumnIndex);
            temp.add(new LatLng(currentLatitude,currentLongitude));
        }
        cursor.close();
        return temp;

    }

}