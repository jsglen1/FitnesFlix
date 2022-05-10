package com.example.ciclismoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("soy la monda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
         */


        // Set the map coordinates to Kyoto Japan.
        LatLng kyoto = new LatLng(35.00116, 135.7681);
        // Set the map type to Hybrid.
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker on the map coordinates.
        mMap.addMarker(new MarkerOptions()
                .position(kyoto)
                .title("Kyoto"));
        // Move the camera to the map coordinates and zoom in closer.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kyoto));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // Display traffic.
        mMap.setTrafficEnabled(true);
    }

    public void MoverActividadInciarSeccion(View view){
        Intent InciarSeccion = new Intent(this,IniciarSeccionActivity.class);
        startActivity(InciarSeccion);
    }


}