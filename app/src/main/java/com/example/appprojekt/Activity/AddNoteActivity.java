package com.example.appprojekt.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private EditText titleEditText, noteTextEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        categorySpinner = findViewById(R.id.categorySpinner);
        titleEditText = findViewById(R.id.titleEditText);
        noteTextEditText = findViewById(R.id.noteTextEditText);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button addButton = findViewById(R.id.addNote);

        // OnClickListener für den Abbrechen-Button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // OnClickListener für den Hinzufügen-Button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNoteToDatabase();
            }
        });

        // Lade Kategorien aus der Datenbank
        loadCategoriesFromDatabase();
    }

    private void loadCategoriesFromDatabase() {
        new LoadCategoriesTask().execute();
    }

    private class LoadCategoriesTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            return NoteDatabase.getInstance(AddNoteActivity.this).categoryDao().getAllCategoryNames();
        }

        @Override
        protected void onPostExecute(List<String> categoryNames) {
            super.onPostExecute(categoryNames);
            // Setze die Kategorien in den Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNoteActivity.this, android.R.layout.simple_spinner_item, categoryNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        }
    }

    private void saveNoteToDatabase() {
        String category = categorySpinner.getSelectedItem().toString();
        String title = titleEditText.getText().toString().trim();
        String noteText = noteTextEditText.getText().toString().trim();

        // Überprüfe, ob Titel und Notiztext eingegeben wurden
        if (title.isEmpty() || noteText.isEmpty()) {
            Toast.makeText(this, "Bitte füllen Sie alle Felder aus", Toast.LENGTH_SHORT).show();
            return;
        }

        // Erstelle eine neue Notiz
        final Note note = new Note(category, title, noteText, Calendar.getInstance(), Calendar.getInstance());

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                // Füge die Notiz zur Datenbank hinzu
                NoteDatabase.getInstance(getApplicationContext()).noteDao().insert(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Zeige eine Toast-Nachricht an und beende die Aktivität
                Toast.makeText(getApplicationContext(), "Notiz hinzugefügt", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        new SaveNoteTask().execute();
    }
}
