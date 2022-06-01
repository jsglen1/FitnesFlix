package com.example.ciclismoapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciclismoapp.fragments.InformacionFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class MapaActivity extends AppCompatActivity  implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;

    private NavigationBarView BarraNavegacionAbajo;

    private FusedLocationProviderClient mLocationClient;


    //lista de rutas
    public ArrayList<PolylineOptions> ListaDeRutas;
    public ArrayList<PolylineOptions> OrigenListaDeRutas;

    private static final int CodigoLocalizacionFine;
    private static final int CodigoRuta;
    private static final int Gps_rest_code;

    static {
        CodigoLocalizacionFine = 1111;
        CodigoRuta = 555;
        Gps_rest_code = 777;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        assert mapFragment != null;
        mapFragment.getMapAsync(this); // puede omitir


        BarraNavegacionAbajo = findViewById(R.id.bottom_navigation);


        BarraNavegacionAbajo.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.locationNVM:
                        //funcionUbicacionActual(mMap);
                        if (isGpsEnable()) {
                            GetCurrentLocation();
                        }
                        return true;

                    case R.id.routesNVM:
                        Intent i = new Intent(MapaActivity.this, MostrarRutasActivity.class);
                        startActivity(i);
                        return true;

                    case R.id.homeNVM:
                        Intent f = new Intent(MapaActivity.this, BienvenidaActivity.class);
                        startActivity(f);
                        return true;

                    case R.id.perfilNVM:
                        //direction();
                        return true;
                }
                return false;
            }
        });


        mapFragment.getMapAsync(this);
        getLocalizacion();

        mLocationClient = new FusedLocationProviderClient(this);

    }




    private void direction(float latI,float lonI,float latF,float lonF) {

        String destino = ""+latF+","+lonF+"";
        String origen = ""+latI+","+lonI+"";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("destination", destino) // la bahia
                .appendQueryParameter("origin", origen) // santa marta
                .appendQueryParameter("mode", "driving")
                .appendQueryParameter("key", "AIzaSyDZjWmDiE-IC5bZvS8YuqRuHQnIEbsllFM")
                .toString();
        Toast.makeText(this, " Direction "+url, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("OK")) {
                        JSONArray routes = response.getJSONArray("routes");

                        ArrayList<LatLng> points;
                        PolylineOptions polylineOptions = null;

                        for (int i = 0; i < routes.length(); i++) {
                            points = new ArrayList<>();
                            polylineOptions = new PolylineOptions();
                            JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");

                            for (int j = 0; j < legs.length(); j++) {
                                JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");

                                for (int k = 0; k < steps.length(); k++) {
                                    String polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points");
                                    List<LatLng> list = descodePoly(polyline);

                                    for (int l = 0; l < list.size(); l++) {
                                        LatLng position = new LatLng((list.get(l)).latitude, ((list.get(l))).longitude);
                                        points.add(position);
                                    }

                                }

                            }

                            polylineOptions.addAll(points);
                            polylineOptions.width(10);
                            polylineOptions.color(ContextCompat.getColor(MapaActivity.this, R.color.purple_500));
                            polylineOptions.geodesic(true);
                        }

                        mMap.addPolyline(polylineOptions);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(latI, lonI)).title("market origenn"));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(latF, lonF)).title("market destinon"));

                        LatLngBounds bounds = new LatLngBounds.Builder()
                                .include(new LatLng(latI, lonI))
                                .include(new LatLng(latF, lonF)).build();

                        Point point = new Point();
                        getWindowManager().getDefaultDisplay().getSize(point);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, point.x, 150, 30));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );

        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(jsonObjectRequest);

    }


    private List<LatLng> descodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b > 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);

            poly.add(p);
            Toast.makeText(this, "wtf + "+p.toString(), Toast.LENGTH_SHORT).show();

        }
        return poly;
    }


    private boolean isGpsEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean provideEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (provideEnable) {
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Gps Permisson")
                    .setMessage("Gps es requerido para que la app funcione correctamente, por favor activar")
                    .setPositiveButton("Si", (dialogInterface, which) -> {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(i, Gps_rest_code);
                    })
                    .setCancelable(false)
                    .show();
        }
        return false;
    }

    private void GetCurrentLocation() {
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
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                gotoLocation(location.getLatitude(), location.getLongitude());
            }
        });

    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // Display traffic.
        //mMap.setTrafficEnabled(true);

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                4000,
                null
        );


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
        //mMap.setTrafficEnabled(true);
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
        mMap.setMyLocationEnabled(true);

        String position = "";
        position = getIntent().getStringExtra("key");

        //Toast.makeText(this, "el dato pocision es" + position, Toast.LENGTH_SHORT).show();

        if (position != null) {
            int pos = Integer.parseInt(position);
            //  Toast.makeText(this, "mostrando localizacion de " + pos, Toast.LENGTH_SHORT).show();
            CordenadasDeTodasLasRutas(pos, mMap);
        } else {
            //crearMarcadorDefault(mMap);
            CordenadaSantaMartaInit(mMap);
            //funcionUbicacionActual(mMap);
        }

        /*
        MostrarRutasActivity mostrarRutasActivity = new MostrarRutasActivity();
        String name = mostrarRutasActivity.rutas.get(Integer.parseInt(position));
        Toast.makeText(getApplicationContext(),"has pulsado marcador"+name ,Toast.LENGTH_SHORT).show();
         */


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(getApplicationContext(),"has pulsado marcador" ,Toast.LENGTH_SHORT).show();


                // Create new fragment and transaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

                // Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.FragmenTdetalles, InformacionFragment.newInstance("",""));

                // Commit the transaction
                transaction.commit();



                return false;
            }
        });

    }

    public void CordenadaSantaMartaInit(@NonNull GoogleMap mMapa) {
        LatLng SantaMarta = new LatLng(11.239634, -74.211125);

        // Move the camera to the map coordinates and zoom in closer.
        mMapa.moveCamera(CameraUpdateFactory.newLatLng(SantaMarta));
        mMapa.moveCamera(CameraUpdateFactory.zoomTo(15));
        // Display traffic.
        //mMap.setTrafficEnabled(true);

        mMapa.animateCamera(
                CameraUpdateFactory.newLatLngZoom(SantaMarta, 15f),
                4000,
                null
        );
    }


    //lista de todas las rutas
    public void CordenadasDeTodasLasRutas(int position, @NonNull GoogleMap mMapa) {

        PolylineOptions RutaUnimag = new PolylineOptions()
                .add(new LatLng(11.225631, -74.188490))
                .add(new LatLng(11.222990, -74.188072))
                .add(new LatLng(11.224169, -74.184446))
                .width(10f)
                .color(ContextCompat.getColor(this, R.color.MenuBarraColor));


        PolylineOptions RutaMinca = new PolylineOptions()
                .add(new LatLng(11.220088, -74.159417))
                .add(new LatLng(11.203246, -74.161734))
                .add(new LatLng(11.190616, -74.152593))
                .add(new LatLng(11.177674, -74.147384))
                .add(new LatLng(11.166365, -74.142685))
                .add(new LatLng(11.157363, -74.134813))
                .add(new LatLng(11.150904, -74.124031))
                .add((new LatLng(11.144710, -74.119085)))
                .width(10f)
                .color(ContextCompat.getColor(this, R.color.MenuBarraColor));


        PolylineOptions RutaBahia = new PolylineOptions()
                .add(new LatLng(11.247504, -74.213773))
                .add(new LatLng(11.245115, -74.214331))
                .add(new LatLng(11.243189, -74.214975))
                .add(new LatLng(11.241489, -74.215637))
                .add(new LatLng(11.240280, -74.215845))
                .add(new LatLng(11.240143, -74.216306))
                .add(new LatLng(11.238796, -74.217014))
                .add(new LatLng(11.237954, -74.217647))
                .width(10f)
                .color(ContextCompat.getColor(this, R.color.MenuBarraColor));

        PolylineOptions RutaUnimagOrigen = new PolylineOptions()
                .add(new LatLng(11.225631, -74.188490))
                .width(10f)
                .color(ContextCompat.getColor(this, R.color.teal_200));

        ListaDeRutas = new ArrayList<>();
        OrigenListaDeRutas = new ArrayList<>();

        ListaDeRutas.add(RutaUnimag);
        ListaDeRutas.add(RutaMinca);
        ListaDeRutas.add(RutaBahia);

        OrigenListaDeRutas.add(RutaUnimagOrigen);

        for (int i = 0; i < ListaDeRutas.size(); i++) {
            if (i == position) {
                Polyline polyline = mMapa.addPolyline(ListaDeRutas.get(i));
                crearMarcadorDefault(mMapa, ListaDeRutas.get(i));
            }
        }


    }

    private void crearMarcadorDefault(@NonNull GoogleMap mMapa, PolylineOptions ListCoords) {
        // Set the map coordinates to Kyoto Japan.

        List<LatLng> points = ListCoords.getPoints();

        int IndexFinal = points.size() - 1;

        //Toast.makeText(this, "ListCoords tam -> "+IndexFinal, Toast.LENGTH_SHORT).show();
        // cordenadas de punto inicial
        float latitudI = (float) points.get(0).latitude;
        float longitudI = (float) points.get(0).longitude;

        // cordenadas de punto final
        float latitudF = (float) points.get(IndexFinal).latitude;
        float longitudF = (float) points.get(IndexFinal).longitude;

        direction(latitudI,longitudI,latitudF,longitudF);

        //marcador inicial
        LatLng SantaMartaI = new LatLng(latitudI, longitudI);
        // Set the map type to Hybrid.
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker on the map coordinates.
        mMapa.addMarker(new MarkerOptions()
                .position(SantaMartaI)
                .title("Punto Inicial")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        // Move the camera to the map coordinates and zoom in closer.
        mMapa.moveCamera(CameraUpdateFactory.newLatLng(SantaMartaI));
        mMapa.moveCamera(CameraUpdateFactory.zoomTo(15));
        // Display traffic.
        //mMap.setTrafficEnabled(true);

        mMapa.animateCamera(
                CameraUpdateFactory.newLatLngZoom(SantaMartaI, 18f),
                4000,
                null
        );

        // marcador final
        LatLng SantaMartaF = new LatLng(latitudF, longitudF);
        // Set the map type to Hybrid.
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Add a marker on the map coordinates.
        mMapa.addMarker(new MarkerOptions()
                .position(SantaMartaF)
                .title("Punto Final")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        // Move the camera to the map coordinates and zoom in closer.


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
                //mMap.setTrafficEnabled(true);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(miUbicacion)
                        .zoom(18f)
                        .bearing(90)
                        .tilt(45)
                        .build();
                  /*mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(miUbicacion,12f),
                        3000,
                  null); */

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


    public void MoverActividadEditarPerfil(View view) {
        Intent EditarPerfilActivity = new Intent(this, MostrarRutasActivity.class);
        startActivity(EditarPerfilActivity);
    }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gps_rest_code) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (providerEnable) {
                Toast.makeText(this, "GPS activado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS NO activado", Toast.LENGTH_SHORT).show();
            }
        }
    }


}