package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.app.pandastock.firebase.PersonalDao;
import com.app.pandastock.firebase.UsuarioDao;
import com.app.pandastock.models.Personal;
import com.app.pandastock.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    UsuarioDao userDao;
    PersonalDao personalDao;
    Button concuenta,crearCuenta;
    EditText empresa,email,password;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        personalDao= new PersonalDao(this);
        userDao = new UsuarioDao(this);
        empresa= findViewById(R.id.txtNombreEmpresa);
        email= findViewById(R.id.txtEmail1);
        password= findViewById(R.id.txtPassword1);
        concuenta = findViewById(R.id.btnYatienescuenta);
        crearCuenta= findViewById(R.id.btnCrearcuenta);
        sessionManager=new SessionManager(this);

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

                String empresas, emails, passwords;
                empresas = empresa.getText().toString().trim();
                emails = email.getText().toString().trim();
                passwords = password.getText().toString().trim();

                if (TextUtils.isEmpty(empresas) ) {
                    Toast.makeText(RegisterActivity.this, "Debe agregar su Nombre de Empresa", Toast.LENGTH_SHORT).show();
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

                userDao.register(empresas, emails, passwords, new UsuarioDao.FirestoreCallback<Boolean>() {
                    @Override
                    public void onComplete(Boolean isCreate) {
                        if (isCreate) {Toast.makeText(RegisterActivity.this, "Cuenta Creada Existosamente !!!", Toast.LENGTH_LONG).show();
                            Personal personal=new Personal(empresas,"Empresa",empresas,empresas,passwords,emails,"Administrador");
                            personalDao.addPerson(personal, new PersonalDao.OnPersonAddedListener() {
                                @Override
                                public void onSuccess() {
                                    // Código a ejecutar cuando la persona se añade exitosamente
                                    Log.d("PersonalDao", "Persona añadida con éxito");
                                    // Por ejemplo, puede mostrar un mensaje al usuario o actualizar la UI
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    // Código a ejecutar si ocurre un error al añadir la persona
                                    Log.e("PersonalDao", "Error al añadir persona", e);
                                    // Por ejemplo, puede mostrar un mensaje de error al usuario
                                }
                            });
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

