package com.example.diogo.sherlocknotebook;

import android.content.Context;
import android.content.Intent;

import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MapsActivity extends ActionBarActivity {

    public static String TAG = "com.example.diogo.sherlocknotebook";

    //toasts
    CharSequence internetText = "Please turn on your Wi-Fi/Data connection.";
    CharSequence gpsText = "Please turn on your GPS connection.";
    CharSequence boundText = "Outside of map boundaries.";
    int duration = Toast.LENGTH_LONG;

    //fragment activity
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    // Area boundaries
    /**private static final LatLngBounds oldPorto = new LatLngBounds(
     new LatLng(41.139200, -8.623968),// NE bound
     new LatLng(41.147086, -8.606802)); // SW bound**/

    private LatLng lastCenter = new LatLng(41.147779, -8.614559); // Praça guilherme gomes ferreira

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getToasts();

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_finish:
                //openFinish();
                return true;
            case R.id.action_notebook:
                Intent intent = new Intent(this, notebook.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getToasts();
        setUpMapIfNeeded();


    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        // latitude and longitude
        double gardenLatitude = 41.145741;
        double gardenLongitude = -8.616345;

        //Overlay Map
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.map_final))
                .position(lastCenter, 5000f, 3700f);


        mMap.addGroundOverlay(newarkMap);

        // Show user location
        mMap.setMyLocationEnabled(true);

        //Check boundaries
        Location centerLocation = new Location("Jardim");

        centerLocation.setLatitude(gardenLatitude);
        centerLocation.setLongitude(gardenLongitude);


        //if (mMap.getMyLocation().distanceTo(centerLocation)>2500)
            //getMapToast();


        // Create and add marker
        MarkerOptions marker = new MarkerOptions().position
                (new LatLng(gardenLatitude,gardenLongitude)).title("Jardim da Cordoaria");
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mMap.addMarker(marker);

        // Auto-update camera position

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(gardenLatitude, gardenLongitude)).zoom(15).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // hide zoom buttons
        mMap.getUiSettings().setZoomControlsEnabled(false);
        // hide compass
        mMap.getUiSettings().setCompassEnabled(false);
    }

    private boolean isNetWorkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isGPSAvailable(){

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return  mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void getToasts(){
        //Toasts
        Context context = getApplicationContext();

        Toast toastNet = Toast.makeText(context,internetText, duration);
        Toast toastGps = Toast.makeText(context,gpsText, duration);

        toastNet.setGravity(Gravity.CENTER, 0, 0);
        toastGps.setGravity(Gravity.CENTER, 0, 0);

        //Checks if required services are available
        if(isNetWorkAvailable()==true)
            Log.v(TAG,"Internet ok");
        else {
            toastNet.show();
            Log.v(TAG, "Internet fail");}
        if(isGPSAvailable()==true)
            Log.v(TAG,"GPS ok");
        else{
            toastGps.show();
            Log.v(TAG, "GPS fail");}

    }

    private void getMapToast() {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context,boundText, duration);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
        Log.v(TAG,"Out of bounds");
    }
}
