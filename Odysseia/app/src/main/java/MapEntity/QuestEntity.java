package MapEntity;

import com.google.android.gms.maps.model.LatLng;

import OdysseiaGame.OdysseiaGame;

/**
 * Created by Brian on 10/11/2014.
 */
public class QuestEntity extends MapEntity{

    public QuestEntity(LatLng loc, String name, OdysseiaGame game) {
        super(loc.latitude, loc.longitude, name, game, MarkerType.QUEST);
    }
}
