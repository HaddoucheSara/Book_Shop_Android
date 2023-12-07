package com.example.book_shop;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Layout;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.grpc.okhttp.internal.Util;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;
    Date date = null;
    String outputDateString = null;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/M/yyyy", Locale.US);

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;

    }



    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));
        String docIdd = this.getSnapshots().getSnapshot(position).getId();
        // Modification ici

        holder.timeTextView.setText((note.time));
        try {
            holder.date.setText(note.getDate());
            date = inputDateFormat.parse(note.getDate());
            outputDateString = dateFormat.format(date);

            String[] items1 = outputDateString.split(" ");
            String day = items1[0];
            String dd = items1[1];
            String month = items1[2];

            holder.day.setText(day);
            holder.date.setText(dd);
            holder.month.setText(month);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*start complete task button*/
        holder.CompleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference;
                documentReference = Utility.getCollectionReferenceForNotes().document(docIdd);
                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //note is deleted
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View customView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.done_task, null);
                            builder.setView(customView)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Fermez le dialog
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            Utility.showToast(context.getApplicationContext(), "Failed while deleting note");
                        }
                    }
                });
                DocumentReference taskRef = FirebaseFirestore.getInstance().collection("tasks").document(docIdd);
            }
        });
        /*end complete task button*/

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,NoteDetailsActivity.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }


    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,contentTextView,timestampTextView,timeTextView, dateTextView,day,date,month;
        Button CompleteTask;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            CompleteTask= itemView.findViewById(R.id.completeButton);
            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);
            timeTextView = itemView.findViewById(R.id.note_time_text_view);
            day = itemView.findViewById(R.id.note_day_text_view);
            date = itemView.findViewById(R.id.note_date_text_view);
            month = itemView.findViewById(R.id.note_month_text_view);
        }
    }




    private String getFormattedTime(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(timestamp.toDate());
    }

    private String getFormattedDate(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(timestamp.toDate());
    }
}