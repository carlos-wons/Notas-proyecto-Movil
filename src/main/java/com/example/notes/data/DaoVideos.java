package com.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notes.models.Image;

import java.util.ArrayList;

public class DaoVideos {
    Context context;
    DB db;
    SQLiteDatabase ad;

    /**
     * Constructor.
     * @param ctx Contexto del Activity que lo invoca.
     */
    public DaoVideos(Context ctx) {
        this.context = ctx;
        db = new DB(ctx);
        ad = db.getWritableDatabase();
    }

    /**
     * Inserta una lista de direcciones que apuntan a archivos de video.
     * @param id ID de la nota al que pertenecen estos videos.
     * @param videos Lista de direcciones a agregar.
     */
    public void insertVideo(long id, ArrayList<String> videos) {
        ContentValues cv;
        for (int i = 0; i < videos.size(); i++) {
            cv = new ContentValues();
            cv.put(DB.COLUMNS_TABLEVIDEOS[1], id);
            cv.put(DB.COLUMNS_TABLEVIDEOS[2], videos.get(i));
            ad.insert(DB.TABLE_VIDEOS_NAME, null, cv);
        }
    }

    /**
     * Obtiene Una la lista de direcciones a videos pertenecientes a una nota.
     * @param id ID de la nota a la que pertenecen.
     * @return Lista de direcciones.
     */
    public ArrayList<String> getAll(long id) {
        ArrayList<String> lst = new ArrayList<>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_VIDEOS_NAME + " where "+ DB.COLUMNS_TABLEVIDEOS[1]  + " = ?", new String[]{String.valueOf(id)});

        if (cursor.getCount() >= 0) {
            while (cursor.moveToNext()) {
                lst.add(cursor.getString(2));
            }
        }
        return lst;
    }
}
