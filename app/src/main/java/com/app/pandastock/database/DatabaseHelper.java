package com.app.pandastock.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PandaStock.db";

    // Table names
    public static final String TABLE_USER = "usuario";
    public static final String TABLE_PRODUCT = "producto";
    public static final String TABLE_CATEGORY = "categoria";
    public static final String TABLE_CUSTOMER = "customer";

    // User columns
    public static final String COL_USER_ID = "ID";
    public static final String COL_USER_NOMBRE = "NOMBRES";
    public static final String COL_USER_APELLIDO = "APELLIDOS";
    public static final String COL_USER_EMAIL = "EMAIL";
    public static final String COL_USER_PASSWORD = "PASSWORD";

    // Product columns
    public static final String COL_PRODUCT_ID = "ID";
    public static final String COL_PRODUCT_NOMBRE = "NOMBRE";
    public static final String COL_PRODUCT_BARCODE = "CODIGOBARRA";
    public static final String COL_PRODUCT_CATEGORIA_ID = "categoria_id";
    public static final String COL_PRODUCT_PRECIO = "precio";
    public static final String COL_PRODUCT_MODELO = "modelo";
    public static final String COL_PRODUCT_TIMESTAMP = "modelo";
    public static final String COL_PRODUCT_USUARIO_ID = "usuario_id";
    public static final String COL_PRODUCT_STOCK = "stock";

    // Category columns
    public static final String COL_CATEGORY_ID = "ID";
    public static final String COL_CATEGORY_NAME = "NAME";

    // Customer columns
    public static final String COL_CUSTOMER_ID = "ID";
    public static final String COL_CUSTOMER_NAME = "NAME";
    public static final String COL_CUSTOMER_EMAIL = "EMAIL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER_NOMBRE + " TEXT," +
                COL_USER_APELLIDO + " TEXT," +
                COL_USER_EMAIL + " TEXT," +
                COL_USER_PASSWORD + " TEXT)");

        /* db.execSQL("CREATE TABLE " + TABLE_PRODUCT + " (" +
                COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_NAME + " TEXT, " +
                COL_PRODUCT_CATEGORY_ID + " INTEGER, " +
                COL_PRODUCT_PRICE + " REAL)");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY_NAME + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CUSTOMER + " (" +
                COL_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CUSTOMER_NAME + " TEXT, " +
                COL_CUSTOMER_EMAIL + " TEXT)");

         */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        onCreate(db);
    }
}
