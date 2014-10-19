package OdysseiaGame;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import MapEntity.MapEntity;
import MapEntity.QuestEntity;
import util.Firebaser;

/**
 * Created by Brian on 10/11/2014.
 */
public class OdysseiaGame {
    private final String id;

    public OdysseiaGame () {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("name", "Default game name");

        id = Firebaser.storeNewGame(dataMap);
    }

    public void createQuestEntity(LatLng loc, String name) {
        QuestEntity quest = new QuestEntity(loc, name, this);

        String questid = Firebaser.storeMarker(id, quest, loc);
    }

    public void deleteGame() {
        Firebaser.deleteGame(id);
    }


}
