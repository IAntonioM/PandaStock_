package com.app.pandastock.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.pandastock.database.DatabaseContract.InventarioProductoEntry;
import com.app.pandastock.database.DatabaseContract.ProductoEntry;
import com.app.pandastock.models.InventarioProducto;

import java.util.ArrayList;
import java.util.List;

public class InventarioProductoDao {
    private DatabaseHelper dbHelper;

    public InventarioProductoDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean addProductToInventory(int productoId, String codigoBarras) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(InventarioProductoEntry.COL_PRODUCTO_ID, productoId);
            contentValues.put(InventarioProductoEntry.COL_CODIGO_BARRAS, codigoBarras);
            long result = db.insert(InventarioProductoEntry.TABLE_NAME, null, contentValues);

            if (result != -1) {
                // Update stock in Producto table
                db.execSQL("UPDATE " + ProductoEntry.TABLE_NAME + " SET " +
                        ProductoEntry.COL_STOCK + " = " + ProductoEntry.COL_STOCK + " + 1 WHERE " +
                        ProductoEntry.COL_ID + " = ?", new Object[]{productoId});

                db.setTransactionSuccessful();
                return true;
            }
        } finally {
            db.endTransaction();
        }
        return false;
    }

    public boolean removeProductFromInventory(String codigoBarras) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Find the product ID
            Cursor cursor = db.query(InventarioProductoEntry.TABLE_NAME,
                    new String[]{InventarioProductoEntry.COL_PRODUCTO_ID},
                    InventarioProductoEntry.COL_CODIGO_BARRAS + " = ?",
                    new String[]{codigoBarras}, null, null, null);

            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int productoId = cursor.getInt(cursor.getColumnIndex(InventarioProductoEntry.COL_PRODUCTO_ID));
                cursor.close();

                // Delete from inventory
                int rowsAffected = db.delete(InventarioProductoEntry.TABLE_NAME,
                        InventarioProductoEntry.COL_CODIGO_BARRAS + " = ?",
                        new String[]{codigoBarras});

                if (rowsAffected > 0) {
                    // Update stock in Producto table
                    db.execSQL("UPDATE " + ProductoEntry.TABLE_NAME + " SET " +
                            ProductoEntry.COL_STOCK + " = " + ProductoEntry.COL_STOCK + " - 1 WHERE " +
                            ProductoEntry.COL_ID + " = ?", new Object[]{productoId});

                    db.setTransactionSuccessful();
                    return true;
                }
            }
        } finally {
            db.endTransaction();
        }
        return false;
    }

    public List<InventarioProducto> getAllInventoryProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<InventarioProducto> inventoryList = new ArrayList<>();
        Cursor cursor = db.query(InventarioProductoEntry.TABLE_NAME,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(InventarioProductoEntry.COL_ID));
                @SuppressLint("Range") int productoId = cursor.getInt(cursor.getColumnIndex(InventarioProductoEntry.COL_PRODUCTO_ID));
                @SuppressLint("Range") String codigoBarras = cursor.getString(cursor.getColumnIndex(InventarioProductoEntry.COL_CODIGO_BARRAS));
                InventarioProducto inventarioProducto = new InventarioProducto(id, productoId, codigoBarras);
                inventoryList.add(inventarioProducto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return inventoryList;
    }
}