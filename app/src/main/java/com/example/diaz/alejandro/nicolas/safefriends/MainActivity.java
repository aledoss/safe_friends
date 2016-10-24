package com.example.diaz.alejandro.nicolas.safefriends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diaz.alejandro.nicolas.safefriends.database.DBHelper;
import com.example.diaz.alejandro.nicolas.safefriends.database.ParadaUser;
import com.example.diaz.alejandro.nicolas.safefriends.util.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Constants {

    private Button btnIngresar, btnEnviarNotif;
    private EditText etNombre, etGrupo;
    //private TextView tvToken;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //btnEnviarNotif = (Button) findViewById(R.id.btnEnviarNotif);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etGrupo = (EditText) findViewById(R.id.etGrupo);
        //tvToken = (TextView) findViewById(R.id.tvToken);

        context = this;
        //obtengo el token
        /*try {
            tvToken.setText(FirebaseInstanceId.getInstance().getToken().toString());
            Log.d("TOKEN", "Token: " + FirebaseInstanceId.getInstance().getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNombre.getText().toString().equalsIgnoreCase("") || etGrupo.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Completar nombre/grupo", Toast.LENGTH_SHORT).show();
                } else {
                    //me meto en el grupo.
                    //FirebaseMessaging.getInstance().subscribeToTopic(etGrupo.getText().toString());
                    FirebaseMessaging.getInstance().subscribeToTopic(etGrupo.getText().toString());
                    Intent intent = new Intent(MainActivity.this, ListaParadas.class);
                    intent.putExtra(NOMBRE, etNombre.getText().toString());
                    cargarGrupo();
                    startActivity(intent);
                }
            }
        });


        /*btnEnviarNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(context);
                ArrayList<ParadaUser> listaParadaUser = dbHelper.getAllParadaUser();
                if (listaParadaUser.get(0) == null) {
                    Toast.makeText(MainActivity.this, "No hay usuarios cargados", Toast.LENGTH_SHORT).show();
                } else {
                    //agrego todas las geofences cargadas, ya que se borran
                    /*Intent intent = new Intent(MainActivity.this, GeofenceAdministrator.class);
                    intent.putExtra(ACCIONGEOFENCE, AGREGARGEOFENCE);
                    intent.putExtra(GEOFENCEPARAM, (Serializable) (listaParadaUser.get(0)));
                    Log.d("NICOTEST", listaParadaUser.get(0).getLatitud() + " longitud: " + listaParadaUser.get(0).getLongitud());
                    startActivity(intent);*/
                    /*JsonRequest jsonRequest = new JsonRequest();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("to", "/topics/Test");
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("body", "test");
                        jsonObject1.put("title", "title");
                        jsonObject.put("notification", jsonObject1);
                        Log.d(JSONTAG, jsonObject.toString());
                        jsonRequest.callJSON(URL, jsonObject, context, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                Toast.makeText(MainActivity.this, "result", Toast.LENGTH_SHORT).show();
                                Log.d(JSONTAG, result);
                            }

                            @Override
                            public void onErrorResponse(String result) {
                                Toast.makeText(MainActivity.this, "result", Toast.LENGTH_SHORT).show();
                                Log.d(JSONTAG, result);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

    }

    private void cargarGrupo() {
        try {
            SharedPreferences pref = getSharedPreferences(GRUPO, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(GRUPO, etGrupo.getText().toString());
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
