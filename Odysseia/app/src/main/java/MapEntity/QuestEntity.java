package MapEntity;

import OdysseiaGame.OdysseiaGame;

/**
 * Created by Brian on 10/11/2014.
 */
public class QuestEntity extends MapEntity{

    public QuestEntity(double lat, double lon, String name, OdysseiaGame game) {
        super(lat, lon, name, game, MarkerType.QUEST);
    }
}
