package com.example.appprojekt.Activity.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Das CategoryDao-Interface definiert Methoden zum Zugriff auf die Datenbanktabelle "categories".
 * Es enthaelt Methoden zum Einfuegen, Aktualisieren, Loeschen und Abrufen von Kategorien.
 */
@Dao
public interface CategoryDao {

    /**
     * Fuegt eine neue Kategorie in die Datenbank ein.
     * @param category Die zu speichernde Kategorie.
     */
    @Insert
    void insert(Category category);

    /**
     * Aktualisiert eine vorhandene Kategorie in der Datenbank.
     * @param category Die zu aktualisierende Kategorie.
     */
    @Update
    void update(Category category);

    /**
     * Loescht eine Kategorie aus der Datenbank.
     * @param category Die zu loeschende Kategorie.
     */
    @Delete
    void delete(Category category);

    /**
     * Ruft alle Kategorien aus der Datenbank ab.
     * @return Eine Liste aller Kategorien in der Datenbank.
     */
    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();
}
