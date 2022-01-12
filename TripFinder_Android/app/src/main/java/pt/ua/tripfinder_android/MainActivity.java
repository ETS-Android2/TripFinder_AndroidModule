package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private String[] trips, trips_dsc;
    private RecyclerView trip_rv;
    private CustomAdapter adapter;
    private BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navBar = findViewById(R.id.navBar);

        navBar.setSelectedItemId(R.id.home);

        trip_rv = findViewById(R.id.trips);

        trips = new String[3];
        trips_dsc = new String[3];


        trips_dsc[0] = "teste 1";
        trips_dsc[1] = "teste 2";
        trips_dsc[2] = "teste 3";

        trips[0] = "Ria de Aveiro";
        trips[1] = "Salinas";
        trips[2] = "Gastronomia";

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        trip_rv.setLayoutManager(llm);
        trip_rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new CustomAdapter(trips, trips_dsc);
        trip_rv.setAdapter( adapter );

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
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