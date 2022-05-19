package com.example.ciclismoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class FitnetFlixActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitnet_flix);
        cargando();
    }

    TimerTask tarea = new TimerTask() {
        @Override
        public void run() {
            Intent i = new Intent(FitnetFlixActivity.this, BienvenidaActivity.class);
            startActivity(i);
            finish();
        }
    };


    private void cargando() {
        Timer tempos = new Timer();
        tempos.schedule(tarea,3000);
    }


}