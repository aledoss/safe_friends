package com.example.diaz.alejandro.nicolas.safefriends;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Lenwe on 26/09/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Token registration
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: " + refreshedToken);
    }

    private void sendRegistrationToServer(String token){
        //por si hacemos un server, para enviarlo
    }
}
