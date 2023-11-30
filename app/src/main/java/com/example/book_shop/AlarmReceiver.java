package com.example.book_shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.book_shop.AlarmDialogActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");

        // Afficher un dialogue, ou lancer une activité, ou effectuer une autre action
        // en fonction de vos besoins
        Intent dialogIntent = new Intent(context, AlarmDialogActivity.class);
        dialogIntent.putExtra("title", title);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialogIntent);

    }
}
