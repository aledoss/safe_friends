package com.example.diaz.alejandro.nicolas.safefriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diaz.alejandro.nicolas.safefriends.util.Constants;

public class MainActivity extends AppCompatActivity implements Constants {

    private Button btnIngresar, btnEnviarNotif;
    private EditText etNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnviarNotif = (Button) findViewById(R.id.btnEnviarNotif);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        etNombre= (EditText) findViewById(R.id.etNombre);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombre.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(MainActivity.this, "Completar nombre", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, ListaParadas.class);
                    intent.putExtra(NOMBRE, etNombre.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }
}
