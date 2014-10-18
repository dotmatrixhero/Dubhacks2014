package OdysseiaGame;

import com.firebase.client.Firebase;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import MapEntity.QuestEntity;
import util.Firebaser;

/**
 * Created by Brian on 10/11/2014.
 */
public class OdysseiaGame {
    private String id;


    public OdysseiaGame () {
        Random random = new Random();
        String randomText = new BigInteger(130, random).toString(32);
        String fragment = "game";
        for (int i = 0; i < 16; i++)
            fragment += randomText.charAt(i);
        this.id = fragment;

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("name", "Default game name");

        Firebaser.store(id, dataMap);
    }

    public void createQuest(double lat, double lon, String name) {
        QuestEntity quest = new QuestEntity(lat, lon, name, this);

    }
}
