package com.example.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    // Base de datos
    public static final String DATABASE_NAME = "dbNotes";
    public static final int DATABASE_VERSION = 1;
    // Tabla notas
    public static final String TABLE_NOTES_NAME = "notes";
    public static final String[] COLUMS_TABLENOTES = {
            "id", "title", "description", "isReminder", "finishDate"
    };
    public static final String SCRIPT_TABLE_NOTES =
            "create table notes(\n" +
                    "   id integer primary key autoincrement NOT NULL,\n" +
                    "   title text not null,\n" +
                    "   description text not null,\n" +
                    "   isReminder INTEGER NOT NULL,\n" +
                    "   finishDate text\n" +
                    ");";

    // Tabla recordatorios
    public static final String TABLE_REMINDERS_DATE = "remindersDate";
    public static final String[] COLUMS_TABLEREMINDERSDATE = {
            "id", "idNote", "dateReminder"
    };
    public static final String SCRIPT_TABLE_REMINDERS_DATE =
            " CREATE TABLE remindersDate(\n" +
                    "   id INTEGER PRIMARY KEY autoincrement NOT NULL,\n" +
                    "   idNote integer NOT NULL,\n" +
                    "   dateReminder TEXT NOT NULL,\n" +
                    "   FOREIGN KEY(idNote) REFERENCES Notes(id)\n" +
                    " );";

    // Tabla Images
    public static final String TABLE_IMAGES_NAME = "images";
    public static final String[] COLUMNS_TABLEIMAGES = {
            "id", "idNote", "srcImage"
    };
    public static final String SCRIPT_TABLE_IMAGES =
            "CREATE TABLE images (\n" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    "  idNote INTEGER NOT NULL,\n" +
                    "  srcImage TEXT NOT NULL,\n" +
                    "  FOREIGN KEY(idNote) REFERENCES Notes(id)\n" +
                    ");";

    // Tabla Videos
    public static final String TABLE_VIDEOS_NAME = "videos";
    public static final String[] COLUMNS_TABLEVIDEOS = {
            "id", "idNote", "srcVideo"
    };
    public static final String SCRIPT_TABLE_VIDEOS =
            "CREATE TABLE videos (\n" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    "  idNote INTEGER NOT NULL,\n" +
                    "  srcVideo TEXT NOT NULL,\n" +
                    "  FOREIGN KEY(idNote) REFERENCES Notes(id)\n" +
                    ");";

    // Tabla Recorders
    public static final String TABLE_RECORDERS_NAME = "recorders";
    public static final String[] COLUMNS_TABLERECORDERS = {
            "id", "idNote", "srcRecord"
    };
    public static final String SCRIPT_TABLE_RECORDERS =
            "CREATE TABLE recorders (\n" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    "  idNote INTEGER NOT NULL,\n" +
                    "  srcRecord TEXT NOT NULL,\n" +
                    "  FOREIGN KEY(idNote) REFERENCES Notes(id)\n" +
                    ");";

    Context context;

    /**
     * Creación de la base de datos
     * @param context Contexto de la aplicación que lo ejecuta.
     */
    public DB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Ejecuta las tablas creadas anteriormente.
     * @param db Base de datos donde se insertan.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_TABLE_NOTES);
        db.execSQL(SCRIPT_TABLE_IMAGES);
        db.execSQL(SCRIPT_TABLE_REMINDERS_DATE);
        db.execSQL(SCRIPT_TABLE_VIDEOS);
        db.execSQL(SCRIPT_TABLE_RECORDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
