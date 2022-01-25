package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;

public class profile extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private BottomNavigationView navBar;
    private Button b_mytrips, b_logout;
    private TextView greeting, mail;
    private UserRepository userRepository;
    private ImageView user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userRepository = new UserRepository(getApplication());

        navBar = findViewById(R.id.navBar);
        b_mytrips = findViewById(R.id.b_tripsmade);
        b_logout = findViewById(R.id.b_logout) ;

        greeting = findViewById(R.id.greeting);

        mail = findViewById(R.id.mail);
        mail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user_logged = mAuth.getCurrentUser();

        user_image = findViewById(R.id.image_user);
        UserViewModel mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.getUser(user_logged.getUid()).observe(this, user -> {
            if (user != null) {
                if (!user.getName().isEmpty()) {
                    greeting.setText("Olá " + user.getName() + "!");
                } else {
                    greeting.setText("Olá " + user_logged.getEmail() + "!");
                }
                if (!user.getImage_url().isEmpty()) {
                    new DownloadImageTask(user_image)
                            .execute(user.getImage_url());
                }
            }
            else{
                greeting.setText("Olá " + user_logged.getEmail() + "!");
            }
        });

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