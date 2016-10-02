package com.example.diaz.alejandro.nicolas.safefriends;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.diaz.alejandro.nicolas.safefriends.database.DBHelper;
import com.example.diaz.alejandro.nicolas.safefriends.database.ParadaUser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        final LatLng latlngInicio = new LatLng(-34.5997038,-58.4458525);
        float zoomInicio = (float) 11.5;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngInicio,zoomInicio));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                //crear el dialog yes/no
                //Toast.makeText(MapsActivity.this, "alto touch", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MapsActivity.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Parada")
                        .setMessage("Agregar parada en este punto?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper db = new DBHelper(MapsActivity.this);
                                ParadaUser paradaUser = new ParadaUser();
                                paradaUser.setNameUser("Nombre usuario");//acá iria el nombre ingresado al principio
                                paradaUser.setNameParada("Parada test");//aca tendria que haber un edittext para ingresar el nombre que quiera a la parada
                                paradaUser.setLatitud(String.valueOf(latLng.latitude));
                                paradaUser.setLongitud(String.valueOf(latLng.longitude));
                                db.insertParadaUser(paradaUser);
                                db.close();
                                Toast.makeText(MapsActivity.this, "Parada agregada.", Toast.LENGTH_SHORT).show();
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

}
