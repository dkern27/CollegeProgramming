package com.csci448.dkern_a3.mapit.database;

/**
 * Created by dkern on 3/20/17.
 */

public class MarkerDbSchema
{
    public static final class MarkerTable
    {
        public static final String NAME = "markers";
        public static final class Cols {
            public static final String LAT = "lat";
            public static final String LONG = "long";
            public static final String DATE = "date";
            public static final String TEMPERATURE = "temperature";
            public static final String WEATHER = "weather";
        }
    }
}
