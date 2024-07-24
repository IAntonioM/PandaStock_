package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.app.pandastock.R;
import com.app.pandastock.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuInicoActivity extends AppCompatActivity {

    Button logout;
    SessionManager session;
    TextView userInfo, empresaInfo;
    CardView productos, ventas, reportes, movimientoInventario, personal;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inico);

        session = new SessionManager(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Verificar si hay un usuario logueado
        if (user == null) {
            // No hay usuario logueado, redirigir a LoginActivity
            startActivity(new Intent(MenuInicoActivity.this, LoginActivity.class));
            finish(); // Finalizar MenuInicoActivity para evitar que el usuario regrese usando el botón "Atrás"
        } else {
            // Hay un usuario logueado, mostrar la interfaz de usuario
            setupUI();
        }
    }

    private void setupUI() {
        productos = findViewById(R.id.cardProductos);
        ventas = findViewById(R.id.cardVentas);
        movimientoInventario = findViewById(R.id.cardMovimientoInventario);
        reportes = findViewById(R.id.cardReporte);
        logout = findViewById(R.id.btnLogout1);
        userInfo = findViewById(R.id.txvUserInfo);
        personal = findViewById(R.id.cardGestionPersonal);
        empresaInfo = findViewById(R.id.txvEmpresa);

        empresaInfo.setText("Empresa: " + session.getEmpresa());
        String welcomeMessage = "Hola, " + session.getUsername();
        userInfo.setText(welcomeMessage);

        if ("Administrador".equals(session.getRol())) {
            personal.setVisibility(View.VISIBLE);
        } else {
            personal.setVisibility(View.GONE);
        }

        // Configurar listeners de clic para las tarjetas
        productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToProductos();
            }
        });

        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToVentas();
            }
        });

        movimientoInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMovimientoInventario();
            }
        });

        reportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToReportesDeVentas();
            }
        });

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToGestionPersonal();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                session.logout();
                Toast.makeText(MenuInicoActivity.this, "Cerrar sesión", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MenuInicoActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void navigateToProductos() {
        Intent intent = new Intent(MenuInicoActivity.this, ProductosActivity.class);
        startActivity(intent);
    }

    private void navigateToVentas() {
        Intent intent = new Intent(MenuInicoActivity.this, VentasActivity.class);
        startActivity(intent);
    }

    private void navigateToMovimientoInventario() {
        Intent intent = new Intent(MenuInicoActivity.this, MovimientoInventarioActivity.class);
        startActivity(intent);
    }

    private void navigateToReportesDeVentas() {
        Intent intent = new Intent(MenuInicoActivity.this, ReporteActivity.class);
        startActivity(intent);
    }

    private void navigateToGestionPersonal() {
        Intent intent = new Intent(MenuInicoActivity.this, ListPersonalActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finishAndRemoveTask();
    }
}
