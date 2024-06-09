package com.app.pandastock.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.pandastock.database.DatabaseContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PandaStock.db";
    private static final int DATABASE_VERSION = 2;

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

        db.execSQL("CREATE TABLE " + VentaEntry.TABLE_NAME + " (" +
                VentaEntry.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                VentaEntry.COL_NOMBRE_CLIENTE + " TEXT," +
                VentaEntry.COL_APELLIDO_CLIENTE + " TEXT," +
                VentaEntry.COL_CELULAR + " TEXT," +
                VentaEntry.COL_DNI + " TEXT," +
                VentaEntry.COL_ID_EMPLEADO + " INTEGER NOT NULL," +
                VentaEntry.COL_MONTO_TOTAL + " REAL NOT NULL," +
                VentaEntry.COL_FECHA_CREACION + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (" + VentaEntry.COL_ID_EMPLEADO + ") REFERENCES " + UsuarioEntry.TABLE_NAME + "(" + UsuarioEntry.COL_ID + "))");

        db.execSQL("CREATE TABLE " + DetalleVentaEntry.TABLE_NAME + " (" +
                DetalleVentaEntry.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DetalleVentaEntry.COL_ID_VENTA + " INTEGER NOT NULL," +
                DetalleVentaEntry.COL_ID_PRODUCTO + " INTEGER NOT NULL," +
                DetalleVentaEntry.COL_CANTIDAD + " INTEGER NOT NULL," +
                DetalleVentaEntry.COL_PRECIO + " REAL NOT NULL," +
                DetalleVentaEntry.COL_SUBTOTAL + " REAL NOT NULL," +
                "FOREIGN KEY (" + DetalleVentaEntry.COL_ID_VENTA + ") REFERENCES " + VentaEntry.TABLE_NAME + "(" + VentaEntry.COL_ID + ")," +
                "FOREIGN KEY (" + DetalleVentaEntry.COL_ID_PRODUCTO + ") REFERENCES " + ProductoEntry.TABLE_NAME + "(" + ProductoEntry.COL_ID + "))");

        insertInitialTipoProductos(db);
        insertInitialMarcas(db);
        insertInitialUsuario(db);
        insertInitialVentas(db);
        insertInitialDetalleVentas(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsuarioEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MarcaEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TipoProductoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProductoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + InventarioProductoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VentaEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DetalleVentaEntry.TABLE_NAME);
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
    private void insertInitialVentas(SQLiteDatabase db) {
        String INSERT_VENTAS = "INSERT INTO " + VentaEntry.TABLE_NAME + " (" +
                VentaEntry.COL_NOMBRE_CLIENTE + ", " +
                VentaEntry.COL_APELLIDO_CLIENTE + ", " +
                VentaEntry.COL_CELULAR + ", " +
                VentaEntry.COL_DNI + ", " +
                VentaEntry.COL_ID_EMPLEADO + ", " +
                VentaEntry.COL_MONTO_TOTAL + ") VALUES " +
                "('Juan', 'Pérez', '123456789', '12345678', 1, 1500.0)," +
                "('María', 'Gómez', '987654321', '87654321', 1, 2000.0)," +
                "('Carlos', 'López', '456789123', '65432178', 1, 1800.0)";
        db.execSQL(INSERT_VENTAS);
    }

    private void insertInitialDetalleVentas(SQLiteDatabase db) {
        String INSERT_DETALLE_VENTAS = "INSERT INTO " + DetalleVentaEntry.TABLE_NAME + " (" +
                DetalleVentaEntry.COL_ID_VENTA + ", " +
                DetalleVentaEntry.COL_ID_PRODUCTO + ", " +
                DetalleVentaEntry.COL_CANTIDAD + ", " +
                DetalleVentaEntry.COL_PRECIO + ", " +
                DetalleVentaEntry.COL_SUBTOTAL + ") VALUES " +
                "(1, 1, 2, 500.0, 1000.0)," +
                "(1, 2, 1, 700.0, 700.0)," +
                "(2, 3, 3, 600.0, 1800.0)," +
                "(3, 1, 1, 500.0, 500.0)," +
                "(3, 2, 2, 700.0, 1400.0)";
        db.execSQL(INSERT_DETALLE_VENTAS);
    }

}
