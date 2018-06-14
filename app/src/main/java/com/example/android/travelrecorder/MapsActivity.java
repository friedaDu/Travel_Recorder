package com.example.android.travelrecorder;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private  TravelDbHelper mDbHelper=new TravelDbHelper(this);
    LocationManager locationManager;
    private Button btn1, btn2;
    private void initView() {
        btn1 = (Button) findViewById(R.id.RestartButton);

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
        }
    }
    private void insertLocation(LatLng lat) {
        TravelDbHelper  mDbHelper=new TravelDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TravelContract.TravelEntry.COLUMN_LATITUDE,lat.latitude);
        values.put(TravelContract.TravelEntry.COLUMN_LONGTITUDE,lat.longitude);
        values.put(TravelContract.TravelEntry.COLUMN_TIMESTAMP,System.currentTimeMillis());
        long newRowId = db.insert(TravelContract.TravelEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving location", Toast.LENGTH_SHORT).show();}

    }
//    private void insertLocation() {
//        TravelDbHelper  mDbHelper=new TravelDbHelper(this);
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(TravelContract.TravelEntry.COLUMN_LATITUDE,0.1);
//        values.put(TravelContract.TravelEntry.COLUMN_LONGTITUDE,53);
//        Log.v("hhhhh",String.valueOf(System.currentTimeMillis()));
//        values.put(TravelContract.TravelEntry.COLUMN_TIMESTAMP,System.currentTimeMillis());
//        long newRowId = db.insert(TravelContract.TravelEntry.TABLE_NAME, null, values);
//        if (newRowId == -1) {
//            // If the row ID is -1, then there was an error with insertion.
//            Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();}
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longtitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longtitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
//                    insertLocation(latLng);
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longtitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longtitude);
                    addPath(mMap);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    insertLocation(latLng);
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

    public void addPath(GoogleMap map) {

        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .width(5)
                .color(Color.BLUE);
//                .add(new LatLng(37.35, -122.0))
//                .add(new LatLng(37.45, -122.0))
//                .add(new LatLng(37.45, -122.2))
//                .add(new LatLng(37.35, -122.2))
//                .add(new LatLng(37.35, -122.0));

        // Get back the mutable Polyline
        List<LatLng> points = new ArrayList<>();

        points=getAllPoints();

        for(int index=0;index<points.size();index++){
            rectOptions.add(points.get(index));
            Polyline polyline = map.addPolyline(rectOptions);
        }

//        polyline.setPoints(points);
    }
    public List<LatLng> getAllPoints() {
        String sql = "select * from Location_Trace";
        TravelDbHelper mDbHelper=new TravelDbHelper(this);
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        List<LatLng> temp=new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int latitudeColumnIndex = cursor.getColumnIndex(TravelContract.TravelEntry.COLUMN_LATITUDE);
            int longtitudeColumnIndex = cursor.getColumnIndex(TravelContract.TravelEntry.COLUMN_LONGTITUDE);
            double currentLatitude = cursor.getDouble(latitudeColumnIndex);
            double currentLongtitude = cursor.getDouble(longtitudeColumnIndex);
            temp.add(new LatLng(currentLatitude,currentLongtitude));
        }
        return temp;



    }

    }