package com.app.pandastock.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.pandastock.database.DatabaseContract.ProductoEntry;
import com.app.pandastock.models.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoDao {
    private DatabaseHelper dbHelper;

    public ProductoDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Método para insertar un nuevo producto
    public boolean createProduct(int categoriaId, int marcaId, String modelo, double precio, int stock) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductoEntry.COL_TIPO_PRODUCTO_ID, categoriaId);
        contentValues.put(ProductoEntry.COL_MARCA_ID, marcaId);
        contentValues.put(ProductoEntry.COL_MODELO, modelo);
        contentValues.put(ProductoEntry.COL_PRECIO, precio);
        contentValues.put(ProductoEntry.COL_STOCK, stock);
        long result = db.insert(ProductoEntry.TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Método para obtener la lista de productos
    public List<Producto> getAllProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Producto> productList = new ArrayList<>();
        Cursor cursor = db.query(ProductoEntry.TABLE_NAME,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(ProductoEntry.COL_ID));
                @SuppressLint("Range") int categoriaId = cursor.getInt(cursor.getColumnIndex(ProductoEntry.COL_TIPO_PRODUCTO_ID));
                @SuppressLint("Range") int marcaId = cursor.getInt(cursor.getColumnIndex(ProductoEntry.COL_MARCA_ID));
                @SuppressLint("Range") String modelo = cursor.getString(cursor.getColumnIndex(ProductoEntry.COL_MODELO));
                @SuppressLint("Range") double precio = cursor.getDouble(cursor.getColumnIndex(ProductoEntry.COL_PRECIO));
                @SuppressLint("Range") int stock = cursor.getInt(cursor.getColumnIndex(ProductoEntry.COL_STOCK));
                Producto producto = new Producto(id, categoriaId, marcaId, modelo, precio, stock);
                productList.add(producto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // Método para actualizar un producto
    public boolean updateProduct(int id, int categoriaId, int marcaId, String modelo, double precio, int stock) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductoEntry.COL_TIPO_PRODUCTO_ID, categoriaId);
        contentValues.put(ProductoEntry.COL_MARCA_ID, marcaId);
        contentValues.put(ProductoEntry.COL_MODELO, modelo);
        contentValues.put(ProductoEntry.COL_PRECIO, precio);
        contentValues.put(ProductoEntry.COL_STOCK, stock);
        int rowsAffected = db.update(ProductoEntry.TABLE_NAME, contentValues,
                ProductoEntry.COL_ID + "=?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }
}

