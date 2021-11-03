package com.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notes.models.Reminders;

import java.util.ArrayList;
import java.util.List;

public class DaoReminders {
    Context context;
    DB db;
    SQLiteDatabase ad;

    /**
     * Constructor.
     * @param ctx Contexto del Activity que lo invoca.
     */
    public DaoReminders(Context ctx) {
        this.context = ctx;
        db = new DB(ctx);
        ad = db.getWritableDatabase();
    }

    /**
     * Inserta un nuevo recordatorio
     * @param reminders Recordatorio a insertar.
     * @return ID del recordatorio en la base de datos.
     */
    public long insertReminder(Reminders reminders) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLENOTES[1], reminders.getTitle());
        cv.put(DB.COLUMS_TABLENOTES[2], reminders.getContent());
        cv.put(DB.COLUMS_TABLENOTES[3], reminders.isReminder());
        cv.put(DB.COLUMS_TABLENOTES[4], reminders.getFinishDate());
        return ad.insert(DB.TABLE_NOTES_NAME, null, cv);
    }

    /**
     * Obtiene todos los recordatorios de la base de datos.
     * @return Una lista de recordatorios.
     */
    public ArrayList<Reminders> getAllReminders() {
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_NOTES_NAME + " where isReminder = ?", new String[]{String.valueOf(1)});
        ArrayList<Reminders> reminders = new ArrayList<>();
        if (cursor.getCount() >= 0) {
            while (cursor.moveToNext()) {
                reminders.add(new Reminders(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4)
                ));
            }
        }
        return reminders;
    }

    /**
     * Obtiene un recordatorio por su ID.
     * @param id ID del recordatorio a obtener.
     * @return Recordatorio obtenido de la base de datos.
     */
    public Reminders getOneById(long id) {
        Cursor cursor = null;
        Reminders reminder = null;
        cursor = ad.rawQuery("select * from " + DB.TABLE_NOTES_NAME + " where " + DB.COLUMS_TABLENOTES[0] + "=?",
                new String[]{String.valueOf(id)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                reminder = new Reminders(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                );
            }
        }
        return reminder;
    }

    /**
     * Actualiza un recordatorio en la base de datos.
     * @param reminder Recordatiorio a actualizar.
     * @return Verdadero si se actualizó, falso si no.
     */
    public boolean update(Reminders reminder) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLENOTES[1], reminder.getTitle());
        cv.put(DB.COLUMS_TABLENOTES[2], reminder.getContent());
        cv.put(DB.COLUMS_TABLENOTES[3], reminder.isReminder());
        cv.put(DB.COLUMS_TABLENOTES[4], reminder.getFinishDate());

        return ad.update(
                DB.TABLE_NOTES_NAME, cv, "id=?",
                new String[]{String.valueOf(reminder.getId())}
        ) > 0;
    }

    /**
     * Elimina un recordatorio de la base de datos.
     * @param id ID del recordatorio a eliminar.
     * @return Verdadero si se eliminó, falso si no.
     */
    public boolean delete(long id) {
        return ad.delete(DB.TABLE_NOTES_NAME, "id=?", new String[]{String.valueOf(id)}) > 0;
    }
}
