package com.csci448.dkern_a3.mapit.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.csci448.dkern_a3.mapit.database.MarkerDbSchema.MarkerTable;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by dkern on 3/20/17.
 */

public class MarkerCursorWrapper extends CursorWrapper
{
    /**
     * Constructor
     * @param cursor
     */
    public MarkerCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    /**
     * Gets latitude and longitude of a point
     * @return LatLng object
     */
    public LatLng getMarkerLocation()
    {
        double latitude = getDouble(getColumnIndex(MarkerTable.Cols.LAT));
        double longitude = getDouble(getColumnIndex(MarkerTable.Cols.LONG));

        LatLng ll = new LatLng(latitude, longitude);

        return ll;
    }

    /**
     * Gets weather and date info
     * @return string for snackbar
     */
    public String getMarkerInfo()
    {
        String temperature = getString(getColumnIndex(MarkerTable.Cols.TEMPERATURE));
        String weatherDesc = getString(getColumnIndex(MarkerTable.Cols.WEATHER));
        String date = getString(getColumnIndex(MarkerTable.Cols.DATE));

        String message = "You were here: " + date + "\n";
        message += "Weather: " + temperature + "F" + " (" + weatherDesc + ")";

        return message;
    }
}
