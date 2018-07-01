package com.example.android.travelrecorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
    }

    public static class locations {
        @com.google.gson.annotations.SerializedName("id")
        private String mId;
        private Double latitude;
        private Double longtitude;
        private String travelId;
        locations(LatLng loc, String id){
            travelId=id;
            latitude=loc.latitude;
            longtitude=loc.longitude;
        }

    }
}
