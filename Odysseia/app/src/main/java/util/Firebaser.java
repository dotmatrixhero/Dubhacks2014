package util;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import java.util.Map;

import MapEntity.MapEntity;
import MapEntity.QuestEntity;

/**
 * Created by Christina on 10/17/2014.
 * Util class that handles storing data to Firebase + Geofire
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

    // updates children of a first-level child in Firebase without overwriting the unreferenced children
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

    // stores a new game
    // Firebase will autogenerate a unique ID for us
    // @param dataMap : map of data to store under ID key (e.g. markers & associated data, not including location)
    public static String storeNewGame(Map<String, Object> dataMap)
    {
        Firebase gameRef = ref.child("games");
        Firebase newGameRef = gameRef.push();

        newGameRef.setValue(dataMap);

        // return the new Firebase-generated game ID
        return newGameRef.getName();
    }

    // MapEntity is the same as a 'marker'
    // store a new marker (MapEntity); Firebase will autogenerate a unique ID.
    // @param gameid : id of game the marker belongs to
    // @param mapEntity : the marker to store
    // @param loc : location (LatLng) of the marker
    public static String storeMarker(String gameid, MapEntity mapEntity, LatLng loc)
    {
        Firebase gameRef = ref.child("games").child(gameid).child(mapEntity.markerType.toString());
        Firebase newQuestRef = gameRef.push();
        newQuestRef.setValue(mapEntity);
        String markerid = newQuestRef.getName();

        // store quest location
        storeMarkerLocation(markerid, loc);

        // return the new Firebase-generated quest ID
        return markerid;
    }

    // set a marker location in geofire
    // @param markerid : unique marker id
    // @param loc :  location (LatLng) of the marker
    public static void storeMarkerLocation(String markerid, LatLng loc)
    {
        geofire.setLocation(markerid, new GeoLocation(loc.latitude, loc.longitude));
    }

    // delete an entire game
    public static void deleteGame(String gameid) {
        ref.child("games").child(gameid).removeValue();
    }

    public static  void removeMarker() {

    }
    public static void removeMarkerLocation(String markerid) {
        geofire.removeLocation(markerid);
    }
}