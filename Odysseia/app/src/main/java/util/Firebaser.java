package util;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christina on 10/17/2014.
 */
public class Firebaser {

    //TODO: put Firebase URL in config file
    protected static String firebaseUrl = "https://dazzling-torch-6796.firebaseio.com/odysseia";

    protected static Firebase ref;
    protected static GeoFire geofire;


    public Firebaser () {
        this.ref = new Firebase(firebaseUrl);
        this.geofire = new GeoFire(ref);
    }

    // tests connection to Firebase
    public static boolean test() {
        return true;
    }



    private LatLng returnLocation(GeoLocation loc) {

        return new LatLng(loc.latitude, loc.latitude);
    }

    // store function.
    // @param id : unique identifying key, first-level child only (e.g. game ID)
    // @param dataMap : map of data to store under ID key (e.g. markers & associated data, not including location)
    public static void store(String id, Map<String, Object> dataMap) {
        Firebase keyRef = ref.child(id);
        keyRef.updateChildren(dataMap);
    }

    // store function
    // @param id : unique identifying key, first-level child only (e.g. game ID)
    // @param dataMap:  map of data to store under ID key (e.g. markers & associated data, not including location)
    // @param locMap : map of unique marker IDs to corresponding marker location to store with geofire
    public static void store(String id, Map<String, Object> dataMap, Map<String, LatLng> locMap) {
        for(Map.Entry<String, LatLng> entry : locMap.entrySet()) {
            String locID = entry.getKey();
            LatLng loc = entry.getValue();
            geofire.setLocation(locID, new GeoLocation(loc.latitude, loc.longitude));
        }

        store(id, dataMap);
    }

    // removal function
    public static void remove(String key) {
        ref.child(key).removeValue();
        geofire.removeLocation(key);
    }
}
