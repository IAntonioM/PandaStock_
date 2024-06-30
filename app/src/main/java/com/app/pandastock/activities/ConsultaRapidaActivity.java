package com.app.pandastock.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.pandastock.R;
import com.app.pandastock.firebase.MarcaDao;
import com.app.pandastock.firebase.ProductoDao;
import com.app.pandastock.firebase.TipoProductoDao;

public class ConsultaRapidaActivity extends AppCompatActivity {
    private ProductoDao productoDao;
    private TipoProductoDao tipoProductoDao;
    private MarcaDao marcaDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_rapida);
        tipoProductoDao = new TipoProductoDao(this);
        marcaDao = new MarcaDao(this);
        productoDao = new ProductoDao(this);
    }
}