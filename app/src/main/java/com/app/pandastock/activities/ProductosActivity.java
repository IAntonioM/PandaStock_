package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.app.pandastock.database.ProductoDao;
import com.app.pandastock.models.Producto;

import java.util.List;

public class ProductosActivity extends AppCompatActivity {
    private Spinner spinnerTipoProducto, spinnerMarca;
    private LinearLayout llProductList;
    private ProductoDao productoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        // Inicializar vistas
        spinnerTipoProducto = findViewById(R.id.spinnerTipoProducto);
        spinnerMarca = findViewById(R.id.spinnerMarca);
        llProductList = findViewById(R.id.llProductList);
        Button btnNuevoProducto = findViewById(R.id.btnNuevoProducto);
        ImageButton btnBack = findViewById(R.id.btnBack);

        productoDao = new ProductoDao(this);

        // Cargar productos
        cargarProductos();

        // Configurar botón de retroceso
        btnBack.setOnClickListener(v -> onBackPressed());

        // Configurar botón para agregar nuevo producto
        btnNuevoProducto.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, IngresoProductoActivity.class);
            startActivity(intent);
        });

        // Configurar botón de filtro (puedes agregar más lógica aquí)
        findViewById(R.id.btnFiltrar).setOnClickListener(v -> {
            // Filtrar productos según los criterios seleccionados
            // cargarProductosFiltrados();
        });
    }

    private void cargarProductos() {
        List<Producto> productos = productoDao.getAllProducts();
        llProductList.removeAllViews();

        for (Producto producto : productos) {
            View cardView = getLayoutInflater().inflate(R.layout.card_producto, null);

            TextView tvTipoProducto = cardView.findViewById(R.id.tvTipoProducto);
            TextView tvMarca = cardView.findViewById(R.id.tvMarca);
            TextView tvModelo = cardView.findViewById(R.id.tvModelo);
            TextView tvStock = cardView.findViewById(R.id.tvStock);
            TextView tvPrecio = cardView.findViewById(R.id.tvPrecio);
            Button btnEditar = cardView.findViewById(R.id.btnEditar);
            Button btnAgregarInventario = cardView.findViewById(R.id.btnAgregarInventario);

            tvTipoProducto.setText(String.valueOf(producto.getTipoProductoId()));
            tvMarca.setText(String.valueOf(producto.getMarcaId()));
            tvModelo.setText(producto.getModelo());
            tvStock.setText(String.valueOf(producto.getStock()));
            tvPrecio.setText(String.valueOf(producto.getPrecio()));

            btnEditar.setOnClickListener(v -> {
                // Lógica para editar producto
            });

            btnAgregarInventario.setOnClickListener(v -> {
                // Lógica para agregar inventario
            });

            llProductList.addView(cardView);
        }
    }

}
