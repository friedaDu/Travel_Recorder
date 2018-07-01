package com.example.android.travelrecorder;

/**
 * Created by a123 on 30/06/2018.
 */

import com.google.android.gms.maps.model.LatLng;

public class locations {
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
