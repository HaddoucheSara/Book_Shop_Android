package com.example.book_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        ///create a user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        String email = "test@example.com";
//        String password = "testpassword";
//
//        auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // User creation successful
//                        FirebaseUser user = auth.getCurrentUser();
//
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                    }
//                });

        String uid = auth.getCurrentUser().getUid();
        String email_user = auth.getCurrentUser().getEmail();
        String name_user = auth.getCurrentUser().getDisplayName();

        //additionall info
        String phoneNumber = "Add your phone";
        String location = "Add your location";
        String interests = "Add your intrests";
        //create user object
        Profile profile = new Profile(uid,name_user,email_user,phoneNumber,interests,location);

        //store user info
        db.collection("profiles")
                .document(uid)
                .set(profile);

        ///////


        Button go_profile = findViewById(R.id.go_profile);

        go_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class );
                startActivity(intent);
            }
        });
    }
}