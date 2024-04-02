package com.example.appprojekt.Activity.Database;

import androidx.room.TypeConverter;

import java.util.Calendar;

/**
 * Die Converters-Klasse bietet Konvertierungsmethoden für den Room-Datenbanktyp Calendar.
 * Sie konvertiert ein Calendar-Objekt zu einem Long-Wert (Zeitstempel).
 */
public class Converters {

    /**
     * Konvertiert einen Long-Wert (Zeitstempel) in ein Calendar-Objekt.
     * @param value Der Long-Wert, der konvertiert werden soll.
     * @return Ein Calendar-Objekt, das den uebergebenen Zeitstempel darstellt.
     */
    @TypeConverter
    public static Calendar fromTimestamp(Long value) {
        if (value == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        return calendar;
    }

    /**
     * Konvertiert ein Calendar-Objekt in einen Long-Wert (Zeitstempel).
     * @param calendar Das Calendar-Objekt, das konvertiert werden soll.
     * @return Ein Long-Wert, der den Zeitstempel des uebergebenen Calendar-Objekts darstellt.
     */
    @TypeConverter
    public static Long calendarToTimestamp(Calendar calendar) {
        return calendar == null ? null : calendar.getTimeInMillis();
    }
}
