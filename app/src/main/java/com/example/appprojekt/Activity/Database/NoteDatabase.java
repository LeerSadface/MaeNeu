package com.example.appprojekt.Activity.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Die NoteDatabase-Klasse repraesentiert die SQLite-Datenbank für die Anwendung.
 * Sie enthaelt die Tabellen für Kategorien und Notizen und bietet Zugriff auf die DAOs.
 */
@Database(entities = {Category.class, Note.class}, version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class NoteDatabase extends RoomDatabase {

    /** Die Instanz der Datenbank. */
    private static NoteDatabase instance;

    /**
     * Gibt eine synchronisierte Instanz der Datenbank zurueck.
     * @param context Der Anwendungskontext.
     * @return Die Instanz der Datenbank.
     */
    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "notes_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    /** Gibt das Data Access Object (DAO) für Kategorien zurueck. */
    public abstract CategoryDao categoryDao();

    /** Gibt das Data Access Object (DAO) für Notizen zurueck. */
    public abstract NoteDao noteDao();
}
