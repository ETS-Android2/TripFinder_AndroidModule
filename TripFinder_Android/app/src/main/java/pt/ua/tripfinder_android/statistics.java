package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class statistics extends AppCompatActivity {

    private BottomNavigationView navBar;
    private TextView username;
    private UserRepository userRepository;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        navBar = findViewById(R.id.navBar);

        username = findViewById(R.id.username);

        userRepository = new UserRepository(getApplication());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user_logged = mAuth.getCurrentUser();

        UserViewModel mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.getUser(user_logged.getUid()).observe(this, user -> {
                    username.setText(user.getName());
        });

        navBar.setSelectedItemId(R.id.stats);

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
                        return true;
                }


                return false;
            }
        });

    }
}