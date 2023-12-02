package com.example.book_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn  = findViewById(R.id.delete_note_text_view_btn);

        //receive data
        title = getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener( (v)-> saveNote());

        deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase() );


    }

    void saveNote(){
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        String noteDate = ((EditText) findViewById(R.id.taskDate)).getText().toString();
        String noteTime = ((EditText) findViewById(R.id.taskTime)).getText().toString();
        if(noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("Title is required");
            return;
        }
        if (noteDate == null || noteDate.isEmpty()) {
            // Afficher un message d'erreur pour indiquer que la date est obligatoire
            Utility.showToast(this, "Date is required");
            return;
        }

        if (noteTime == null || noteTime.isEmpty()) {
            // Afficher un message d'erreur pour indiquer que l'heure est obligatoire
            Utility.showToast(this, "Time is required");
            return;
        }




        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        note.setDate(noteDate);
        note.setTime(noteTime);






        saveNoteToFirebase(note);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(note.getDate().split("/")[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(note.getDate().split("/")[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(note.getDate().split("/")[0]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(note.getTime().split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(note.getTime().split(":")[1]));
        calendar.set(Calendar.SECOND, 0);

        // Créer une intention pour déclencher l'alarme
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", note.getTitle()); // Passer des données supplémentaires si nécessaire
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Obtenir l'objet AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Définir une alarme
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }



        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (isEditMode) {
                        // Note edited successfully
                        Utility.showToast(NoteDetailsActivity.this, "Note edited successfully");
                    } else {
                        // Note added successfully
                        Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                        finish();
                    }
                } else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while saving note");
                }
                finish();
            }
        });



    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(NoteDetailsActivity.this,"Note deleted successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"Failed while deleting note");
                }
            }
        });
    }




    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        EditText taskDateEditText = findViewById(R.id.taskDate);
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        taskDateEditText.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        EditText taskTimeEditText = findViewById(R.id.taskTime);
                        String selectedTime = hourOfDay + ":" + minute;
                        taskTimeEditText.setText(selectedTime);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

}