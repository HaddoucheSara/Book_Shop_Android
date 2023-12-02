package com.example.book_shop;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Note {
    String title;
    String content;

    Timestamp timestamp;
   String date;  // Nouvelle propriété pour stocker la date sous forme de chaîne
    String time;  // Nouvelle propriété pour stocker l'heure sous forme de chaîne

    public Note() {
        // Constructeur par défaut requis par Firebase
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
        // Mettre à jour les propriétés date et time lors de la modification du timestamp
        updateDateTime();
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setDate(String date) {
        this.date = date;
    }

    // Méthode pour mettre à jour les propriétés date et time à partir du timestamp
    private void updateDateTime() {
        // Mettez en forme la date et l'heure à partir du timestamp
        // Vous pouvez ajuster le format en fonction de vos préférences
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    }

}
