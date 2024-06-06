package com.app.pandastock.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.pandastock.models.Usuario;

public class UsuarioDao {
    public static final String TABLE_USER = "usuario";
    private DatabaseHelper dbHelper;

    public UsuarioDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    public boolean createUser(String nombres, String apellidos, String email,String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOMBRES", nombres);
        contentValues.put("APELLIDOS", apellidos);
        contentValues.put("EMAIL", email);
        contentValues.put("PASSWORD", password);
        long result = db.insert(TABLE_USER, null, contentValues);
        return result != -1;
    }
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public Usuario getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID, NOMBRES, APELLIDOS, EMAIL FROM " + TABLE_USER + " WHERE EMAIL=?", new String[]{email});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String nombres = cursor.getString(cursor.getColumnIndex("NOMBRES"));
            @SuppressLint("Range") String apellidos = cursor.getString(cursor.getColumnIndex("APELLIDOS"));
            @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex("EMAIL"));
            cursor.close();
            return new Usuario(id, nombres, apellidos, userEmail);
        }
        cursor.close();
        return null;
    }


}
