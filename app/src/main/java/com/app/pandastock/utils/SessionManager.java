package com.app.pandastock.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_EMPRESA = "EMPRESA";
    private static final String KEY_USER_USUARIO = "USUARIO";
    private static final String KEY_USER_ROL = "ROL";
    private static final String KEY_USER_EMAIL = "userEmail";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSessionUser(String id, String empresa, String usuario, String email,String rol) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, id); // Almacenar el ID como cadena de texto
        editor.putString(KEY_USER_EMPRESA, empresa);
        editor.putString(KEY_USER_USUARIO, usuario);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ROL, rol);
        editor.apply(); // Usar apply() en lugar de commit()
    }

    public void createLoginSession(String empresa, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_EMPRESA, empresa);
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply(); // Usar apply() en lugar de commit()
    }


    public void logout() {
        // Limpiar todos los datos de la sesión
        editor.clear();
        editor.apply();

        // Opcionalmente, puedes establecer isLoggedIn a false explícitamente
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, null); // Obtener el ID como cadena de texto
    }
    public String getRol() {
        return pref.getString(KEY_USER_ROL, null);
    }
    public String getEmpresa() {
        return pref.getString(KEY_USER_EMPRESA, null);
    }

    public String getUsername() {
        return pref.getString(KEY_USER_USUARIO, null);
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, null);
    }
}