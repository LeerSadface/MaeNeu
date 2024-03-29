package com.example.appprojekt.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appprojekt.Activity.Database.Note;
import com.example.appprojekt.Activity.Database.NoteDao;
import com.example.appprojekt.Activity.Database.NoteDatabase;
import com.example.appprojekt.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Die ShowNoteActivity-Klasse zeigt die Details einer einzelnen Notiz an.
 * Sie ermoeglicht es dem Benutzer, die Details einer Notiz zu betrachten, einschließlich Kategorie, Titel, Inhalt und Erstellungs-/Aktualisierungsdatum.
 */
public class ShowNoteActivity extends AppCompatActivity {

    private TextView categoryTextView, titleTextView, noteContentTextView, creationDateTextView, updateDateTextView;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        categoryTextView = findViewById(R.id.categoryTextView);
        titleTextView = findViewById(R.id.titleTextView);
        noteContentTextView = findViewById(R.id.noteContentTextView);
        creationDateTextView = findViewById(R.id.creationDateTextView);
        updateDateTextView = findViewById(R.id.updateDateTextView);
        okButton = findViewById(R.id.okButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int noteId = extras.getInt("noteId");
            loadNoteFromDatabase(noteId);
        }

        okButton.setOnClickListener(v -> finish());
    }

    /**
     * Laedt die Details einer Notiz aus der Datenbank und aktualisiert die Ansicht entsprechend.
     * @param noteId Die ID der Notiz, deren Details geladen werden sollen.
     */
    private void loadNoteFromDatabase(int noteId) {
        LoadNoteTask loadNoteTask = new LoadNoteTask();
        loadNoteTask.execute(noteId);
    }

    /**
     * AsyncTask zum Laden der Notizdetails aus der Datenbank.
     */
    private class LoadNoteTask extends AsyncTask<Integer, Void, Note> {

        @Override
        protected Note doInBackground(Integer... integers) {
            int noteId = integers[0];
            NoteDao noteDao = NoteDatabase.getInstance(ShowNoteActivity.this).noteDao();
            return noteDao.getNoteById(noteId);
        }

        @Override
        protected void onPostExecute(Note note) {
            if (note != null) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String creationDate = dateFormat.format(note.getDateCreated().getTime());
                String updateDate = dateFormat.format(note.getDateLastUpdated().getTime());

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String creationTime = timeFormat.format(note.getDateCreated().getTime());
                String updateTime = timeFormat.format(note.getDateLastUpdated().getTime());

                categoryTextView.setText(note.getCategory());
                titleTextView.setText(note.getTitle());
                noteContentTextView.setText(note.getNoteText());
                creationDateTextView.setText(creationDate + " " + creationTime);
                updateDateTextView.setText(updateDate + " " + updateTime);
            }
        }
    }
}
