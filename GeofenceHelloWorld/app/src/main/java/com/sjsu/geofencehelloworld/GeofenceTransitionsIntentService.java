package com.sjsu.geofencehelloworld;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceTransitionsIntentService extends IntentService {

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()) return;
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.btn_plus)
                            .setContentTitle("SEND WECHAT MESSAGE")
                            .setContentText("SEND WECHAT MESSAGE");
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
            notificationManager.notify(001, notificationBuilder.build());
        }
    }

}
