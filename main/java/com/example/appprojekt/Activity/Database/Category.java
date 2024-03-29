package com.example.appprojekt.Activity.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Die Klasse Category repraesentiert eine Kategorie in der Datenbank.
 * Sie wird verwendet, um Kategorien für bestimmte Elemente zu definieren.
 */
@Entity(tableName = "categories")
public class Category {

    /**
     * Der Primaerschluessel für die Kategorie.
     * Er wird automatisch generiert und muss eindeutig sein.
     */
    @PrimaryKey
    @NonNull
    public String category;

    /**
     * Konstruktor für die Category-Klasse.
     */
    public Category(String category) {
        this.category = category;
    }
}
