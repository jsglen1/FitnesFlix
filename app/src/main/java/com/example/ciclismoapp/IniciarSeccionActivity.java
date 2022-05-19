package com.example.ciclismoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class IniciarSeccionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_seccion);
    }

    public void MoverActividadRegistrar(View view) {
        Intent ActividadRegistrar = new Intent(this, RegistrarseActivity.class);
        startActivity(ActividadRegistrar);
    }

    public void MoverActividadEditarPerfil(View view) {
        Intent EditarPerfilActivity = new Intent(this, FitnetFlixActivity.class);
        startActivity(EditarPerfilActivity);
    }
}