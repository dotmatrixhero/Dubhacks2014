package MapEntity;

import OdysseiaGame.OdysseiaGame;

/**
 * Created by Christina on 10/18/2014.
 */
public class ItemEntity extends MapEntity {

    public ItemEntity(double lat, double lon, String name, OdysseiaGame game) {
        super(lat, lon, name, game, MarkerType.ITEM);
    }
}
