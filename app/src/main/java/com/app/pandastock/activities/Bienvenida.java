package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;

public class Bienvenida extends AppCompatActivity {

    Button siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        siguiente = findViewById(R.id.btnIngresaar);
        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Inicie sesión", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Bienvenida.this, LoginActivity.class));
            }
        });
    }
}
