package com.example.ciclismoapp;

import static java.lang.System.exit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;

    private static final int CodigoLocalizacionFine;

    static {
        CodigoLocalizacionFine = 1111;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        getLocalizacion();
    }

    private void getLocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this).setMessage("Necesitas Aceptar Los Permisos")
                        .setPositiveButton("Intentalo Nuevamente", (dialogInterface, i) -> ActivityCompat.requestPermissions(MapaActivity.this, new String[]{(Manifest.permission.ACCESS_FINE_LOCATION)}, CodigoLocalizacionFine))
                        .setNegativeButton("No Gracias", (dialogInterface, i) -> {
                            //no hacer nada
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Toast.makeText(this, "debes configurar los permisos manualmente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        /*mMap = googleMap;
        crearMarcador();
        mMap.setOnMyLocationClickListener(this);
        EnableLocation();*/
        funcionUbicacionActual(mMap);
    }

    private void funcionUbicacionActual(@NonNull GoogleMap mMapa) {
        mMap = mMapa;
       // mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //EnableLocation();
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        LocationManager locationManager = (LocationManager) MapaActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(miUbicacion).title("ubicacion actual"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                mMap.setTrafficEnabled(true);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(miUbicacion)
                        .zoom(18f)
                        .bearing(90)
                        .tilt(45)
                        .build();
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(miUbicacion, 18f),
                        4000,
                        null);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public void MoverActividadInciarSeccion(View view) {
        Intent InciarSeccion = new Intent(this, IniciarSeccionActivity.class);
        startActivity(InciarSeccion);
    }

    // logica de permisos de usuario geolocalizacion

    /*

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




    private boolean isLocationPermissionGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED);
    }



    private void EnableLocation() {
        if (!mMap.isBuildingsEnabled()) {
            Toast.makeText(this, "Error fatal!", Toast.LENGTH_SHORT).show();
            //error
            exit(-1);
        }
        if (isLocationPermissionGranted()) {
            /*boolean enabled = true ;// ojo aqui = true
            enabled  = mMap.isMyLocationEnabled();

            Toast.makeText(this, "activado", Toast.LENGTH_SHORT).show();
        } else {
            RequestLocationPermission();
            Toast.makeText(this, "Existencia", Toast.LENGTH_SHORT).show();
        }
    }
    */

    /*
    private void RequestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // permisos denegados
            Toast.makeText(this, "Acepta Los Permisos En Los Ajustes De La Aplicacion", Toast.LENGTH_SHORT).show();
        } else {
            // pregunta permisos incialmente
            ActivityCompat.requestPermissions(this, new String[]{(Manifest.permission.ACCESS_FINE_LOCATION)}, CodigoLocalizacionFine);
        }
    }

     */


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CodigoLocalizacionFine) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos Aceptados Final", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Permisos Denegados Final", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //Toast.makeText(this, "Nuevo Intento De Permisos", Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(this).setMessage("Necesitas Aceptar Los Permisos Final")
                            .setPositiveButton("Intentalo Nuevamente", (dialogInterface, i) -> ActivityCompat.requestPermissions(MapaActivity.this, new String[]{(Manifest.permission.ACCESS_FINE_LOCATION)}, CodigoLocalizacionFine))
                            .setNegativeButton("No Gracias", (dialogInterface, i) -> {
                                //no hacer nada
                            }).show();
                } else {
                    Toast.makeText(this, "debes configurar los permisos manualmente", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }


}