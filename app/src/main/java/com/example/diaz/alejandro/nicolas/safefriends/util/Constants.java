package com.example.diaz.alejandro.nicolas.safefriends.util;

import com.google.android.gms.location.Geofence;

/**
 * Created by Lenwe on 26/09/2016.
 */
public interface Constants {
    String NOMBRE = "Nombre";
    String GRUPO = "Grupo";
    String TOKENUSER = "TokenUser";

    //DB
    String DATABASE = "SafeFriends";
    String PARADATABLE = "ParadaUser";
    String IDCOLUMNPARADA = "IdColumnParada";
    String NAMECOLUMNPARADA = "NameColumnParada";
    String NAMECOLUMNUSER = "NameColumnUser";
    String LATITUDCOLUMNPARADA = "LatitudColumnParada";
    String LONGITUDCOLUMNPARADA = "LongitudColumnParada";

    //JSON REQUEST
    String JSONTAG = "JsonTag";
    int TIMEOUT_MS = 4000;
    int MAX_RETRIES = 3;
    String KEY1 = "Content-Type";
    String KEY2 = "Authorization";
    String VALUE1 = "application/json";
    String VALUE2 = "key=AIzaSyDfJDr5cq_qhsUJ-qE0Mb_78qnafaSPZ1Y";
    String URL = "https://fcm.googleapis.com/fcm/send";

    //Geofencing
    String UPDATEACTUALLOCATIONTAG = "UpdateActuaLocation";
    String GEOFENCINGTAG = "GeofencingTag";
    String GEOFENCINGADMINTAG = "GeofencingAdminTag";
    float GEOFENCE_RADIUS_METERS = 40;   //40 metros
    long GEOFENCE_EXPIRATION_TIME = Geofence.NEVER_EXPIRE;
    int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    String GEOFENCEPARAM = "GeofenceParam";
    String ACCIONGEOFENCE = "AccionGeofence";
    String AGREGARGEOFENCE = "AgregarGeofence";
    String REMOVERGEOFENCE = "RemoverGeofence";

}