package com.app.pandastock.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NOMBRES = "nombres";
    private static final String KEY_USER_APELLIDOS = "apellidos";
    private static final String KEY_USER_EMAIL = "userEmail";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int id, String nombres, String apellidos, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, id);  // Corregir: almacenar el ID como entero
        editor.putString(KEY_USER_NOMBRES, nombres);
        editor.putString(KEY_USER_APELLIDOS, apellidos);
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);  // Corregir: obtener el ID como entero
    }

    public String getUserNombres() {
        return pref.getString(KEY_USER_NOMBRES, null);
    }

    public String getUserApellidos() {
        return pref.getString(KEY_USER_APELLIDOS, null);
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, null);
    }
}

