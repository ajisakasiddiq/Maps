package com.example.maps;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.EncodedPolyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Inisialisasi MapView dan aktifkan lifecycle-nya
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            String asal = intent.getStringExtra("Asal");
            String tuju = intent.getStringExtra("Tuju");


            new GeocodingTask().execute(asal, tuju);
        }
        // Inisialisasi GoogleMap
        mapView.getMapAsync(this);

        // Inisialisasi FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Minta izin akses lokasi pengguna
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Jika izin telah diberikan, tampilkan lokasi saat ini pada peta
//            fusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
//                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                                googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Lokasi saya"));
//                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
//                            }
//                        }
//                    });
            showLastKnownLocation();
        } else {
            // Jika izin belum diberikan, minta izin kepada pengguna
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika pengguna memberikan izin, tampilkan lokasi saat ini pada peta
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    fusedLocationProviderClient.getLastLocation()
//                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                                @Override
//                                public void onSuccess(Location location) {
//                                    if (location != null) {
//                                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                                        googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Lokasi saya"));
//                                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
//                                    }
//                                }
//                            });
                    showLastKnownLocation();
                }
            } else {
                // Jika pengguna menolak izin, tampilkan pesan peringatan
                Toast.makeText(MapsActivity.this, "Izin akses lokasi ditolak", Toast.LENGTH_SHORT).show();
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void showLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Jika izin telah diberikan, tampilkan lokasi saat ini pada peta
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                                googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Lokasi saya"));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                                Toast.makeText(getBaseContext(), "Location changed : Lat: "+location.getLatitude()+""+"\n Lng: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Lokasi saya"));
                                    // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                                    Toast.makeText(getBaseContext(), "Location changed : Lat: "+location.getLatitude()+""+"\n Lng: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        handler.postDelayed(this, 10000);
                    } else {
                        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
            };
            handler.postDelayed(runnable, 10000);
        } else {
            // Jika izin belum diberikan, minta izin kepada pengguna
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GeocodingTask extends AsyncTask<String, Void, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(String... params) {
            List<LatLng> latLngList = new ArrayList<>();
            for (String address : params) {
                LatLng latLng = getLatLngFromAddress(address);
                if (latLng != null) {
                    latLngList.add(latLng);
                }
            }
            return latLngList;
        }

        private List<LatLng> getDirectionsDetails(LatLng origin, LatLng destination) {
            try {
                // Menggunakan Google Maps Directions API untuk mendapatkan jalur
                GeoApiContext context = new GeoApiContext.Builder()
                        .apiKey("AIzaSyDXWk4C2wtezVlkhzB6-ZiZxXLmBaPQRqA")
                        .build();

                DirectionsApiRequest request = DirectionsApi.newRequest(context)
                        .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                        .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude));

                DirectionsResult results = request.await();

                // Mendapatkan polyline dari hasil Directions API
                DirectionsRoute route = results.routes[0];
                EncodedPolyline encodedPolyline = route.overviewPolyline;
                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(encodedPolyline.getEncodedPath());

                // Menyimpan koordinat dalam format LatLng
                List<LatLng> newDecodedPath = new ArrayList<>();
                for (com.google.maps.model.LatLng latLng : decodedPath) {
                    newDecodedPath.add(new LatLng(latLng.lat, latLng.lng));
                }

                return newDecodedPath;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // Panggil metode ini dalam onPostExecute
        protected void onPostExecute(List<LatLng> latLngList) {
            if (latLngList != null && latLngList.size() == 2) {
                LatLng asalLatLng = latLngList.get(0);
                LatLng tujuLatLng = latLngList.get(1);

                googleMap.addMarker(new MarkerOptions().position(asalLatLng).title("Lokasi Asal"));
                googleMap.addMarker(new MarkerOptions().position(tujuLatLng).title("Lokasi Tujuan"));

                // Mendapatkan jalur antara dua titik
                List<LatLng> polylinePath = getDirectionsDetails(asalLatLng, tujuLatLng);
                if (polylinePath != null) {
                    // Menampilkan jalur pada peta
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(polylinePath)
                            .width(5) // lebar polyline
                            .color(Color.BLUE); // warna polyline
                    googleMap.addPolyline(polylineOptions);

                    // Mengatur batas peta
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(asalLatLng);
                    builder.include(tujuLatLng);
                    LatLngBounds bounds = builder.build();

                    int padding = 100; // padding di piksel
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    googleMap.moveCamera(cu);
                } else {
                    Toast.makeText(getBaseContext(), "Tidak dapat mengonversi alamat ke koordinat.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
