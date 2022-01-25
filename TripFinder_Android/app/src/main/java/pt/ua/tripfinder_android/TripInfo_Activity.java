package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;

public class TripInfo_Activity extends AppCompatActivity {

    public static String tripId = "tripId";
    public static String tripName = "tripName";
    public static String tripLat = "tripLat";
    public static String tripLng = "tripLng";
    private String trip_id;

    private BottomNavigationView navBar;
    private ImageView trip_img;
    private Button b_galery,b_start;
    private TextView tripname, trip_dsc;
    private double lat=0, lng=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);

        Intent intent = getIntent();
        trip_id = intent.getStringExtra(CustomAdapter.ViewHolder.tripId);

        if (trip_id == "Ria de Aveiro"){
            trip_id = "1";
        }
        if (trip_id == "Salinas"){
            trip_id = "2";
        }
        if (trip_id == "Gastronomia"){
            trip_id = "3";
        }
        if (trip_id == "Aliados"){
            trip_id = "4";
        }

        tripname = findViewById(R.id.trip_name);
        trip_dsc = findViewById(R.id.trip_dsc);

        navBar = findViewById(R.id.navBar);
        b_galery = findViewById(R.id.b_TripGalery);
        b_start = findViewById(R.id.b_startTrip);

        trip_img = findViewById(R.id.trip_img);

        navBar.setSelectedItemId(R.id.home);

        TripsViewModel mTripsViewModel = new ViewModelProvider(this).get(TripsViewModel.class);
        mTripsViewModel.getTrip(trip_id).observe(this, trip -> {
            tripname.setText(trip.getTitle());
            trip_dsc.setText(trip.getContentFull());
            lat = trip.getLat();
            lng = trip.getLng();
            new DownloadImageTask(trip_img)
                    .execute(trip.getImageurl());

        });

        b_galery.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TripGalery_Activity.class).putExtra(tripId, tripname.getText()));
                overridePendingTransition(0,0);
            }
        });

        b_start.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), map_page.class)
                        .putExtra(tripName, tripname.getText())
                        .putExtra(tripLat, lat)
                        .putExtra(tripLng, lng)
                );
                overridePendingTransition(0,0);
            }
        });

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.stats:
                        startActivity(new Intent(getApplicationContext(), statistics.class));
                        overridePendingTransition(0,0);
                        return true;
                }


                return false;
            }
        });

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}