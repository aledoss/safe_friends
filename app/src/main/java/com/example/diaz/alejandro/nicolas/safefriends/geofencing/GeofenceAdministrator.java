package com.example.diaz.alejandro.nicolas.safefriends.geofencing;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.diaz.alejandro.nicolas.safefriends.database.ParadaUser;
import com.example.diaz.alejandro.nicolas.safefriends.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenwe on 10/10/2016.
 */

public class GeofenceAdministrator extends Activity implements
                                                        GoogleApiClient.ConnectionCallbacks,
                                                        GoogleApiClient.OnConnectionFailedListener,
                                                        Constants {

    List<Geofence> mGeofenceList = new ArrayList<>();
    private PendingIntent mGeofenceRequestIntent;
    private GoogleApiClient mApiClient;
    private SimpleGeofence mGeofence;//dato estatico de prueba
    //private enum REQUEST_TYPE {ADD}  //enum Clase{Constante1,Constante2}
    //private REQUEST_TYPE mRequestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(GEOFENCINGADMINTAG, "GeofenceAdministrator On Create");
        if (!isMyServiceRunning(UpdateActualLocation.class)) {    //si no está corriendo el servicio, que lo inicie
            startService(new Intent(GeofenceAdministrator.this, UpdateActualLocation.class));  //Servicio para consultar dónde está el chabon
        }
        if (!isGooglePlayServicesAvailable()) {
            Log.e(GEOFENCINGADMINTAG, "Google Play services unavailable.");
            finish();
            return;
        }
        connectGoogleApiClient();
        //obtengo el dato de la parada pasado por Bundle para crearla
        Bundle extras = getIntent().getExtras();
        ParadaUser paradaUser = (ParadaUser) extras.getSerializable(GEOFENCEPARAM);
        createGeofences(paradaUser);

    }

    private void connectGoogleApiClient() {
        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mApiClient.connect();
    }

    //Disconnect
    protected void onStop() {
        Log.d(GEOFENCINGADMINTAG, "GeofenceAdministrator On Stop");
        mApiClient.disconnect();
        //Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    public void createGeofences(ParadaUser geofence) {
        //recorto la latitud y longitud para probar en el maps de genymotion, esto no afecta a la precisión
        Double latitud = Double.parseDouble(geofence.getLatitud().substring(0,12));
        Double longitud = Double.parseDouble(geofence.getLongitud().substring(0,12));
        mGeofence = new SimpleGeofence(
                String.valueOf(geofence.getId()),
                latitud,
                longitud,
                //Double.parseDouble(geofence.getLatitud()),
                //Double.parseDouble(geofence.getLongitud()),
                //-34.6033177,
                //-58.4428914,
                GEOFENCE_RADIUS_METERS,
                GEOFENCE_EXPIRATION_TIME,
                Geofence.GEOFENCE_TRANSITION_ENTER
        );
        mGeofenceList.add(mGeofence.toGeofence());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(GeofenceAdministrator.this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                Log.e(GEOFENCINGADMINTAG, "Excepcion al tratar de resolver el error en la conexion. ", e);
            }
        } else {
            int errorCode = connectionResult.getErrorCode();
            Log.e(GEOFENCINGADMINTAG, "Fallo en la conexion a Google Play Services " + errorCode);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //obtengo el pendingIntent para la geofence que se llamo y envio una peticion para agregar la geofence
        mGeofenceRequestIntent = getGeofenceTransitionPendingIntent();
        String accion = getIntent().getExtras().getString(ACCIONGEOFENCE);//obtengo el tipo de accion
        Log.d(GEOFENCINGTAG, "Accion: " + accion);
        /*if (accion.equalsIgnoreCase(REMOVERGEOFENCE)) {
            ArrayList<String> listaGeofences = new ArrayList<>();
            for(Geofence geofence: mGeofenceList){
                listaGeofences.add(geofence.getRequestId());
            }

            listaGeofences.add(mGeofenceList.get(0).getRequestId());
            LocationServices.GeofencingApi.removeGeofences(mApiClient, listaGeofences);
        }else{*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(GeofenceAdministrator.this, "Sin permisos", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    LocationServices.GeofencingApi.addGeofences(mApiClient, mGeofenceList,
                            mGeofenceRequestIntent);
                }
            }else{
                LocationServices.GeofencingApi.addGeofences(mApiClient, mGeofenceList,
                        mGeofenceRequestIntent);
            }
        Log.d(GEOFENCINGTAG, "id: " + mGeofenceList.get(0).getRequestId());
        Toast.makeText(GeofenceAdministrator.this, "Parada cargada", Toast.LENGTH_SHORT).show();
        //}
        Log.e(GEOFENCINGADMINTAG, "Geofencelist: " + mGeofenceList);
        //Toast.makeText(this, getString(R.string.start_geofence_service), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGeofenceRequestIntent != null) {
            LocationServices.GeofencingApi.removeGeofences(mApiClient, mGeofenceRequestIntent);
            Toast.makeText(this, "Conexion terminada", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            if (Log.isLoggable(GEOFENCINGADMINTAG, Log.DEBUG))
                Log.d(GEOFENCINGADMINTAG, "Google Play Services is available");
            return true;
        } else {
            Log.e(GEOFENCINGADMINTAG, "Google Play services is unavailable");
            return false;
        }
    }

    private PendingIntent getGeofenceTransitionPendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        Log.d(GEOFENCINGADMINTAG, "Pending Intent");
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
