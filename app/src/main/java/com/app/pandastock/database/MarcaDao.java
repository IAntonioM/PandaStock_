package com.app.pandastock.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.pandastock.database.DatabaseContract.MarcaEntry;
import com.app.pandastock.database.DatabaseContract.TipoProductoEntry;

public class MarcaDao {
    private DatabaseHelper dbHelper;

    public MarcaDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public Cursor getMarcasByTipoProducto(String tipoProducto) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + MarcaEntry.COL_NOMBRE + " FROM " + MarcaEntry.TABLE_NAME +
                " WHERE " + MarcaEntry.COL_TIPO_PRODUCTO_ID + " = (SELECT " + TipoProductoEntry.COL_ID +
                " FROM " + TipoProductoEntry.TABLE_NAME + " WHERE " + TipoProductoEntry.COL_NOMBRE + "=?)";
        return db.rawQuery(query, new String[]{tipoProducto});
    }
}

