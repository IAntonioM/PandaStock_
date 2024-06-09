package com.app.pandastock.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.app.pandastock.database.DatabaseContract.DetalleVentaEntry;
import com.app.pandastock.models.DetalleVenta;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDao {
    private DatabaseHelper dbHelper;

    public DetalleVentaDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertDetalleVenta(DetalleVenta detalleVenta) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DetalleVentaEntry.COL_ID_VENTA, detalleVenta.getIdVenta());
        values.put(DetalleVentaEntry.COL_ID_PRODUCTO, detalleVenta.getIdProducto());
        values.put(DetalleVentaEntry.COL_CANTIDAD, detalleVenta.getCantidad());
        values.put(DetalleVentaEntry.COL_PRECIO, detalleVenta.getPrecio());
        values.put(DetalleVentaEntry.COL_SUBTOTAL, detalleVenta.getSubtotal());
        long id = db.insert(DetalleVentaEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<DetalleVenta> getAllDetallesVenta() {
        List<DetalleVenta> detallesVenta = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DetalleVentaEntry.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setId(cursor.getInt(cursor.getColumnIndex(DetalleVentaEntry.COL_ID)));
                detalleVenta.setIdVenta(cursor.getInt(cursor.getColumnIndex(DetalleVentaEntry.COL_ID_VENTA)));
                detalleVenta.setIdProducto(cursor.getInt(cursor.getColumnIndex(DetalleVentaEntry.COL_ID_PRODUCTO)));
                detalleVenta.setCantidad(cursor.getInt(cursor.getColumnIndex(DetalleVentaEntry.COL_CANTIDAD)));
                detalleVenta.setPrecio(cursor.getDouble(cursor.getColumnIndex(DetalleVentaEntry.COL_PRECIO)));
                detalleVenta.setSubtotal(cursor.getDouble(cursor.getColumnIndex(DetalleVentaEntry.COL_SUBTOTAL)));
                detallesVenta.add(detalleVenta);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return detallesVenta;
    }
}
