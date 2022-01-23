package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pt.ua.tripfinder_android.directionhelpers.FetchURL;
import pt.ua.tripfinder_android.directionhelpers.TaskLoadedCallback;

public class map_page extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, OnCameraMoveStartedListener,
        OnCameraMoveListener,
        OnCameraMoveCanceledListener,
        OnCameraIdleListener{
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    FloatingActionButton curr_btn;
    private Polyline currentPolyline;
    MapFragment mapFragment;
    FusedLocationProviderClient client;

    private boolean locked = true;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 500;
    private final long MIN_DIST = 0;

    private String trip_name;
    private double lat,lng;
    private Marker myMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        trip_name = intent.getStringExtra(TripInfo_Activity.tripId);
        lat = intent.getDoubleExtra(TripInfo_Activity.tripLat,0);
        lng = intent.getDoubleExtra(TripInfo_Activity.tripLng,0);

        setContentView(R.layout.map_page);
        curr_btn = findViewById(R.id.curr_btn);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = LocationServices.getFusedLocationProviderClient(this);

        curr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                locked = true;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng dest = new LatLng(lat,lng);
        place2 = new MarkerOptions().position(dest).title(trip_name);
        mMap.addMarker(place2);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dest,15));
        getCurrentLocation();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                myMarker.setPosition(current);
                if(locked){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current,15));
                }
                new FetchURL(map_page.this).execute(getUrl(current, place2.getPosition(), "driving"), "driving");
            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }catch (SecurityException e){
            e.printStackTrace();
        }

        mMap.setOnCameraIdleListener( this);
        mMap.setOnCameraMoveStartedListener( this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
    }

    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            locked = false;
        }
    }
    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {

    }

    private void getCurrentLocation() {
        Task<Location> task;
        if (ActivityCompat.checkSelfPermission(map_page.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            task = client.getLastLocation();
            task.addOnSuccessListener(location -> {
                if(location != null){
                    mapFragment.getMapAsync(googleMap -> {
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        place1 = new MarkerOptions().position(current).title("My Position").icon(bitmapFromVector(getApplicationContext(),R.drawable.car_icon));
                        myMarker = googleMap.addMarker(place1);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current,15));
                        new FetchURL(map_page.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                    });
                }
            });
        }else{
            ActivityCompat.requestPermissions(map_page.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            ActivityCompat.requestPermissions(map_page.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }
    }

    private BitmapDescriptor bitmapFromVector(Context context, int VectorResID){
        Drawable vectorDrawable= ContextCompat.getDrawable(context, VectorResID);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.map_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}