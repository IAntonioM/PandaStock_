package com.app.pandastock.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.pandastock.database.DatabaseContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PandaStock.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + UsuarioEntry.TABLE_NAME + " (" +
                UsuarioEntry.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UsuarioEntry.COL_NOMBRE + " TEXT NOT NULL," +
                UsuarioEntry.COL_APELLIDO + " TEXT NOT NULL," +
                UsuarioEntry.COL_EMAIL + " TEXT UNIQUE NOT NULL," +
                UsuarioEntry.COL_CONTRASENA + " TEXT)");

        db.execSQL("CREATE TABLE " + TipoProductoEntry.TABLE_NAME + " (" +
                TipoProductoEntry.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TipoProductoEntry.COL_NOMBRE + " TEXT UNIQUE NOT NULL)");

        db.execSQL("CREATE TABLE " + MarcaEntry.TABLE_NAME + " (" +
                MarcaEntry.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MarcaEntry.COL_NOMBRE + " TEXT NOT NULL," +
                MarcaEntry.COL_TIPO_PRODUCTO_ID + " INTEGER," +
                "FOREIGN KEY (" + MarcaEntry.COL_TIPO_PRODUCTO_ID + ") REFERENCES " + TipoProductoEntry.TABLE_NAME + "(" + TipoProductoEntry.COL_ID + "))");


        db.execSQL("CREATE TABLE " + ProductoEntry.TABLE_NAME + " (" +
                ProductoEntry.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProductoEntry.COL_TIPO_PRODUCTO_ID + " INTEGER," +
                ProductoEntry.COL_MARCA_ID + " INTEGER," +
                ProductoEntry.COL_MODELO + " TEXT NOT NULL," +
                ProductoEntry.COL_PRECIO + " REAL NOT NULL," +
                ProductoEntry.COL_STOCK + " INTEGER DEFAULT 0," +
                ProductoEntry.COL_FECHA_CREACION + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                ProductoEntry.COL_FECHA_ACTUALIZACION + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (" + ProductoEntry.COL_TIPO_PRODUCTO_ID + ") REFERENCES " + TipoProductoEntry.TABLE_NAME + "(" + TipoProductoEntry.COL_ID + ")," +
                "FOREIGN KEY (" + ProductoEntry.COL_MARCA_ID + ") REFERENCES " + MarcaEntry.TABLE_NAME + "(" + MarcaEntry.COL_ID + "))");

        db.execSQL("CREATE TABLE " + InventarioProductoEntry.TABLE_NAME + " (" +
                InventarioProductoEntry.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                InventarioProductoEntry.COL_PRODUCTO_ID + " INTEGER," +
                InventarioProductoEntry.COL_CODIGO_BARRAS + " TEXT UNIQUE NOT NULL," +
                "FOREIGN KEY (" + InventarioProductoEntry.COL_PRODUCTO_ID + ") REFERENCES " + ProductoEntry.TABLE_NAME + "(" + ProductoEntry.COL_ID + "))");

        insertInitialTipoProductos(db);
        insertInitialMarcas(db);
        insertInitialUsuario(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsuarioEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MarcaEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TipoProductoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProductoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + InventarioProductoEntry.TABLE_NAME);
        onCreate(db);
    }
    private void insertInitialUsuario(SQLiteDatabase db) {
        String INSERT_USUARIO = "INSERT INTO " + UsuarioEntry.TABLE_NAME + " (" +
                UsuarioEntry.COL_NOMBRE + ", " +
                UsuarioEntry.COL_APELLIDO + ", " +
                UsuarioEntry.COL_EMAIL + ", " +
                UsuarioEntry.COL_CONTRASENA + ") VALUES ('Admin', 'User', 'prueba', '1234')";
        db.execSQL(INSERT_USUARIO);
    }

    private void insertInitialTipoProductos(SQLiteDatabase db) {
        String INSERT_TIPO_PRODUCTOS = "INSERT INTO " + TipoProductoEntry.TABLE_NAME + " (" + TipoProductoEntry.COL_NOMBRE + ") VALUES " +
                "('Laptop'), ('Mouse'), ('Cargador_Laptop'), ('Cargador_Celular'), ('Teclados'), ('Monitor')";
        db.execSQL(INSERT_TIPO_PRODUCTOS);
    }

    private void insertInitialMarcas(SQLiteDatabase db) {
        String INSERT_MARCAS = "INSERT INTO " + MarcaEntry.TABLE_NAME + " (" + MarcaEntry.COL_NOMBRE + ", " + MarcaEntry.COL_TIPO_PRODUCTO_ID + ") VALUES " +
                "('Lenovo', (SELECT " + TipoProductoEntry.COL_ID + " FROM " + TipoProductoEntry.TABLE_NAME + " WHERE " + TipoProductoEntry.COL_NOMBRE + "='Laptop'))," +
                "('Asus', (SELECT " + TipoProductoEntry.COL_ID + " FROM " + TipoProductoEntry.TABLE_NAME + " WHERE " + TipoProductoEntry.COL_NOMBRE + "='Laptop'))," +
                "('Logitech', (SELECT " + TipoProductoEntry.COL_ID + " FROM " + TipoProductoEntry.TABLE_NAME + " WHERE " + TipoProductoEntry.COL_NOMBRE + "='Mouse'))," +
                "('Cibertel', (SELECT " + TipoProductoEntry.COL_ID + " FROM " + TipoProductoEntry.TABLE_NAME + " WHERE " + TipoProductoEntry.COL_NOMBRE + "='Mouse'))," +
                "('Halion', (SELECT " + TipoProductoEntry.COL_ID + " FROM " + TipoProductoEntry.TABLE_NAME + " WHERE " + TipoProductoEntry.COL_NOMBRE + "='Teclados'))," +
                "('Micronics', (SELECT " + TipoProductoEntry.COL_ID + " FROM " + TipoProductoEntry.TABLE_NAME + " WHERE " + TipoProductoEntry.COL_NOMBRE + "='Teclados'))," +
                "('Samsung', (SELECT " + TipoProductoEntry.COL_ID + " FROM " + TipoProductoEntry.TABLE_NAME + " WHERE " + TipoProductoEntry.COL_NOMBRE + "='Monitor'))," +
                "('Acer', (SELECT " + TipoProductoEntry.COL_ID + " FROM " + TipoProductoEntry.TABLE_NAME + " WHERE " + TipoProductoEntry.COL_NOMBRE + "='Monitor'))";
        db.execSQL(INSERT_MARCAS);
    }

}
