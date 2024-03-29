package com.example.appprojekt.Activity;

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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Die EditNoteActivity-Klasse ermoeglicht das Bearbeiten einer vorhandenen Notiz.
 * Sie zeigt ein Formular an, in dem der Benutzer den Titel, den Inhalt und die Kategorie der Notiz bearbeiten kann.
 */
public class EditNoteActivity extends AppCompatActivity {

    private EditText titleEditText, noteContentEditText;
    private Spinner categorySpinner;
    private Button cancelButton, okButton;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleEditText = findViewById(R.id.titleEditText);
        noteContentEditText = findViewById(R.id.noteContentEditText);
        categorySpinner = findViewById(R.id.categorySpinnerEdit);
        cancelButton = findViewById(R.id.cancelButton);
        okButton = findViewById(R.id.okButton);

        List<String> categories = new ArrayList<>();
        categories.add("Arbeit");
        categories.add("Persönlich");
        categories.add("Einkaufsliste");
        categories.add("Sport");
        categories.add("Reisen");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        noteId = getIntent().getIntExtra("noteId", -1);

        if (noteId != -1) {
            loadNoteContent();
        } else {
            finish();
        }

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
            }
        });
    }

    private void loadNoteContent() {
        new LoadNoteContentTask().execute();
    }

    private int getIndexForCategory(String category) {
        return 0;
    }

    private void saveNoteAndFinish() {
        String updatedTitle = titleEditText.getText().toString().trim();
        String updatedContent = noteContentEditText.getText().toString().trim();
        String selectedCategory = categorySpinner.getSelectedItem().toString();

        if (!updatedTitle.isEmpty() && !updatedContent.isEmpty()) {
            new UpdateNoteContentTask().execute(updatedTitle, updatedContent, selectedCategory);
        } else {
            Toast.makeText(this, "Titel und Beschreibung dürfen nicht leer sein", Toast.LENGTH_SHORT).show();
        }
    }

    private class LoadNoteContentTask extends AsyncTask<Void, Void, Note> {

        @Override
        protected Note doInBackground(Void... voids) {
            return NoteDatabase.getInstance(EditNoteActivity.this).noteDao().getNoteById(noteId);
        }

        @Override
        protected void onPostExecute(Note note) {
            super.onPostExecute(note);
            if (note != null) {
                titleEditText.setText(note.getTitle());
                noteContentEditText.setText(note.getNoteText());
                categorySpinner.setSelection(getIndexForCategory(note.getCategory()));
            } else {
                finish();
            }
        }
    }

    private class UpdateNoteContentTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String updatedTitle = strings[0];
            String updatedContent = strings[1];
            String updatedCategory = strings[2];
            long currentTime = System.currentTimeMillis();
            String lastUpdatedDateTime = DateFormat.getDateTimeInstance().format(new Date(currentTime));

            NoteDatabase.getInstance(EditNoteActivity.this).noteDao().updateNoteContentAndCategoryAndLastUpdated(noteId, updatedTitle, updatedContent, updatedCategory, lastUpdatedDateTime);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
