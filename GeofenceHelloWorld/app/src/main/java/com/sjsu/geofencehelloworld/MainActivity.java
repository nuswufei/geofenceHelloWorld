package com.sjsu.geofencehelloworld;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    final static String GEOFENCE_REQUEST_ID = "com.sjsu.geofencehelloworld.requestID";
    final static double HOME_LAT = 37.280026;
    final static double HOME_LNT = -121.938395;
    final static float CIRCULARRADIUS = 50f;
    private GoogleApiClient googleApiClient;
    private List<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geofenceList = new ArrayList<Geofence>();
        buildGoogleApiClient();
        buildGeofenceList();
    }

    protected void buildGeofenceList() {
        geofenceList.add(new Geofence.Builder()
                .setRequestId(GEOFENCE_REQUEST_ID)
                .setCircularRegion(HOME_LAT, HOME_LNT, CIRCULARRADIUS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build());
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }
    public void startAlert(View view) {
        TextView textview = (TextView) findViewById(R.id.textview);
        if(!googleApiClient.isConnected()) {
            textview.setText("not conected");
        }
        try{
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this);
            textview.setText("alarm is on");
        }catch(Exception e) {}
    }

    public void stopAlert(View view) {
        TextView textview = (TextView) findViewById(R.id.textview);
        if(!googleApiClient.isConnected()) {
            textview.setText("not conected");
        }
        try {
            LocationServices.GeofencingApi.removeGeofences(
                    googleApiClient,
                    getGeofencePendingIntent()
            ).setResultCallback(this);
            textview.setText("alarm is off");
        }catch (Exception e) {}
    }

    private PendingIntent getGeofencePendingIntent() {
        if(geofencePendingIntent != null) return geofencePendingIntent;
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    protected void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onResult(Status status) {
        TextView textview = (TextView) findViewById(R.id.textview);
        if(status.isSuccess()) {

            textview.setText("done");
        }
        else {
            textview.setText(status.getStatusCode());
        }
    }
}
