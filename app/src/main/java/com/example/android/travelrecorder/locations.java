package com.example.android.travelrecorder;

import com.google.android.gms.maps.model.LatLng;

public class locations {
    private Double latitude;
    private Double longtitude;
    private String travelId;
    locations(LatLng loc, String id){
        travelId=id;
        latitude=loc.latitude;
        longtitude=loc.longitude;
    }

}
