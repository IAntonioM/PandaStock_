package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.app.pandastock.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {
    Button logout;
    ImageButton ingreso, salida, stock;
    SessionManager session;
    TextView useremail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        ingreso = findViewById(R.id.btnIngresoProductos);
        logout = findViewById(R.id.btnLogout);
        salida = findViewById(R.id.btnSalida);
        useremail = findViewById(R.id.txtUseremail);
        stock = findViewById(R.id.btnStock);

        String welcomeMessage = "Bienvenido, "+session.getUserId()+"-" + session.getUserNombres()+" "+session.getUserApellidos();
        useremail.setText(welcomeMessage);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                Toast.makeText(MenuActivity.this, "Cerrando Sesion",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                finish();
            }
        });

        ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, IngresoProductoActivity.class));
            }
        });

        salida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, SalidaProductoActivity.class));
            }
        });
        salida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, SalidaProductoActivity.class));
            }
        });
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, ConsultarStockActivity.class));
            }
        });

    }
}