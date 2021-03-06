package com.example.diaz.alejandro.nicolas.safefriends.geofencing;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.diaz.alejandro.nicolas.safefriends.R;
import com.example.diaz.alejandro.nicolas.safefriends.database.DBHelper;
import com.example.diaz.alejandro.nicolas.safefriends.database.ParadaUser;
import com.example.diaz.alejandro.nicolas.safefriends.util.Constants;
import com.example.diaz.alejandro.nicolas.safefriends.util.JsonRequest;
import com.example.diaz.alejandro.nicolas.safefriends.util.VolleyCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Lenwe on 10/10/2016.
 */

public class GeofenceTransitionsIntentService extends IntentService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Constants {

    private GoogleApiClient mGoogleApiClient;

    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //showToast(this,"OnCreateTransitionIntentService");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        showToast(this, "OnConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        //showToast(this,"OnConnectionSuspended");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(GEOFENCINGTAG, "OnHandleIntent..");
        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
            Log.e(GEOFENCINGTAG, "Location Services error: " + errorCode);
        } else {//si no tira error
            int transitionType = geoFenceEvent.getGeofenceTransition();
            Log.e(GEOFENCINGTAG, "Transition Type: " + transitionType);
            if (Geofence.GEOFENCE_TRANSITION_ENTER == transitionType) {
                //Para obtener la lista:
                List triggeredGeoFenceId = geoFenceEvent.getTriggeringGeofences();
                List<ParadaUser> geofenceTransitionDetails = getGeofenceTransitionDetails(
                        this,
                        triggeredGeoFenceId
                );
                Log.e(GEOFENCINGTAG, "Triggered Geo Fence Event: " + triggeredGeoFenceId);
                //Log.i(GEOFENCINGTAG, geofenceTransitionDetails);
                //showToast(this,R.string.entering_geofence);
                //Geofence geofence = (Geofence) triggeredGeoFenceId.get(0);
                //showToast(this,geofence.getRequestId().toString());
                showNotifToTopic(geofenceTransitionDetails);
                //showNotif(geofenceTransitionDetails);
                //Toast.makeText(GeofenceTransitionsIntentService.this, getString(R.string.entering_geofence), Toast.LENGTH_SHORT).show();
            } else if (Geofence.GEOFENCE_TRANSITION_EXIT == transitionType) {
                //showToast(this, R.string.exiting_geofence);
            }
        }
    }

    private void showNotifToTopic(List<ParadaUser> geoFenceId) {
        try {
            String grupo = obtenerGrupo();
            JsonRequest jsonRequest = new JsonRequest();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("to", "/topics/" + grupo);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("body", "Llegando a " + geoFenceId.get(0).getNameParada());
            jsonObject1.put("title", geoFenceId.get(0).getNameUser());
            jsonObject.put("notification", jsonObject1);
            Log.d(JSONTAG, jsonObject.toString());
            jsonRequest.callJSON(URL, jsonObject, getApplicationContext(), new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    Log.d(JSONTAG, result);
                    showToast(GeofenceTransitionsIntentService.this, "Se envió una notitficacion a los miembros de tu grupo");
                }

                @Override
                public void onErrorResponse(String result) {
                    Log.d(JSONTAG, result);
                    showToast(GeofenceTransitionsIntentService.this, "Error al  una notitficacion a los miembros de tu grupo");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String obtenerGrupo() {
        SharedPreferences prefs = getSharedPreferences(GRUPO, Context.MODE_PRIVATE);
        String grupo = prefs.getString(GRUPO, "");
        return grupo;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showToast(final Context context, final int resourceId) {
        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(final Context context, final String resourceId) {
        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, resourceId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<ParadaUser> getGeofenceTransitionDetails(Context context, List<Geofence> triggeredGeoFences) {
        DBHelper db = new DBHelper(context);
        ArrayList<ParadaUser> listaDBGeofences = db.getAllParadaUser();
        ArrayList<ParadaUser> listaGeofencesAccedidas = new ArrayList<>();

        for (Geofence geofence : triggeredGeoFences) {
            for (int i = 0; i < listaDBGeofences.size(); i++) {
                if (Integer.parseInt(geofence.getRequestId()) == listaDBGeofences.get(i).getId()) {
                    listaGeofencesAccedidas.add(listaDBGeofences.get(i));
                }
            }
        }
        db.close();
        return listaGeofencesAccedidas;
    }
}