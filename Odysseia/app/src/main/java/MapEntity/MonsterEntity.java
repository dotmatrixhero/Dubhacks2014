package MapEntity;

import OdysseiaGame.OdysseiaGame;

/**
 * Created by Christina on 10/18/2014.
 */
public class MonsterEntity extends MapEntity {

    public MonsterEntity(double lat, double lon, String name, OdysseiaGame game) {
        super(lat, lon, name, game, MarkerType.MONSTER);
    }
}
