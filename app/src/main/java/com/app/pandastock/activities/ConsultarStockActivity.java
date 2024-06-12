package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.app.pandastock.database.ProductoDao1;
import com.app.pandastock.models.Producto;

import java.util.List;

public class ConsultarStockActivity extends AppCompatActivity {
    TableLayout tableProductos;
    ProductoDao1 productoDao;
    ImageButton irMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_stock);

        tableProductos = findViewById(R.id.tableProductos);

        irMenu = findViewById(R.id.btnIrMenu);
        productoDao = new ProductoDao1(this);

        // Agregar encabezados de columna
        agregarEncabezados();
        cargarProductos();
        irMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        startActivity(new Intent(ConsultarStockActivity.this, MenuInicoActivity.class));
                        finish();

            }
        });
    }


    private void agregarEncabezados() {
        TableRow row = new TableRow(this);
        row.setPadding(0, 0, 0, 16); // Espacio inferior

        TextView headerProducto = new TextView(this);
        headerProducto.setText("Producto");
        headerProducto.setTypeface(null, android.graphics.Typeface.BOLD);
        headerProducto.setPadding(16, 16, 16, 16);
        row.addView(headerProducto);

        TextView headerMarca = new TextView(this);
        headerMarca.setText("Marca");
        headerMarca.setTypeface(null, android.graphics.Typeface.BOLD);
        headerMarca.setPadding(16, 16, 16, 16);
        row.addView(headerMarca);

        TextView headerModelo = new TextView(this);
        headerModelo.setText("Modelo");
        headerModelo.setTypeface(null, android.graphics.Typeface.BOLD);
        headerModelo.setPadding(16, 16, 16, 16);
        row.addView(headerModelo);

        TextView headerStock = new TextView(this);
        headerStock.setText("Stock");
        headerStock.setTypeface(null, android.graphics.Typeface.BOLD);
        headerStock.setPadding(16, 16, 16, 16);
        row.addView(headerStock);

        tableProductos.addView(row);
    }

    private void cargarProductos() {
        List<Producto> productos = productoDao.getAllProducts();

        if (productos.isEmpty()) {
            Toast.makeText(ConsultarStockActivity.this, "No se encontraron productos.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Producto producto : productos) {
            agregarFilaATabla(producto);
        }
    }

    private void agregarFilaATabla(Producto producto) {
        TableRow row = new TableRow(this);
        row.setPadding(0, 0, 0, 8); // Espacio inferior

        TextView tvProducto = new TextView(this);
        tvProducto.setText(String.valueOf(producto.getTipoProductoRef())); // Assuming category ID is the product name
        tvProducto.setPadding(16, 16, 16, 16);
        row.addView(tvProducto);

        TextView tvMarca = new TextView(this);
        tvMarca.setText(String.valueOf(producto.getMarcaRef())); // Assuming brand ID is the brand name
        tvMarca.setPadding(16, 16, 16, 16);
        row.addView(tvMarca);

        TextView tvModelo = new TextView(this);
        tvModelo.setText(producto.getModelo());
        tvModelo.setPadding(16, 16, 16, 16);
        row.addView(tvModelo);

        TextView tvStock = new TextView(this);
        tvStock.setText(String.valueOf(producto.getStock()));
        tvStock.setPadding(16, 16, 16, 16);
        row.addView(tvStock);

        tableProductos.addView(row);
    }
}
