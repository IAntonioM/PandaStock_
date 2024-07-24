package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pandastock.R;
import com.app.pandastock.adapters.PasswordDialogFragment;
import com.app.pandastock.adapters.UserAdapter;
import com.app.pandastock.models.Personal;
import com.app.pandastock.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MenuSelectUserActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<Personal> userList;
    private SessionManager sessionManager;
    private Button logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select_user);

        sessionManager = new SessionManager(this);

        // Verificar si ya hay un usuario autenticado
        if (sessionManager.getUsername() != null) {
            // Hay un usuario autenticado, redirigir a MenuInicoActivity
            startActivity(new Intent(MenuSelectUserActivity.this, MenuInicoActivity.class));
            finish(); // Finalizar la actividad actual para evitar regresar a ella
            return; // Terminar el método onCreate aquí
        }

        // Configuración del RecyclerView y Adapter para mostrar usuarios
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this::showPasswordDialog);
        recyclerViewUsers.setAdapter(userAdapter);

        // Configuración del botón de logout
        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cerrar sesión de FirebaseAuth y limpiar la sesión local
                FirebaseAuth.getInstance().signOut();
                sessionManager.logout();
                Toast.makeText(MenuSelectUserActivity.this, "Logout",
                        Toast.LENGTH_SHORT).show();
                // Redirigir a LoginActivity
                startActivity(new Intent(MenuSelectUserActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Obtener lista de usuarios desde Firestore
        fetchPersonnelUsers();
    }

    private void fetchPersonnelUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(sessionManager.getEmpresa() + "_persons")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Personal user = document.toObject(Personal.class);
                            userList.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MenuSelectUserActivity.this, "Error al obtener usuarios.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showPasswordDialog(Personal user) {
        PasswordDialogFragment dialog = new PasswordDialogFragment(user, this::authenticateUser);
        dialog.show(getSupportFragmentManager(), "PasswordDialogFragment");
    }

    private void authenticateUser(Personal user, String password) {
        if (user.getPassword().equals(password)) {
            // Crear sesión y redirigir a MenuInicoActivity
            sessionManager.createLoginSessionUser(user.getId(), sessionManager.getEmpresa(), user.getUsername(), user.getEmail(), user.getRole());
            Intent intent = new Intent(MenuSelectUserActivity.this, MenuInicoActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }
}
