package com.example.book_shop;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmDialogActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageView imageViewAnimatedAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_dialog);

        // Récupérer les informations de l'alarme depuis l'intent
        String title = getIntent().getStringExtra("title");

        // Afficher le titre de l'alarme
        TextView titleTextView = findViewById(R.id.textViewTitle);
        titleTextView.setText("Task: " + title);

        // Charger et jouer le son de l'alarme
        playAlarmSound();
        imageViewAnimatedAlarm = findViewById(R.id.imageViewIcon);
        animateAlarmIcon();
        // Ajouter un bouton pour arrêter l'alarme
        Button stopAlarmButton = findViewById(R.id.buttonStopAlarm);
        stopAlarmButton.setOnClickListener(v -> {
            // Arrêtez le son de l'alarme
            stopAlarmSound();
            stopAlarmIconAnimation();
            // Fermez cette activité une fois l'alarme arrêtée
            finish();
        });
    }
    private void animateAlarmIcon() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        imageViewAnimatedAlarm.startAnimation(rotateAnimation);
    }






    private void stopAlarmIconAnimation() {
        imageViewAnimatedAlarm.clearAnimation();
    }

    private void playAlarmSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.your_alarm_sound); // Remplacez R.raw.your_alarm_sound par le son que vous souhaitez utiliser
        mediaPlayer.setLooping(true); // Pour répéter le son en boucle
        mediaPlayer.start();
    }

    private void stopAlarmSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
