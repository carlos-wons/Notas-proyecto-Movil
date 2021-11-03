package com.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notes.models.Image;

import java.util.ArrayList;

public class DaoRecorders {
    Context context;
    DB db;
    SQLiteDatabase ad;

    /**
     * Constructor.
     * @param ctx Contexto del Activity que lo ejecuta.
     */
    public DaoRecorders(Context ctx) {
        this.context = ctx;
        db = new DB(ctx);
        ad = db.getWritableDatabase();
    }

    /**
     * Inserta una lista de direcciones que apuntan a archivos de grabación.
     * @param id Id de la nota a la que corresponden.
     * @param recorders Lista de direcciones.
     */
    public void insertRecorder(long id, ArrayList<String> recorders) {
        ContentValues cv;
        for (int i = 0; i < recorders.size(); i++) {
            cv = new ContentValues();
            cv.put(DB.COLUMNS_TABLERECORDERS[1], id);
            cv.put(DB.COLUMNS_TABLERECORDERS[2], recorders.get(i));
            ad.insert(DB.TABLE_RECORDERS_NAME, null, cv);
        }
    }

    /**
     * Obtiene todas las direcciones de archivos correspondientes a una nota.
     * @param id Id de la nota a la que corresponden.
     * @return Lista de direcciones.
     */
    public ArrayList<String> getAll(long id) {
        ArrayList<String> lst = new ArrayList<>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_RECORDERS_NAME + " where "+ DB.COLUMNS_TABLERECORDERS[1]  + " = ?", new String[]{String.valueOf(id)});

        if (cursor.getCount() >= 0) {
            while (cursor.moveToNext()) {
                lst.add(cursor.getString(2));
            }
        }
        return lst;
    }
}
