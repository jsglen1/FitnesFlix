package com.example.ciclismoapp;

import static java.lang.System.exit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Array;
import java.util.Arrays;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;

    private static final int CodigoLocalizacionFine = 1111;

    private static final int CodigoLolizacionCoarse = 2222;


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
        crearMarcador();
        //mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
        mMap.setOnMyLocationClickListener(this);
        EnableLocation();

    }

    private void crearMarcador() {
        // Set the map coordinates to Kyoto Japan.
        LatLng SantaMarta = new LatLng(11.233, -74.2);
        // Set the map type to Hybrid.
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker on the map coordinates.
        mMap.addMarker(new MarkerOptions()
                .position(SantaMarta)
                .title("Santa Marta"));
        // Move the camera to the map coordinates and zoom in closer.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SantaMarta));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // Display traffic.
        mMap.setTrafficEnabled(true);

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(SantaMarta, 18f),
                4000,
                null
        );
    }

    public void MoverActividadInciarSeccion(View view) {
        Intent InciarSeccion = new Intent(this, IniciarSeccionActivity.class);
        startActivity(InciarSeccion);
    }

    // logica de permisos de usuario geolocalizacion

    private boolean isLocationPermisionGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);
    }

    private void EnableLocation() {
        if (!mMap.isBuildingsEnabled()) {
            //error
            exit(-1);
        }
        if (isLocationPermisionGranted()) {
            boolean enabled = mMap.isMyLocationEnabled();
            enabled = true; // ojo aqui = true

        }else{
            RequestLocationPermision();
        }

    }

    private void RequestLocationPermision() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            // permisos denegados
            Toast.makeText(this,"Acepta Los Permisos En Los Ajustes De La Aplicacion", Toast.LENGTH_SHORT).show();
        }else{
            // pregunta permisos incialmente
            ActivityCompat.requestPermissions(this,  new String[]{(Manifest.permission.ACCESS_FINE_LOCATION)}  ,CodigoLocalizacionFine );
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CodigoLocalizacionFine){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permisos Aceptados", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this,"Permisos Denegados", Toast.LENGTH_SHORT).show();
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) ) {
                    Toast.makeText(this,"Nuevo Intento De Permisos", Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(this).setMessage("Necesitas Aceptar Los Permisos")
                            .setPositiveButton("Intentalo Nuevamente", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(MapaActivity.this,  new String[]{(Manifest.permission.ACCESS_FINE_LOCATION)}  ,CodigoLocalizacionFine );
                                }
                            })
                            .setNegativeButton("No Gracias", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //no hacer nada
                                }
                            }).show();
                }else{
                    Toast.makeText(this,"debes configurar los permisos manualmente", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
}