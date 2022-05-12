package com.example.ciclismoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrarseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
    }

    public void MoverActividadInciarSeccion(View view) {
        Intent InciarSeccion = new Intent(this, IniciarSeccionActivity.class);
        startActivity(InciarSeccion);
    }

    public void MoverActividadMapa(View view) {
        Intent ActividadMapa = new Intent(this, MapaActivity.class);
        startActivity(ActividadMapa);
    }
}