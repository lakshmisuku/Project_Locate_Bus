package com.example.project_locate_bus;

import androidx.fragment.app.FragmentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.example.project_locate_bus.databinding.ActivityMapsBinding;
import com.google.firebase.database.DatabaseReference;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final int PERMISSIONS_REQUEST_LOCATION = 123;
    double longGPS, latGPS;
    LocationManager locationManager;
    String provider;
    MarkerOptions origin, destination;
    List<Address> addressList = new ArrayList<>();
    DatabaseReference databaseReference;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private HomeRecyclerViewAdapter adapter;
    private ActivityResultLauncher<IntentSenderRequest> resolutionForResult;
    String lg,lt,cityName=null;
    Double firebaseLat = 0.0;
    Double firebaseLong = 0.0;
    Double lat = 0.0;
    Double lont = 0.0;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        preferences = getSharedPreferences("LocateMyBus",MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://locate-my-bus-5e265-default-rtdb.firebaseio.com/");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.viewPager.setLayoutManager(layoutManager);
        adapter = new HomeRecyclerViewAdapter(this, addressList,lg,lt,cityName);
        binding.viewPager.setAdapter(adapter);

        enableLocationSettings();
        resolutionForResult = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                getLocation();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child("Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                LocationModel model = snapshot.getValue(LocationModel.class);
                firebaseLat = model.getLatitude();
                firebaseLong = model.getLongitude();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=driving";
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyAKueYWFVF6M472H_4nPwZEyxhkfNOmj8o";
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void enableLocationSettings() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10*1000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        LocationServices.getSettingsClient(MapsActivity.this).checkLocationSettings(builder.build()).addOnSuccessListener(MapsActivity.this, (LocationSettingsResponse response) -> {
            getLocation();
        }).addOnFailureListener(MapsActivity.this, ex -> {
            if (ex instanceof ResolvableApiException) {
                try {
                    IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(((ResolvableApiException) ex).getResolution()).build();
                    resolutionForResult.launch(intentSenderRequest);
                } catch (Exception exception) {
                    Toast.makeText(this, "" + exception, Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "enableLocationSettings: " + exception);
                }
            }
        });
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500L, (float) 0, (LocationListener) this);

        if (location != null) onLocationChanged(location);
        else location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(getBaseContext(), "Location can't be retrived", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
    @Override
    public void onLocationChanged(Location location) {
        longGPS = location.getLongitude();
        latGPS = location.getLatitude();
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),500.0f));
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));

        databaseReference.child("Driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String busName = preferences.getString("BusName", "");

                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    if(dataSnapshot.child("BusName").getValue().toString().equals(busName)){
                        Log.e("TAG", "onDataChange: " +dataSnapshot.child("Contact").getValue().toString() );

                        String driverMobileNumber = dataSnapshot.child("Contact").getValue().toString();
                        try {
                            databaseReference.child("Location").child(driverMobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.getValue() != null){
                                        LocationModel model = snapshot.getValue(LocationModel.class);
                                        lat = model.getLatitude();
                                        lont = model.getLongitude();

                                        mMap.clear();
                                        origin = new MarkerOptions().position(new LatLng(latGPS, longGPS)).title("HSR Layout").snippet("origin");
                                        destination = new MarkerOptions().position(new LatLng(lat, lont)).title("Destination").snippet("destination");

                                        String url = getDirectionsUrl(origin.getPosition(), destination.getPosition());

                                        DownloadTask downloadTask = new DownloadTask();
                                        downloadTask.execute(url);

                                        mMap.addMarker(origin);
                                        mMap.addMarker(destination);
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin.getPosition(), 20f));

                                        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                                        List<Address> addresses;
                                        try {
                                            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            if (addresses.size() > 0) System.out.println(addresses.get(0).getLocality());
                                            cityName = addresses.get(0).getLocality();
                                            Log.e("TAG", "onLocationChanged: " + addresses);

                                            lg = String.valueOf(location.getLongitude());
                                            lt = String.valueOf(location.getLatitude());
                                            cityName = addresses.get(0).getLocality();

                                            addressList.clear();
                                            addressList.addAll(addresses);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        String s = location.getLongitude() + "\n" + location.getLatitude() + "\n\n My Current City is : " + cityName;
                                        Log.e("TAG", "onLocationChanged: " + s);
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(MapsActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: "+error.getMessage() );
            }
        });




    }

    @Override
    public void onProviderDisabled(String provider) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
            }
            // Drawing polyline in the Google Map
            if (points.size() != 0) mMap.addPolyline(lineOptions);
        }
    }
}

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
