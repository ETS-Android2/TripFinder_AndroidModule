package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TripInfo_Activity extends AppCompatActivity {

    public static String tripId = "tripId";

    private BottomNavigationView navBar;
    private Button b_galery,b_start;
    private TextView tripname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);

        Intent intent = getIntent();
        tripId = intent.getStringExtra(CustomAdapter.ViewHolder.tripId);

        tripname = findViewById(R.id.trip_name);
        tripname.setText(tripId);

        navBar = findViewById(R.id.navBar);
        b_galery = findViewById(R.id.b_TripGalery);
        b_start = findViewById(R.id.b_startTrip);

        navBar.setSelectedItemId(R.id.home);

        b_galery.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TripGalery_Activity.class).putExtra(tripId, tripId));
                overridePendingTransition(0,0);
            }
        });

        b_start.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), map_page.class));
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
}