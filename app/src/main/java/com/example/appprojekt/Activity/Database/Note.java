package com.example.appprojekt.Activity.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

/**
 * Die Klasse Note repräsentiert eine Notiz in der Datenbank.
 * Sie enthält Attribute wie Kategorie, Titel, Notiztext und Datumsangaben.
 */
@Entity(tableName = "notes")
public class Note {

    /** Die eindeutige ID der Notiz. */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /** Die Kategorie, der die Notiz zugeordnet ist. */
    @ColumnInfo(name = "category")
    public String category;

    /** Der Titel der Notiz. */
    @ColumnInfo(name = "title")
    public String title;

    /** Der Textinhalt der Notiz. */
    @ColumnInfo(name = "note_text")
    public String noteText;

    /** Das Datum, an dem die Notiz erstellt wurde. */
    @ColumnInfo(name = "date_created")
    public Calendar dateCreated;

    /** Das Datum der letzten Aktualisierung der Notiz. */
    @ColumnInfo(name = "date_last_updated")
    public Calendar dateLastUpdated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Calendar getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Calendar dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }


    public Note(String category, String title, String noteText, Calendar dateCreated, Calendar dateLastUpdated) {
        this.category = category;
        this.title = title;
        this.noteText = noteText;
        this.dateCreated = dateCreated;
        this.dateLastUpdated = dateLastUpdated;
    }


}
