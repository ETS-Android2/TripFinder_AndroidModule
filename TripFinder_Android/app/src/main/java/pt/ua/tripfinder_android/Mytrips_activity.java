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

public class Mytrips_activity extends AppCompatActivity {

    private BottomNavigationView navBar;
    private RecyclerView mytrip_rv;
    private String[] trips, trips_dsc;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrips);

        mytrip_rv = findViewById(R.id.mytrips_rv);

        trips = new String[2];
        trips_dsc = new String[2];


        trips_dsc[0] = "teste 1";
        trips_dsc[1] = "teste 2";

        trips[0] = "Ria de Aveiro";
        trips[1] = "Gastronomia";

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mytrip_rv.setLayoutManager(llm);
        mytrip_rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new CustomAdapter(trips, trips_dsc);
        mytrip_rv.setAdapter( adapter );

        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.profile);

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