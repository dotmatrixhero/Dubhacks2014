package com.ragtag.Odysseia;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import MapEntity.MapEntity;
import MapEntity.NPCEntity;
import OdysseiaGame.OdysseiaGame;
import util.Firebaser;

public class GameMapFragment extends Fragment {

    GoogleMap googleMap;
    LocationManager locationManager;
    PendingIntent pendingIntent;
    SharedPreferences sharedPreferences;

    private Firebaser firebaser;

    private FragmentActivity fa;
    private View view;
    public static GameMapFragment newInstance() {
        GameMapFragment g = new GameMapFragment();
        return g;
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fa = super.getActivity();
        view = inflater.inflate(R.layout.activity_game_map, container, false);

        Firebase.setAndroidContext(fa.getBaseContext());
        firebaser = new Firebaser();

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(fa.getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, fa, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) fa.getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager) fa.getSystemService(fa.LOCATION_SERVICE);

            // Opening the sharedPreferences object
            sharedPreferences = fa.getSharedPreferences("location", 0);

            // Getting stored latitude if exists else return 0
            String lat = sharedPreferences.getString("lat", "0");

            // Getting stored longitude if exists else return 0
            String lng = sharedPreferences.getString("lng", "0");

            // Getting stored zoom level if exists else return 0
            String zoom = sharedPreferences.getString("zoom", "0");

            // If coordinates are stored earlier
            if(!lat.equals("0")){

                // Drawing circle on the map
                drawCircle(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                // Drawing marker on the map
                drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                // Moving CameraPosition to previously clicked position
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));

                // Setting the zoom level in the map
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(Float.parseFloat(zoom)));

            }

            googleMap.setOnMapClickListener(new OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    // Removes the existing marker from the Google Map
                    googleMap.clear();

                    // Drawing marker on the map
                    drawMarker(point);

                    // Drawing circle on the map
                    drawCircle(point);

                    // This intent will call the activity ProximityActivity
                    Intent proximityIntent = new Intent("android.intent.action.PROXTRIGGER");

                    // Creating a pending intent which will be invoked by LocationManager when the specified region is
                    // entered or exited
                    pendingIntent = PendingIntent.getActivity(fa.getBaseContext(), 0, proximityIntent,Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Setting proximity alert
                    // The pending intent will be invoked when the device enters or exits the region 20 meters
                    // away from the marked point
                    // The -1 indicates that, the monitor will not be expired
                    locationManager.addProximityAlert(point.latitude, point.longitude, 20, -1, pendingIntent);

                    /** Opening the editor object to write data to sharedPreferences */
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    /** Storing the latitude of the current location to the shared preferences */
                    editor.putString("lat", Double.toString(point.latitude));

                    /** Storing the longitude of the current location to the shared preferences */
                    editor.putString("lng", Double.toString(point.longitude));

                    /** Storing the zoom level to the shared preferences */
                    editor.putString("zoom", Float.toString(googleMap.getCameraPosition().zoom));

                    /** Saving the values stored in the shared preferences */
                    editor.apply();

                    Toast.makeText(fa.getBaseContext(), "Quest Location set!", Toast.LENGTH_SHORT).show();

                }
            });

            googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {
                    Intent proximityIntent = new Intent("android.intent.action.PROXTRIGGER");

                    pendingIntent = PendingIntent.getActivity(fa.getBaseContext(), 0, proximityIntent,Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Removing the proximity alert
                    locationManager.removeProximityAlert(pendingIntent);

                    // Removing the marker and circle from the Google Map
                    googleMap.clear();

                    // Opening the editor object to delete data from sharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Clearing the editor
                    editor.clear();

                    // Committing the changes
                    editor.apply();

                    Toast.makeText(fa.getBaseContext(), "Proximity Alert is removed", Toast.LENGTH_LONG).show();
                }
            });
        }
        return view;
    }

    private void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        NPCEntity marker = new NPCEntity(point, "Tsuku Yomi", new OdysseiaGame());
        firebaser.storeMarker("yomisgame", marker, point);

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
    }

    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(20);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }



    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #googleMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.

        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) fa.getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #googleMap} is not null.
     */
    private void setUpMap() {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(47.561846, -122.139131))
                .title("Hello world"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
       // Context.getSystemService(Context.LOCATION_SERVICE);
    }
}
