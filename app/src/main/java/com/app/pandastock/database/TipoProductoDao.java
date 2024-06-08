package com.app.pandastock.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.pandastock.database.DatabaseContract.TipoProductoEntry;

public class TipoProductoDao {
    private DatabaseHelper dbHelper;

    public TipoProductoDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public Cursor getAllTipoProductos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(TipoProductoEntry.TABLE_NAME,
                new String[]{TipoProductoEntry.COL_NOMBRE},
                null, null, null, null, null);
    }
}

