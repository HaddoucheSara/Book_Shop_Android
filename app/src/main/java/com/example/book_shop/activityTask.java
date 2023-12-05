package com.example.book_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class activityTask extends AppCompatActivity {
    FirebaseAuth mAuth;
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;

    NoteAdapter noteAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_menu, menu); // Assurez-vous de remplacer "your_menu" par le nom de votre fichier de menu XML
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

            // startActivity(new Intent(getApplicationContext(), getApplicationContext());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintask);
        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recyler_view);

        //srtart menu









        addNoteBtn.setOnClickListener((v) -> startActivity(new Intent(activityTask.this, NoteDetailsActivity.class)));


        setupRecylerView();


    }



    void setupRecylerView() {

        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }

}
