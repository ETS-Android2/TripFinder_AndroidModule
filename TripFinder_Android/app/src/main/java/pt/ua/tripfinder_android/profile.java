package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class profile extends AppCompatActivity {

    private BottomNavigationView navBar;
    private Button b_mytrips, b_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        navBar = findViewById(R.id.navBar);
        b_mytrips = findViewById(R.id.b_tripsmade);
        b_logout = findViewById(R.id.b_logout) ;

        navBar.setSelectedItemId(R.id.profile);

        b_mytrips.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Mytrips_activity.class));
                overridePendingTransition(0,0);
            }

        });

        b_logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login_activity.class));
                overridePendingTransition(0,0);
            }

        });

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.profile:
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