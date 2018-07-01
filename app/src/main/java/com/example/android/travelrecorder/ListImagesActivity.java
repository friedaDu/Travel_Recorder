package com.example.android.travelrecorder;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListImagesActivity extends ListActivity {

    String[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler handler = new Handler();

        Thread th = new Thread(new Runnable() {
            public void run() {

                try {

                    final String[] images = ImageManager.ListImages();

                    handler.post(new Runnable() {

                        public void run() {
                            ListImagesActivity.this.images = images;

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListImagesActivity.this, R.layout.text_view_item, images);
                            setListAdapter(adapter);
                        }
                    });
                }
                catch(Exception ex) {
                    final String exceptionMessage = ex.getMessage();
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(ListImagesActivity.this, exceptionMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }});
        th.start();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getBaseContext(), ImageActivity.class);
        intent.putExtra("image", images[position]);

        startActivity(intent);
    }

    /**
     * Created by a123 on 24/06/2018.
     */

    public static class travels {
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

    /**
     * Created by a123 on 30/06/2018.
     */

    public static class InfoWindowData {
        private String image;
        private String hotel;
        private String food;
        private String transport;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getHotel() {
            return hotel;
        }

        public void setHotel(String hotel) {
            this.hotel = hotel;
        }

        public String getFood() {
            return food;
        }

        public void setFood(String food) {
            this.food = food;
        }

        public String getTransport() {
            return transport;
        }

        public void setTransport(String transport) {
            this.transport = transport;
        }
    }
}