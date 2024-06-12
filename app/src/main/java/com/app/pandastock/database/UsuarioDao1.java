package com.app.pandastock.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.pandastock.database.DatabaseContract.UsuarioEntry;
import com.app.pandastock.models.Usuario;

public class UsuarioDao1 {
    private DatabaseHelper dbHelper;

    public UsuarioDao1(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean createUser(String nombres, String apellidos, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsuarioEntry.COL_NOMBRE, nombres);
        contentValues.put(UsuarioEntry.COL_APELLIDO, apellidos);
        contentValues.put(UsuarioEntry.COL_EMAIL, email);
        contentValues.put(UsuarioEntry.COL_CONTRASENA, password);
        long result = db.insert(UsuarioEntry.TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UsuarioEntry.TABLE_NAME + " WHERE " +
                UsuarioEntry.COL_EMAIL + "=? AND " +
                UsuarioEntry.COL_CONTRASENA + "=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Usuario getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UsuarioEntry.TABLE_NAME,
                new String[]{UsuarioEntry.COL_ID,
                        UsuarioEntry.COL_NOMBRE,
                        UsuarioEntry.COL_APELLIDO,
                        UsuarioEntry.COL_EMAIL},
                UsuarioEntry.COL_EMAIL + "=?",
                new String[]{email}, null, null, null);
        Usuario user = null;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(UsuarioEntry.COL_ID));
            @SuppressLint("Range") String nombres = cursor.getString(cursor.getColumnIndex(UsuarioEntry.COL_NOMBRE));
            @SuppressLint("Range") String apellidos = cursor.getString(cursor.getColumnIndex(UsuarioEntry.COL_APELLIDO));
            @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex(UsuarioEntry.COL_EMAIL));
            //user = new Usuario(id, nombres, apellidos, userEmail);
        }
        cursor.close();
        return user;
    }
}

