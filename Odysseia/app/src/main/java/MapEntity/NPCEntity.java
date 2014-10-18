package MapEntity;

import OdysseiaGame.OdysseiaGame;

/**
 * Created by Christina on 10/18/2014.
 */
public class NPCEntity extends MapEntity {

    public NPCEntity(double lat, double lon, String name, OdysseiaGame game) {
        super(lat, lon, name, game, MarkerType.NPC);
    }
}
