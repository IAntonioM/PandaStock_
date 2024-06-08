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

public class RegisterActivity extends AppCompatActivity {

    UsuarioDao userDao;
    Button concuenta,crearCuenta;
    EditText nombres,apellidos,email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDao = new UsuarioDao(this);
        nombres= findViewById(R.id.txtNombres);
        apellidos= findViewById(R.id.txtApellidos);
        email= findViewById(R.id.txtEmail1);
        password= findViewById(R.id.txtPassword1);
        concuenta = findViewById(R.id.btnYatienescuenta);
        crearCuenta= findViewById(R.id.btnCrearcuenta);
        concuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombress, apellidoss, emails, passwords;
                nombress = nombres.getText().toString().trim();
                apellidoss = apellidos.getText().toString().trim();
                emails = email.getText().toString().trim();
                passwords = password.getText().toString().trim();

                if (TextUtils.isEmpty(nombress) || TextUtils.isEmpty(apellidoss)) {
                    Toast.makeText(RegisterActivity.this, "Los campos Nombres o Apellidos están vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(emails) || TextUtils.isEmpty(passwords)) {
                    Toast.makeText(RegisterActivity.this, "Los campos Email o Contraseña están vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isCreate = userDao.createUser(nombress, apellidoss, emails, passwords);
                if (isCreate) {
                    Toast.makeText(RegisterActivity.this, "Cuenta Creada Existosamente !!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Error al Crear Cuenta", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}