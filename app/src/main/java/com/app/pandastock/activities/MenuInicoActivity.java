package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.app.pandastock.R;
import com.app.pandastock.utils.SessionManager;

public class MenuInicoActivity extends AppCompatActivity {
    Button logout, escanear;
    ImageButton ingreso, salida, stock;
    SessionManager session;
    TextView userInfo;
    CardView consulta,productos,ventas,reportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inico);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            Intent intent = new Intent(MenuInicoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        consulta = findViewById(R.id.cardConsultaR);
        productos = findViewById(R.id.cardProductos);
        ventas = findViewById(R.id.cardVentas);
        reportes = findViewById(R.id.cardReporte);
        logout = findViewById(R.id.btnLogout1);
        userInfo = findViewById(R.id.txvUserInfo);

        String welcomeMessage = "Hola, "+ session.getUserNombres();
        userInfo.setText(welcomeMessage);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                Toast.makeText(MenuInicoActivity.this, "Cerrando Sesion... ",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MenuInicoActivity.this, LoginActivity.class));
                finish();
            }
        });
        // Configurar listeners de clic para las tarjetas
        consulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToConsultaRapida();
            }
        });

        productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProductos();
            }
        });

        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToVentas();
            }
        });

        reportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToReportesDeVentas();
            }
        });
    }

    // Funciones para navegar a las diferentes actividades
    private void navigateToConsultaRapida() {
        Intent intent = new Intent(MenuInicoActivity.this, ConsultarStockActivity.class);
        startActivity(intent);
    }

    private void navigateToProductos() {
        Intent intent = new Intent(MenuInicoActivity.this, ProductosActivity.class);
        startActivity(intent);
    }

    private void navigateToVentas() {
        Intent intent = new Intent(MenuInicoActivity.this, VentasActivity.class);
        startActivity(intent);
    }

    private void navigateToReportesDeVentas() {
    }

}