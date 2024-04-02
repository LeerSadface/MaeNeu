package com.example.appprojekt.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appprojekt.Activity.Database.Note;
import com.example.appprojekt.Activity.Database.NoteDatabase;
import com.example.appprojekt.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


/**
 * Die MainActivity ist die Einstiegsaktivitaet der Anwendung.
 * Sie zeigt die Liste der Notizen an und ermoeglicht das Hinzufuegen neuer Notizen.
 */
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_SHOW_NOTES = 5;
    private static final int REQUEST_ADD_NOTE = 1;
    private RecyclerView noteRecyclerView;
    private NoteAdapter noteAdapter;
    private NoteDatabase noteDatabase;
    private List<Note> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisierung der UI-Elemente
        noteRecyclerView = findViewById(R.id.noteRecyclerView);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesList = new ArrayList<>();
        noteAdapter = new NoteAdapter(notesList);
        noteRecyclerView.setAdapter(noteAdapter);

        EditText searchEditText = findViewById(R.id.searchEditText);

        noteDatabase = NoteDatabase.getInstance(this);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchNotes(s.toString());
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((v) -> startActivityForResult(new Intent(getApplicationContext(), AddNoteActivity.class), REQUEST_ADD_NOTE));

        loadNotes(REQUEST_SHOW_NOTES);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(REQUEST_SHOW_NOTES);
    }

    /**
     * Sucht nach Notizen anhand eines Suchbegriffs und aktualisiert die angezeigte Liste.
     * @param query Der Suchbegriff.
     */
    private void searchNotes(String query) {
        List<Note> searchResults = new ArrayList<>();

        for (Note note : notesList) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(note);
            } else if (note.getCategory().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(note);
            }

            noteAdapter.setNotes(searchResults);
            noteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Lädt die Notizen aus der Datenbank und aktualisiert die Anzeige entsprechend.
     * @param requestCode Der Anforderungscode für die Aktualisierung.
     */
    private void loadNotes(int requestCode) {
        @SuppressLint("StaticFieldLeak")
        class LoadNotesTask extends AsyncTask<Void, Void, List<Note>> {
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return noteDatabase.noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (requestCode == REQUEST_SHOW_NOTES) {
                    notesList.clear();
                    notesList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_ADD_NOTE) {
                    notesList.add(0, notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                    noteAdapter.notifyDataSetChanged();
                }
                noteRecyclerView.smoothScrollToPosition(0);
            }
        }
        new LoadNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadNotes(requestCode);
    }
}
