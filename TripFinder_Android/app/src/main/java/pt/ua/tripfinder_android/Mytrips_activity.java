package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Mytrips_activity extends AppCompatActivity {

    private BottomNavigationView navBar;
    private RecyclerView mytrip_rv;
    private CustomAdapter adapter;
    private UserRepository userRepository;
    private String name;
    private String description;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrips);

        mytrip_rv = findViewById(R.id.mytrips_rv);

        this.name = "";
        this.description = "";

        userRepository = new UserRepository(getApplication());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user_logged = mAuth.getCurrentUser();

//        User user = userRepository.getUser(user_logged.getUid()).getValue();

        UserViewModel mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.getUser(user_logged.getUid()).observe(this, user -> {
                    String[] trips;

                    if (user != null) {
                        trips = user.getTrip_ids().split(",");
                    } else {
                        trips = new String[0];
                    }

                    String[] mDataSet = new String[trips.length];
                    String[] trips_descrps = new String[trips.length];

                    int i = 0;
                    for (String trip_id : trips) {
                        TripsViewModel mTripViewModel = new ViewModelProvider(this).get(TripsViewModel.class);
                        int finalI = i;
                        mDataSet[finalI] = "1";
                        trips_descrps[finalI] = "1";
                        mTripViewModel.getTrip(trip_id).observe(this, trip -> {
                            if (trip != null) {
                                this.name = trip.getTitle();
                                this.description = trip.getContentShort();
                            }
                        });

                       mDataSet[finalI] = this.name;
                       trips_descrps[finalI] = this.description;
                       i++;
                    }

                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    mytrip_rv.setLayoutManager(llm);
                    mytrip_rv.setItemAnimator(new DefaultItemAnimator());
                    adapter = new CustomAdapter(mDataSet, trips_descrps);
                    mytrip_rv.setAdapter(adapter);
        });
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