package com.app.pandastock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.app.pandastock.database.DatabaseContract.VentaEntry;
import com.app.pandastock.models.Venta;
import java.util.ArrayList;
import java.util.List;

public class VentaDao {
    private DatabaseHelper dbHelper;

    public VentaDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertVenta(Venta venta) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VentaEntry.COL_NOMBRE_CLIENTE, venta.getNombreCliente());
        values.put(VentaEntry.COL_APELLIDO_CLIENTE, venta.getApellidoCliente());
        values.put(VentaEntry.COL_CELULAR, venta.getCelular());
        values.put(VentaEntry.COL_DNI, venta.getDni());
        values.put(VentaEntry.COL_ID_EMPLEADO, venta.getIdEmpleado());
        values.put(VentaEntry.COL_MONTO_TOTAL, venta.getMontoTotal());
        long id = db.insert(VentaEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<Venta> getAllVentas() {
        List<Venta> ventas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + VentaEntry.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Venta venta = new Venta();
                venta.setId(cursor.getInt(cursor.getColumnIndex(VentaEntry.COL_ID)));
                venta.setNombreCliente(cursor.getString(cursor.getColumnIndex(VentaEntry.COL_NOMBRE_CLIENTE)));
                venta.setApellidoCliente(cursor.getString(cursor.getColumnIndex(VentaEntry.COL_APELLIDO_CLIENTE)));
                venta.setCelular(cursor.getString(cursor.getColumnIndex(VentaEntry.COL_CELULAR)));
                venta.setDni(cursor.getString(cursor.getColumnIndex(VentaEntry.COL_DNI)));
                venta.setIdEmpleado(cursor.getInt(cursor.getColumnIndex(VentaEntry.COL_ID_EMPLEADO)));
                venta.setMontoTotal(cursor.getDouble(cursor.getColumnIndex(VentaEntry.COL_MONTO_TOTAL)));
                ventas.add(venta);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ventas;
    }
}