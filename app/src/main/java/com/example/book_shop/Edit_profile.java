package com.example.book_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class Edit_profile extends AppCompatActivity {
    Button save_profile;
    Button changeImageButton;
    EditText name_txt, email_txt, phone_txt, location_txt, interests_txt;
    ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        //variables
        save_profile = findViewById(R.id.btn_save_profile);
        name_txt = findViewById(R.id.name_user);
        email_txt = findViewById(R.id.email_user);
        phone_txt = findViewById(R.id.phone_user);
        location_txt = findViewById(R.id.location_user);
        interests_txt = findViewById(R.id.interests_user);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("profile")){
            Profile profileUser = (Profile) intent.getSerializableExtra("profile");
            name_txt.setText(profileUser.getName());
            email_txt.setText(profileUser.getEmail());
            phone_txt.setText(profileUser.getPhone());
            location_txt.setText(profileUser.getLocation());
            interests_txt.setText(profileUser.getIntrests());
        }
        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("email", email_txt.getText().toString());
                resultIntent.putExtra("name", name_txt.getText().toString());
                resultIntent.putExtra("phone", phone_txt.getText().toString());
                resultIntent.putExtra("interests", interests_txt.getText().toString());
                resultIntent.putExtra("location", location_txt.getText().toString());


                Profile profile = new Profile();
                profile.setName(name_txt.getText().toString());
                profile.setEmail(email_txt.getText().toString());
                profile.setPhone(phone_txt.getText().toString());
                profile.setLocation(location_txt.getText().toString());
                profile.setIntrests(interests_txt.getText().toString());
//                saveProfileToFirebase(profile);
                //save data to db
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String uid = auth.getCurrentUser().getUid();
                FirebaseUser user = auth.getCurrentUser();
                /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(profile.getName())
                                .build();
                user.updateProfile(profileUpdates);
                user.updateEmail(profile.getEmail());*/
//                db.collection("profiles").document(uid).set(profileUpdates, SetOptions.merge());



                db.collection("profiles").document(uid).set(profile, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> {
                            // Update was successful
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                            Toast.makeText(Edit_profile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        });
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        int id = item.getItemId();
        if (id == R.id.bottom_home) {

            startActivity(new Intent(getApplicationContext(), activityTask.class));
            return true;
        } else if (id == R.id.bottom_account) {

            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            return true;
        }
        else if (id == R.id.bottom_logout) {

            firebaseAuth.getCurrentUser().sendEmailVerification();
            firebaseAuth.signOut();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;

    }

}
