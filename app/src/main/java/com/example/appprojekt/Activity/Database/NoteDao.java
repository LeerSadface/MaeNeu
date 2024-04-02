package com.example.appprojekt.Activity.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

/**
 * Das NoteDao-Interface definiert Methoden zum Zugriff auf die Notizen in der Datenbank.
 */
@Dao
public interface NoteDao {

    /** Fuegt eine neue Notiz in die Datenbank ein. */
    @Insert
    void insert(Note note);

    /** Aktualisiert eine vorhandene Notiz in der Datenbank. */
    @Update
    void update(Note note);

    /** Loescht eine Notiz aus der Datenbank. */
    @Delete
    void delete(Note note);

    /** Ruft alle Notizen aus der Datenbank ab. */
    @Query("SELECT * FROM notes")
    List<Note> getAllNotes();

    /** Loescht alle Notizen aus der Datenbank. */
    @Query("DELETE FROM notes")
    void deleteAll();

    /** Sucht Notizen anhand eines Suchtextes. */
    @Query("SELECT * FROM notes WHERE title LIKE :searchText")
    List<Note> searchNotes(String searchText);

    /** Aktualisiert den Inhalt, die Kategorie und das Datum der letzten Aktualisierung einer Notiz. */
    @Query("UPDATE notes SET title = :title, note_text = :text, category = :category, date_last_updated = :lastUpdatedDateTime WHERE id = :noteId")
    void updateNoteContentAndCategoryAndLastUpdated(int noteId, String title, String text, String category, String lastUpdatedDateTime);

    /** Ruft Notizen und zugehoerige Kategorien aus der Datenbank ab. */
    @Transaction
    @Query("SELECT * FROM notes")
    List<NoteandCategory> getNoteAndCategory();

    /** Ruft Notizen einer bestimmten Kategorie aus der Datenbank ab. */
    @Query("SELECT * FROM notes WHERE category = :categoryName")
    List<Note> getNotesByCategoryName(String categoryName);

    /** Ruft eine Notiz anhand ihrer ID aus der Datenbank ab. */
    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNoteById(int noteId);

    /** Loescht eine Notiz anhand ihrer ID aus der Datenbank. */
    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById(int noteId);
}
