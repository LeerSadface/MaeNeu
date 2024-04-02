package com.example.appprojekt.Activity.Database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * Die NoteandCategory-Klasse repraesentiert eine Verbindung zwischen Notizen und Kategorien in der Datenbank.
 * Sie ermoeglicht den Zugriff auf Notizen, die zu einer bestimmten Kategorie gehoeren.
 */
public class NoteandCategory {


    @Embedded
    public Note note;

    /** Eine Liste von Notizen, die zu einer bestimmten Kategorie gehoeren. */
    @Relation(
            parentColumn = "id",
            entityColumn = "category"
    )
    public List<Note> notes;
}
