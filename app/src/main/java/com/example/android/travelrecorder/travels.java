package com.example.android.travelrecorder;
/**
 * Created by a123 on 24/06/2018.
 */

public class travels {
    private String Id;
    private String userId;
    private String travelId;
    private String tripName="mytrip";
    private Integer status=1;
    public travels(String user,String travel){
        travelId=travel;
        userId=user;

    }

    public String getTravelId(){
        return travelId;
    }
    public String getTripName(){
        return tripName;
    }
    public void setTripName(String name){
        tripName=name;
    }
    public void setStatus(Integer num){status=num;}


}