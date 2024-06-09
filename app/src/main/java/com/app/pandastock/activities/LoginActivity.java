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
import com.app.pandastock.database.UsuarioDao;
import com.app.pandastock.models.Usuario;
import com.app.pandastock.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    UsuarioDao userDao;
    SessionManager session;
    Button crear, ingresar;
    EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userDao = new UsuarioDao(this);
        session = new SessionManager(this);

        email=findViewById(R.id.txtEmail);
        password=findViewById(R.id.txtPassword);
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
                boolean isExist = userDao.checkUser(emails, passwords);
                if (isExist) {
                    Toast.makeText(LoginActivity.this, "Inicio de Sesion Exitoso !!!", Toast.LENGTH_SHORT).show();

                    Usuario user = userDao.getUserByEmail(emails);
                    // Create login session
                    session.createLoginSession(user.getId(),user.getNombres(), user.getApellidos(), user.getEmail());
                    Intent intent=new Intent(LoginActivity.this,MenuInicoActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Error al Iniciar Sesion", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
