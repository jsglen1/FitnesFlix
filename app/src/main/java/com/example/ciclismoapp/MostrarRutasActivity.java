package com.example.ciclismoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MostrarRutasActivity extends AppCompatActivity {

    private ListView listView;

    private ArrayList<String> rutas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_rutas);

        listView = findViewById(R.id.ListViuwMR);
        rutas = new ArrayList<>();

        rutas.add("Bonda");
        rutas.add("Rodadero");
        rutas.add("Bahia");
        rutas.add("Playa Blanca");
        rutas.add("Minca");
        rutas.add("Ciudadela");
        rutas.add("parque Los Novios");
        rutas.add("La Unimag");
        rutas.add("ruta 1");
        rutas.add("ruta 2");
        rutas.add("ruta 3");
        rutas.add("ruta 4");
        rutas.add("ruta 5");
        rutas.add("ruta 6");
        rutas.add("ruta 7");
        rutas.add("ruta 8");
        rutas.add("ruta 9");
        rutas.add("ruta 10");
        rutas.add("ruta 11");
        rutas.add("ruta 12");
        rutas.add("ruta 13");
        rutas.add("ruta 14");
        rutas.add("ruta 15");

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,rutas);

        listView.setAdapter(adapter);

    }

    public void MoverActividadMapa(View view) {
        Intent ActividadMapa = new Intent(this, MapaActivity.class);
        startActivity(ActividadMapa);
    }



}