package com.example.diaz.alejandro.nicolas.safefriends.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenwe on 10/10/2016.
 */

public class JsonRequest implements Constants {

    public void callJSON(String url, final JSONObject jsonNico, Context context, final VolleyCallback callback){
        //funcion que se pasa por parametro la url, un objecto json, el contexto de donde se llama la funcion, y un callback
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,jsonNico,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response.toString());
                        Log.d(JSONTAG,"Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("VolleyError", "Error: " + error.toString());
                String json;
                NetworkResponse response = error.networkResponse;
                if(response!=null && response.data != null){
                    switch(response.statusCode){
                        case 400:
                            json = new String(response.data);   //{"$id":"1","Message":"User does not exist or password wrong"}
                            Log.d(JSONTAG,"Json response: " + json);
                            json = trimMessage(json,"Message"); //obtengo el mensaje de respuesta del Json.
                            if(json!=null){
                                //Log.d(TAGJSON,"Status code 400: " + json);
                                callback.onErrorResponse(json);
                            }
                        case 401:
                            callback.onErrorResponse("Unauthorized error");
                    }
                }
            }
        })
        {
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY1, VALUE1);
                params.put(KEY2, VALUE2);
                return params;
            }
        }
                ;
        //Politica de "reconexion"
        jsObjRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        TIMEOUT_MS,
                        MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
        //queue.start();
    }

    public String trimMessage (String json, String key){
        String trimmedString = null;
        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }

}
