package com.csci448.dkern_a3.mapit;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.csci448.dkern_a3.mapit.database.MarkerBaseHelper;
import com.csci448.dkern_a3.mapit.database.MarkerCursorWrapper;
import com.csci448.dkern_a3.mapit.database.MarkerDbSchema.MarkerTable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MapActivity extends AppCompatActivity
{
    private static final String TAG = "MapActivity";
    private CoordinatorLayout mCoordinatorLayout;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mClient;

    private Location mCurrentLocation;
    private String mDate;
    private String mWeatherType;
    private double mWeatherTemp;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    //region Overridden methods

    /**
     * Creates view, hooks up map, loads markers from database
     * @param savedInstanceState Holds saved variables if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        checkPermission();

        mContext = getApplicationContext();
        mDatabase = new MarkerBaseHelper(mContext).getWritableDatabase();

        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                findCurrentLocation();
            }
        });

        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle)
                    {
                        invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i)
                    {

                    }
                })
                .build();

        FragmentManager fmanager = getSupportFragmentManager();
        mMapFragment = (SupportMapFragment) fmanager.findFragmentById(R.id.map_fragment);

        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                loadMarkers();
            }
        });

    }

    /**
     * starts the googleApiClient
     */
    @Override
    protected void onStart()
    {
        mClient.connect();
        super.onStart();
    }

    /**
     * Stops googleApiClient
     */
    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }

    /**
     * Inflates the menu options (Only Clear Map)
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    /**
     * Decides which menu item was pressed
     * @param item item that was pressed
     * @return true if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.clear_map)
        {
            mDatabase.delete(MarkerTable.NAME, null, null);
            mMap.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Helper functions to OnCreate
    /**
     * Checks if the app has location permissions and asks for them
     */
    private void checkPermission()
    {
        Log.d(TAG, "checkPermission()");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "Requesting Permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
    }

    /**
     * Loads all markers in the database onto the map
     */
    private void loadMarkers()
    {
        MarkerCursorWrapper cursor = queryMarkers(null, null);

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                LatLng ll = cursor.getMarkerLocation();
                addMarkerToMap(ll);
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
    }

    //endregion

    //region Location/Map Helpers
    /**
     * Uses location services t ofind the current location, executes the WeatherTask
     * @return
     */
    public boolean findCurrentLocation()
    {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        try
        {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mClient, request, new LocationListener()
                    {
                        @Override
                        public void onLocationChanged(Location location)
                        {
                            Log.i(TAG, "Location found: " + location);
                            mCurrentLocation = location;
                            DateFormat df = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                            mDate = df.format(Calendar.getInstance().getTime());
                            new WeatherTask().execute(mCurrentLocation);
                        }
                    });
        } catch (SecurityException se)
        {
            Log.i(TAG, "findCurrentLocation(): " + se.getMessage());
        }
        return true;
    }

    /**
     * Adds new point to map, moves camera to new point
     */
    public void updateUI()
    {
        if (mMap == null || mCurrentLocation == null) {
            return; }
        LatLng myPoint = new LatLng(
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        addMarkerToMap(myPoint);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myPoint, 19.0f);
        mMap.animateCamera(update);
    }

    /**
     * Add a single marker to the map
     * @param point Latitude and longitude of point to add
     */
    private void addMarkerToMap(LatLng point)
    {
        MarkerOptions myMarker = new MarkerOptions()
                .position(point)
                .title("Lat, Long")
                .snippet("(" + point.latitude + ", " + point.longitude + ")");

        mMap.addMarker(myMarker);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                //Show title/snippet
                marker.showInfoWindow();

                //Show weather/date in snackbar
                LatLng ll = marker.getPosition();
                String[] args = new String[]{String.valueOf(ll.latitude), String.valueOf(ll.longitude)};
                MarkerCursorWrapper cursor = queryMarkers(MarkerTable.Cols.LAT + " = CAST(? AS decimal) AND " + MarkerTable.Cols.LONG + " = CAST(? AS decimal)", args);

                String message;
                try {
                    if(cursor.getCount() == 0)
                    {
                        message = "Error getting date and weather data";
                    }
                    else
                    {
                        cursor.moveToFirst();
                        message = cursor.getMarkerInfo();
                    }
                }
                finally
                {
                    cursor.close();
                }
                Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
                return true;
            }
        });
    }
    //endregion

    //region Database Stuff

    /**
     * Gets contentvalues and adds row to database
     */
    public void addMarkerToDatabase()
    {
        ContentValues values = getContentValues();
        mDatabase.insert(MarkerTable.NAME, null, values);
    }

    /**
     * Builds contentvalues to add to database
     * @return column values for a row
     */
    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MarkerTable.Cols.LAT, mCurrentLocation.getLatitude());
        values.put(MarkerTable.Cols.LONG, mCurrentLocation.getLongitude());
        values.put(MarkerTable.Cols.DATE, mDate);
        values.put(MarkerTable.Cols.TEMPERATURE, mWeatherTemp);
        values.put(MarkerTable.Cols.WEATHER, mWeatherType);
        return values;
    }

    /**
     * Queries database for markers
     * @param whereClause Condition clause
     * @param whereArgs args for condition clause
     * @return cursor to entries in database
     */
    private MarkerCursorWrapper queryMarkers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                MarkerTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new MarkerCursorWrapper(cursor);
    }
    //endregion

    //region WeatherTask class
    /**
     * Class to handle OpenWeatherMap API call
     */
    private class WeatherTask extends AsyncTask<Location, Void, Void>
    {
        private final String API_KEY = "57ac18f5770d8deb1ad3150db98c7764";
        private Uri base_uri = Uri.parse("http://api.openweathermap.org/data/2.5/weather?")
                .buildUpon()
                .appendQueryParameter("APPID", API_KEY)
                .appendQueryParameter("units", "Imperial")
                .build();
        private String weatherDescription;
        private double weatherTemp;

        /**
         * Asks OpenWeatherMap for weather data
         * @param params Location of user
         * @return null
         */
        @Override
        protected Void doInBackground(Location... params)
        {
            Location l = params[0];
            String uri = base_uri
                    .buildUpon()
                    .appendQueryParameter("lat", "" + l.getLatitude())
                    .appendQueryParameter("lon", "" + l.getLongitude())
                    .build().toString();
            try
            {
                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder json = new StringBuilder();
                String line;
                while((line=reader.readLine())!=null)
                {
                    json.append(line).append("\n");
                }
                reader.close();

                JSONObject weatherJson = new JSONObject(json.toString());
                JSONArray weather = weatherJson.getJSONArray("weather");
                JSONObject temperature = weatherJson.getJSONObject("main");
                weatherDescription = weather.getJSONObject(0).getString("main");
                weatherTemp = temperature.getDouble("temp");
                connection.disconnect();
            } catch (Exception e)
            {
                Log.i(TAG, "WeatherTask: " + e.getMessage());
            }
            return null;
        }

        /**
         * Sets weather variables, adds to database and updates map
         * @param result
         */
        @Override
        protected void onPostExecute(Void result)
        {
            mWeatherTemp = weatherTemp;
            mWeatherType = weatherDescription;
            addMarkerToDatabase();
            updateUI();
        }
    }
    //endregion
}
