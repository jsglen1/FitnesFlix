package com.example.ciclismoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BienvenidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
    }

    public  void MoverInicioSeccion(View view){
        Intent I = new Intent(this,IniciarSeccionActivity.class);
        startActivity(I);
    }
}