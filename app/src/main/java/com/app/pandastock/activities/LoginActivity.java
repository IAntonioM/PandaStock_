package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.app.pandastock.firebase.UsuarioDao;
import com.app.pandastock.models.Usuario;
import com.app.pandastock.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    UsuarioDao userDao;
    SessionManager session;
    Button crear, ingresar;
    EditText email, password;
    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(this, currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MenuInicoActivity.class));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userDao = new UsuarioDao(this);
        session = new SessionManager(this);
        mAuth= FirebaseAuth.getInstance();
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        crear = findViewById(R.id.btnCrearCuentaa);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        ingresar = findViewById(R.id.btnIngresar);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emails, passwords;
                emails = email.getText().toString().trim();
                passwords = password.getText().toString().trim();

                if (TextUtils.isEmpty(emails) || TextUtils.isEmpty(passwords)) {
                    Toast.makeText(LoginActivity.this, "Los campos Email o Contraseña están vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }

                userDao.login(emails, passwords, new UsuarioDao.FirestoreCallback<Boolean>() {
                    @Override
                    public void onComplete(Boolean isSuccessful) {
                        if (isSuccessful) {
                            Toast.makeText(LoginActivity.this, "Inicio de Sesión Exitoso !!!", Toast.LENGTH_SHORT).show();
                            // Aquí puedes agregar la lógica adicional si es necesario
                            Intent intent = new Intent(LoginActivity.this, MenuInicoActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error al Iniciar Sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}



