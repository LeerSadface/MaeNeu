package com.example.appprojekt.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appprojekt.Activity.Database.Note;
import com.example.appprojekt.Activity.Database.NoteDatabase;
import com.example.appprojekt.R;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Die EditNoteActivity-Klasse ermoeglicht das Bearbeiten einer vorhandenen Notiz.
 * Sie zeigt ein Formular an, in dem der Benutzer den Titel, den Inhalt und die Kategorie der Notiz bearbeiten kann.
 */
public class EditNoteActivity extends AppCompatActivity {

    private EditText titleEditText, noteContentEditText;
    private Spinner categorySpinner;
    private Button cancelButton, okButton;
    private int noteId;
    private List<String> categoriesFromDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // Initialisierung der UI-Elemente
        titleEditText = findViewById(R.id.titleEditText);
        noteContentEditText = findViewById(R.id.noteContentEditText);
        categorySpinner = findViewById(R.id.categorySpinnerEdit);
        cancelButton = findViewById(R.id.cancelButton);
        okButton = findViewById(R.id.okButton);

        noteId = getIntent().getIntExtra("noteId", -1);

        // Wenn eine gültige Notiz-ID übergeben wurde, lade den Inhalt der Notiz, sonst beende die Aktivität
        if (noteId != -1) {
            loadNoteContent();
        } else {
            finish();
        }

        // Asynchrone Aufgabe zum Laden der Kategorien aus der Datenbank
        new LoadCategoriesTask().execute();

        // OnClickListener für den Abbrechen-Button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNoteAndFinish();
            }   // Methode aufrufen, um die Notiz zu speichern und die Aktivität zu beenden
        });
    }

    private void loadNoteContent() {
        new LoadNoteContentTask().execute();
    }

    // Methode zur Ermittlung des Indexes für eine bestimmte Kategorie in der Kategorienliste
    private int getIndexForCategory(String category) {
        if (categoriesFromDb != null) {
            return categoriesFromDb.indexOf(category);
        }
        return 0;
    }
    // Methode zum Speichern der bearbeiteten Notiz und Beenden der Aktivität
    private void saveNoteAndFinish() {
        String updatedTitle = titleEditText.getText().toString().trim();
        String updatedContent = noteContentEditText.getText().toString().trim();
        String selectedCategory = categorySpinner.getSelectedItem().toString();

        // Überprüfung, ob Titel und Inhalt nicht leer sind
        if (!updatedTitle.isEmpty() && !updatedContent.isEmpty()) {
            new UpdateNoteContentTask().execute(updatedTitle, updatedContent, selectedCategory);
        } else {
            // Anzeige einer Benachrichtigung, wenn Titel oder Inhalt leer sind
            Toast.makeText(this, "Titel und Beschreibung dürfen nicht leer sein", Toast.LENGTH_SHORT).show();
        }
    }
    // Asynchrone Aufgabe zum Laden der Kategorien aus der Datenbank
    private class LoadCategoriesTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            return NoteDatabase.getInstance(EditNoteActivity.this).categoryDao().getAllCategoryNames();
        }

        @Override
        protected void onPostExecute(List<String> categoryNames) {
            super.onPostExecute(categoryNames);
            categoriesFromDb = categoryNames;   // Kategorienliste aktualisieren
            // Adapter erstellen und mit der Kategorienliste verknüpfen
            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditNoteActivity.this, android.R.layout.simple_spinner_item, categoriesFromDb);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        }
    }
    // Asynchrone Aufgabe zum Laden des Inhalts der zu bearbeitenden Notiz aus der Datenbank
    private class LoadNoteContentTask extends AsyncTask<Void, Void, Note> {

        @Override
        protected Note doInBackground(Void... voids) {
            return NoteDatabase.getInstance(EditNoteActivity.this).noteDao().getNoteById(noteId);   // Notiz aus der Datenbank abrufen
        }

        @Override
        protected void onPostExecute(Note note) {
            super.onPostExecute(note);
            if (note != null) {
                // Inhalte der Notiz in die entsprechenden UI-Elemente setzen
                titleEditText.setText(note.getTitle());
                noteContentEditText.setText(note.getNoteText());
                categorySpinner.setSelection(getIndexForCategory(note.getCategory()));
            } else {
                finish();
            }
        }
    }
    // Asynchrone Aufgabe zum Aktualisieren der Notiz in der Datenbank
    private class UpdateNoteContentTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String updatedTitle = strings[0];
            String updatedContent = strings[1];
            String updatedCategory = strings[2];

            //  die aktuelle Zeit in Millisekunden
            long currentTime = System.currentTimeMillis();

            String lastUpdatedDateTime = Long.toString(currentTime);

            // Aktualisieren der Notiz in der Datenbank
            NoteDatabase.getInstance(EditNoteActivity.this).noteDao().updateNoteContentAndCategoryAndLastUpdated(noteId, updatedTitle, updatedContent, updatedCategory, lastUpdatedDateTime);
            Log.d("EditNoteActivity", "Last Updated Date: " + lastUpdatedDateTime);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);   // Setzen des Ergebniscodes
            finish();   // Aktivität beenden
        }
    }
}
