package com.example.ciclismoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MostrarRutasActivity extends AppCompatActivity {

    public ListView listView;

    public ArrayList<String> rutas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_rutas);

        listView = findViewById(R.id.ListViuwMR);
        rutas = new ArrayList<>();

        //12 rutas identificadores 0 - 11
        rutas.add("La Unimagdalena");
        rutas.add("Rodadero");
        rutas.add("Bahia");
        rutas.add("Playa Blanca");
        rutas.add("Minca");
        rutas.add("Ciudadela");
        rutas.add("Parque Los Novios");
        rutas.add("Bonda");
        rutas.add("Palomino");
        rutas.add("Cienega");
        rutas.add("Rio Frio");
        rutas.add("Don Jaca");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rutas);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MostrarRutasActivity.this, "Has pulsado: " + rutas.get(position), Toast.LENGTH_LONG).show();
                Toast.makeText(MostrarRutasActivity.this, "Pocision : " + position, Toast.LENGTH_LONG).show();

                //cambiar de vista ojo
                Intent i = new Intent(MostrarRutasActivity.this, MapaActivity.class);
                i.putExtra("key", position + "");
                startActivity(i);

            }
        });


    }

    public void MoverActividadMapa(View view) {
        Intent ActividadMapa = new Intent(this, MapaActivity.class);
        startActivity(ActividadMapa);
    }


}