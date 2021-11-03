package com.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.notes.models.Note;

import java.util.ArrayList;

public class DaoNotes {
    Context context;
    DB db;
    SQLiteDatabase ad;

    /**
     * Constructor.
     * @param ctx Contexto del Activity que lo invoca.
     */
    public DaoNotes(Context ctx) {
        this.context = ctx;
        db = new DB(ctx);
        ad = db.getWritableDatabase();
    }

    /**
     * Inserta una nota.
     * @param note La nota a insertar.
     * @return ID de la nota en la base de datos.
     */
    public long insertNote(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLENOTES[1], note.getTitle());
        cv.put(DB.COLUMS_TABLENOTES[2], note.getContent());
        cv.put(DB.COLUMS_TABLENOTES[3], note.isReminder());
        return ad.insert(DB.TABLE_NOTES_NAME, null, cv);
    }

    /**
     * Obtiene todas las notas de la base de datos.
     * @return Lista de notas obtenidas de la base de datos.
     */
    public ArrayList<Note> getAllNotes() {
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_NOTES_NAME + " where isReminder = ?", new String[]{String.valueOf(0)});
        ArrayList<Note> notes = new ArrayList<>();
        if (cursor.getCount() >= 0) {
            while (cursor.moveToNext()) {
                notes.add(new Note(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)
                ));
            }
        }
        return notes;
    }

    /**
     * Actualiza una nota en la base de datos.
     * @param note Nota que será editada.
     * @return Verdadero si la nota se actualizó, falso si no.
     */
    public boolean update(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLENOTES[1], note.getTitle());
        cv.put(DB.COLUMS_TABLENOTES[2], note.getContent());
        cv.put(DB.COLUMS_TABLENOTES[3], note.isReminder());
        cv.put(DB.COLUMS_TABLENOTES[4], "");

        return ad.update(
                DB.TABLE_NOTES_NAME, cv, "id=?",
                new String[]{String.valueOf(note.getId())}
        ) > 0;
    }
}
