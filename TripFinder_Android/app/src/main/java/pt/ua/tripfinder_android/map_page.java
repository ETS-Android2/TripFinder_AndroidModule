package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ipsec.ike.exceptions.IkeNetworkLostException;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import pt.ua.tripfinder_android.directionhelpers.FetchURL;
import pt.ua.tripfinder_android.directionhelpers.TaskLoadedCallback;

public class map_page extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, OnCameraMoveStartedListener,
        OnCameraMoveListener,
        OnCameraMoveCanceledListener,
        OnCameraIdleListener{
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    FloatingActionButton curr_btn, cam_btn, done_btn;
    private Polyline currentPolyline;
    MapFragment mapFragment;
    FusedLocationProviderClient client;

    private boolean locked = false;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 500;
    private final long MIN_DIST = 0;

    private String trip_name;
    private double lat,lng;
    private Marker myMarker;

    private ImageView picture;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        trip_name = intent.getStringExtra(TripInfo_Activity.tripName);
        lat = intent.getDoubleExtra(TripInfo_Activity.tripLat,0);
        lng = intent.getDoubleExtra(TripInfo_Activity.tripLng,0);

        setContentView(R.layout.map_page);
        curr_btn = findViewById(R.id.curr_btn);
        cam_btn = findViewById(R.id.cam_btn);
        done_btn = findViewById(R.id.done_btn);
        picture = findViewById(R.id.picture);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = LocationServices.getFusedLocationProviderClient(this);

        curr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToCurr();
                locked = true;
            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0,0);
            }
        });

        if (ContextCompat.checkSelfPermission(map_page.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(map_page.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        cam_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            Random rand = new Random();

            // Generate random integers in range 0 to 999
            int rand_int1 = rand.nextInt(10000);
            String generatedString = trip_name + "_" + rand_int1;

            StorageReference storageRef = storage.getReference();

            StorageReference newfileRef = storageRef.child(generatedString+".jpg");

            StorageReference newFileTripRef = storageRef.child(trip_name + "/" + generatedString + ".jpg");

            // While the file names are the same, the references point to different files
            newfileRef.getName().equals(newFileTripRef.getName());    // true
            newfileRef.getPath().equals(newFileTripRef.getPath());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] databyte = baos.toByteArray();

            UploadTask uploadTask = newFileTripRef.putBytes(databyte);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(map_page.this, "Image not saved to Trip gallery",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(map_page.this, "Image saved to Trip gallery",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void moveCameraToCurr(){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(),15));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng dest = new LatLng(lat,lng);
        place2 = new MarkerOptions().position(dest).title(trip_name);
        mMap.addMarker(place2);
        place1 = new MarkerOptions().position(dest).title("My Position").icon(bitmapFromVector(getApplicationContext(),R.drawable.car_icon));
        myMarker = mMap.addMarker(place1);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dest,15));
        getInitialPosition();

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

    private void getInitialPosition() {
        Task<Location> task;
        if (ActivityCompat.checkSelfPermission(map_page.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            task = client.getLastLocation();
            task.addOnSuccessListener(location -> {
                if(location != null){
                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                    myMarker.setPosition(current);
                    new FetchURL(map_page.this).execute(getUrl(current, place2.getPosition(), "driving"), "driving");
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
                getInitialPosition();
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