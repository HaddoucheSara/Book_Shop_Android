package com.example.book_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateAccountActivity extends AppCompatActivity {

    EditText editEmail,editPassword,editUserName,editPasswordConfirm;
    Button btnSignUp;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();

        editUserName = findViewById(R.id.inputUserNameSignUp);
        editEmail = findViewById(R.id.inputEmailSignUp);
        editPassword = findViewById(R.id.inputPasswordSignUp);
        editPasswordConfirm = findViewById(R.id.inputConfimPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.progressBar);



        //route from signup to login
        TextView btn = findViewById(R.id.alreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this,LoginActivity.class));
            }
        });
        //end route

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password,confirmPassword;

                //lire les valeurs
                email = String.valueOf(editEmail.getText());
                password = String.valueOf(editPassword.getText());
                confirmPassword = String.valueOf(editPassword.getText());

                //check if the inputs are empty
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editEmail.setError("Email is invalid");
                    return ;
                }
                if(password.length()<6){
                    editPassword.setError("Password length is invalid");
                    return ;
                }
                if(!password.equals(confirmPassword)){
                    editPasswordConfirm.setError("Password not matched");
                    return ;
                }


                //add user

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    //creating acc is done
                                    Toast.makeText(CreateAccountActivity.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();
                                    firebaseAuth.getCurrentUser().sendEmailVerification();
                                    /**** Profile ****/
                                    String uid = firebaseAuth.getCurrentUser().getUid();
                                    String email_user = firebaseAuth.getCurrentUser().getEmail();
                                    String name_user = String.valueOf(editUserName.getText());

                                    //additionall info
                                    String phoneNumber = "Add your phone";
                                    String location = "Add your location";
                                    String interests = "Add your intrests";
                                    Profile profile = new Profile(uid,name_user,email_user,phoneNumber,interests,location);
                                    db.collection("profiles")
                                            .document(uid)
                                            .set(profile);
                                    Toast.makeText(CreateAccountActivity.this, profile.profile_inf() , Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    //failure
                                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );

            }
        });

    }
}