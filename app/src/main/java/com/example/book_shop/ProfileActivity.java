package com.example.book_shop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

public class ProfileActivity  extends AppCompatActivity {
    Button edit_profile;
    TextView name_txt, email_txt, phone_txt, location_txt, interests_txt, email_top;
    ImageView image_user;
    Profile profileUser = new Profile();
    private static final int SAVE_PROFILE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        //set the variable
        edit_profile = findViewById(R.id.btn_edit_profile);
        name_txt = findViewById(R.id.name_user);
        email_txt = findViewById(R.id.email_user);
        phone_txt = findViewById(R.id.phone_user);
        location_txt = findViewById(R.id.location_user);
        interests_txt = findViewById(R.id.interests_user);
        email_top = findViewById(R.id.email_user_top);

        //get the data from the data base
//        AtomicReference<Profile> profileUser = new AtomicReference<>(new Profile());/**/
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid_db = auth.getCurrentUser().getUid();

        db.collection("profiles")
                .document()
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        Profile profile  = documentSnapshot.toObject(Profile.class);
                        String name_db = profile.getName();
                        String email_db = profile.getEmail();
                        String phone_db = profile.getPhone();
                        String location_db = profile.getLocation();
                        String interests_db = profile.getIntrests();
                        profileUser = new Profile(uid_db,name_db,email_db,phone_db,location_db,interests_db);
                    }
                    else{
                        profileUser = new Profile();
                    }

                }).addOnFailureListener(e->{
                    profileUser = new Profile();
                });
        name_txt.setText(profileUser.getName());
        email_top.setText(profileUser.getEmail());
        email_txt.setText(profileUser.getEmail());
        phone_txt.setText(profileUser.getPhone());
        location_txt.setText(profileUser.getLocation());
        interests_txt.setText(profileUser.getIntrests());

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(ProfileActivity.this, Edit_profile.class );
               intent.putExtra("profile",profileUser);
               startActivityForResult(intent, SAVE_PROFILE);

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_CANCELED) return;
        if(requestCode == SAVE_PROFILE){
            String name = data.getStringExtra("name");
            String email = data.getStringExtra("email");
            String phone = data.getStringExtra("phone");
            String location = data.getStringExtra("location");
            String image = data.getStringExtra("image");
            String interests = data.getStringExtra("interests");
            name_txt.setText(name);
            email_txt.setText(email);
            phone_txt.setText(phone);
            interests_txt.setText(interests);
            location_txt.setText(location);
            email_top.setText(email);

        }
    }



}
