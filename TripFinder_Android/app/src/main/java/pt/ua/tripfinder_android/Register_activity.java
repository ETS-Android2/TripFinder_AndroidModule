package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register_activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText mail, pass, name, image_url;
    private static final String TAG = "EmailPassword";
    private UserRepository userRepository;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0,0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        userRepository = new UserRepository(getApplication());

        setContentView(R.layout.activity_register);

        mail = findViewById(R.id.mail_register);
        pass = findViewById(R.id.pass_register);
        name = findViewById(R.id.name_register);
        image_url = findViewById(R.id.image_register);

        Button b_signin = findViewById(R.id.b_signin);

        b_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail_value = mail.getText().toString();
                String pass_value = pass.getText().toString();
                String name_value = name.getText().toString();
                String image_url_value = image_url.getText().toString();

                if (mail_value != null && !mail_value.isEmpty() && pass_value != null && !pass_value.isEmpty() && !name_value.isEmpty()) {
                    if (image_url_value.isEmpty()){
                        image_url_value = " ";
                    }
                    createAccount(mail_value, pass_value, name_value, image_url_value);
                }
            }
        });

    }

    private void createAccount(String email, String password, String name, String image_url) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User newUser = new User(user.getUid().toString(), name, image_url, "");
                            userRepository.insert(newUser);
                            startActivity(new Intent(getApplicationContext(), Login_activity.class));
                            overridePendingTransition(0,0);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register_activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }
}