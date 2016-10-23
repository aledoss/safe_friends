package com.example.diaz.alejandro.nicolas.safefriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.example.diaz.alejandro.nicolas.safefriends.database.DBHelper;
import com.example.diaz.alejandro.nicolas.safefriends.database.ParadaUser;
import com.example.diaz.alejandro.nicolas.safefriends.util.AdapterListaParadas;

import java.util.ArrayList;

import static com.example.diaz.alejandro.nicolas.safefriends.util.Constants.NOMBRE;

public class ListaParadas extends AppCompatActivity {

    private Button btnMapa;
    private Toolbar toolbar;
    private ListView listViewParadas;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_paradas);
        toolbar = (Toolbar) findViewById(R.id.toolbarListaParadas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lista de Paradas");
        nombre = obtenerDatosIntent();
    }

    @Override
    protected void onResume() {
        listViewParadas = (ListView) findViewById(R.id.listViewParadas);
        DBHelper db = new DBHelper(this);
        ArrayList<ParadaUser> listaParadas = new ArrayList<>();
        for(ParadaUser parada: db.getAllParadaUser()){  //aca el db.get tendria que ser al especifico del usuario logueado
            listaParadas.add(parada);
            Log.d("Listaparadas", "Parada: ID: "+ parada.getId() + ",NombreParada: " + parada.getNameParada() + ", NombreUser: " + parada.getNameUser() + ", latitud: " +
                    parada.getLatitud() + ", longitud: " + parada.getLongitud());
        }
        AdapterListaParadas adapterListaParadas = new AdapterListaParadas(this, listaParadas);
        listViewParadas.setAdapter(adapterListaParadas);
        db.close();

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.agregarParada:
                Intent intent = new Intent(ListaParadas.this, MapsActivity.class);
                intent.putExtra(NOMBRE, nombre);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private String obtenerDatosIntent() {
        try{
            return nombre = getIntent().getExtras().getString(NOMBRE);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
