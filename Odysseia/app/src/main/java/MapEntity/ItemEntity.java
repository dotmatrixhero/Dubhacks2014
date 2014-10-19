package MapEntity;

import com.google.android.gms.maps.model.LatLng;

import OdysseiaGame.OdysseiaGame;

/**
 * Created by Christina on 10/18/2014.
 */
public class ItemEntity extends MapEntity {

    public ItemEntity(LatLng loc, String name, OdysseiaGame game) {
        super(loc.latitude, loc.longitude, name, game, MarkerType.ITEM);
    }
}
