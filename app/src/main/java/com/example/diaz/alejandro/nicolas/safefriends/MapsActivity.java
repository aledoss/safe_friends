package com.example.diaz.alejandro.nicolas.safefriends;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diaz.alejandro.nicolas.safefriends.database.DBHelper;
import com.example.diaz.alejandro.nicolas.safefriends.database.ParadaUser;
import com.example.diaz.alejandro.nicolas.safefriends.geofencing.GeofenceAdministrator;
import com.example.diaz.alejandro.nicolas.safefriends.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Constants {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng latlngInicio = new LatLng(-34.5997038, -58.4458525);
        float zoomInicio = (float) 11.5;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngInicio, zoomInicio));
        cargarParadas(googleMap);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                //crear el dialog yes/no
                final EditText input = new EditText(MapsActivity.this);
                input.setText("Nueva parada");
                new AlertDialog.Builder(MapsActivity.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Parada")
                        .setView(input)
                        .setMessage("Agregar parada en este punto?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper db = new DBHelper(MapsActivity.this);
                                ParadaUser paradaUser = new ParadaUser();
                                paradaUser.setNameUser("Nombre usuario");//acá iria el nombre ingresado al principio
                                paradaUser.setNameParada(input.getText().toString());
                                paradaUser.setLatitud(String.valueOf(latLng.latitude));
                                paradaUser.setLongitud(String.valueOf(latLng.longitude));
                                //inserto en la bd local la parada
                                db.insertParadaUser(paradaUser);
                                //Mando el intent para crear la Geofence (parada) para que me avise cuando pase por esas coordenadas
                                Intent intent = new Intent(MapsActivity.this, GeofenceAdministrator.class);
                                //intent.putExtra(ACCIONGEOFENCE, AGREGARGEOFENCE);
                                intent.putExtra(GEOFENCEPARAM, (Serializable) paradaUser);
                                //Log.d("NICOTEST", listaParadaUser.get(0).getLatitud() + " longitud: " + listaParadaUser.get(0).getLongitud());
                                startActivity(intent);


                                db.close();
                                //Toast.makeText(MapsActivity.this, "Parada agregada.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MapsActivity.this, "No", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    private void cargarParadas(GoogleMap googleMap) {
        try {
            DBHelper db = new DBHelper(MapsActivity.this);
            ArrayList<ParadaUser> listaParadas = db.getAllParadaUser();
            LatLng parqueLatLng;
            for (ParadaUser parada : listaParadas) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 65, 65, false);
                parqueLatLng = new LatLng(Double.parseDouble(parada.getLatitud()), Double.parseDouble(parada.getLongitud()));
                googleMap.addMarker(new MarkerOptions()
                        .title(parada.getNameParada())
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .position(parqueLatLng)
                );
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
