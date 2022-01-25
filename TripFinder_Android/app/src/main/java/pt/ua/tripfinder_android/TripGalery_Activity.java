package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class TripGalery_Activity extends AppCompatActivity {

    private BottomNavigationView navBar;
    public static String tripId = "tripId";
    private TextView tripname;
    private FirebaseStorage storage;
    private RecyclerView images_RV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_galery);

        storage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        tripId = intent.getStringExtra(TripInfo_Activity.tripId);

        tripname = findViewById(R.id.tripgalery_name);
        tripname.setText(tripId);

        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.home);

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

        StorageReference listRef = storage.getReference().child(tripId + "/");

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        images_RV = findViewById(R.id.ImageGalery);
                        images_RV.setLayoutManager(llm);
                        images_RV.setItemAnimator(new DefaultItemAnimator());

                        ImageGalleryAdapter imageGalleryAdapter = new ImageGalleryAdapter(listResult.getItems());
                        images_RV.setAdapter(imageGalleryAdapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });

    }
}