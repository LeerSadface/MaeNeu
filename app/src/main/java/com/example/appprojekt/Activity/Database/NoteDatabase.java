package com.example.appprojekt.Activity.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Die NoteDatabase-Klasse repräsentiert die SQLite-Datenbank für die Notizen-App.
 * Sie verwendet das Room Persistence Library, um die Datenbankzugriffe zu verwalten.
 */
@Database(entities = {Category.class, Note.class}, version = 3,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    /**
     * Methode zum Erstellen einer Instanz der Datenbank.
     * @param context Der Anwendungskontext.
     * @return Die Instanz der Datenbank.
     */

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "notes_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Initialisierungsroutine aufrufen
            new PopulateDbAsyncTask(instance).execute();
        }
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("DATABASE", "Database onCreate called");
            new PopulateDbAsyncTask(instance).execute();

        }
    };

    // Voreingestelle Kategorien in die Datenbank speichern
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CategoryDao categoryDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            categoryDao = db.categoryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (categoryDao.getAllCategories().isEmpty()) {
                categoryDao.insert(new Category("Arbeit"));
                categoryDao.insert(new Category("Persönlich"));
                categoryDao.insert(new Category("Einkaufsliste"));
                categoryDao.insert(new Category("Sport"));
                categoryDao.insert(new Category("Reisen"));
            }
            return null;
        }
    }
    
    // Abstrakte Methoden zur Rückgabe von DAOs
    public abstract CategoryDao categoryDao();

    public abstract NoteDao noteDao();
}
