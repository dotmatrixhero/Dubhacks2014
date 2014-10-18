
package MapEntity;
import OdysseiaGame.OdysseiaGame;
import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Brian on 10/11/2014.
 */

public abstract class MapEntity {
    private String id;
    public String name;
    public String description;
    private double longitude;
    private double latitude;
    private float triggerRadiusInMeters;

    private OdysseiaGame game;
    private boolean isActive;

    public MapEntity(double lat, double lon, String name, OdysseiaGame game) {
        String fragment;
        if (name.length() >= 5)
            fragment = name.substring(0, 4);
        else
            fragment = name;
        int rand_length = 20 - fragment.length();
        Random random = new Random();
        String randomText = new BigInteger(130, random).toString(32);
        for (int i = 0; i < rand_length; i++)
            fragment += randomText.charAt(i);

        // open and test connection to firebase
        // see if firebase contains ID, if not
        this.id = fragment;
        this.longitude = lon;
        this.latitude = lat;
        this.game = game;
        this.name = name;
    }
    
    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public void setCoordinates(double lat, double lon) {
        this.longitude = lon;
        this.latitude = lat;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void turnOff() {
        this.isActive = false;
    }

    public void setTriggerRadius(float meters) {
        this.triggerRadiusInMeters = meters;
    }

    public float getTriggerRadius() {
        return this.triggerRadiusInMeters;
    }
}
