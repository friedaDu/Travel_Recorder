package com.example.android.travelrecorder.data;

import android.provider.BaseColumns;

/**
 * Created by a123 on 02/06/2018.
 */

public final class TravelContract {
    private TravelContract() {}

    public static final class TravelEntry implements BaseColumns {

        public final static String TABLE_NAME = "Location_Trace";

        public final static String _ID = BaseColumns._ID;
//        public final static String COLUMN_TRAVEL="travelId";
        public final static String COLUMN_LATITUDE = "latitude";
        public final static String COLUMN_LONGITUDE = "longitude";
        public final static String COLUMN_TIMESTAMP = "time";


    }
    public static final class UsersEntry implements BaseColumns {

        public final static String TABLE_NAME = "USERS";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_EAMIL="email";
        public final static String COLUMN_PASSWORD = "password";

    }

}