package com.ragtag.Odysseia;
 import android.app.Activity;
 import android.app.Notification;
 import android.app.NotificationManager;
 import android.app.PendingIntent;
 import android.content.Context;
 import android.content.Intent;
 import android.location.LocationManager;
 import android.net.Uri;
 import android.os.Bundle;
 import android.support.v4.app.NotificationCompat;
 import android.widget.Toast;
 import android.util.Log;
public class ProximityActivity extends Activity {

    String notificationTitle;
    String notificationContent;
    String tickerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.v("DID I?", "creat");
        boolean proximity_entering = getIntent().getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, true);

        if(proximity_entering){
            Toast.makeText(getBaseContext(),"Entering the region"  ,Toast.LENGTH_LONG).show();
            notificationTitle="Proximity - Entry";
            notificationContent="Entered the region";
            tickerMessage = "Entered the region";
        }else{
            Toast.makeText(getBaseContext(),"Exiting the region"  ,Toast.LENGTH_LONG).show();
            notificationTitle="Proximity - Exit";
            notificationContent="Exited the region";
            tickerMessage = "Exited the region";
        }

        ProximityAlertNotification.notify(this, "Quest Objective Reached!", 1);

        /** Finishes the execution of this activity */
        finish();
    }
}