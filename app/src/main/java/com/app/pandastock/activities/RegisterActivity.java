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
import com.google.firebase.auth.FirebaseAuth;

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

                // Validar el formato del email
                if (!isValidEmail(emails)) {
                    Toast.makeText(RegisterActivity.this, "Por favor, ingresa un email válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar que la contraseña tenga al menos 6 caracteres
                if (passwords.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                userDao.register(nombress, apellidoss, emails, passwords, new UsuarioDao.FirestoreCallback<Boolean>() {
                    @Override
                    public void onComplete(Boolean isCreate) {
                        if (isCreate) {Toast.makeText(RegisterActivity.this, "Cuenta Creada Existosamente !!!", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error al Crear Cuenta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

