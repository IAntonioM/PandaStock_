package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.app.pandastock.firebase.ProductoDao;
import com.app.pandastock.models.Producto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import com.app.pandastock.firebase.FirestoreContract.MarcaEntry;
import com.app.pandastock.firebase.FirestoreContract.TipoProductoEntry;

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
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, MenuInicoActivity.class);
            startActivity(intent);
            finish();});

        // Configurar botón para agregar nuevo producto
        btnNuevoProducto.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, IngresoProductoActivity.class);
            startActivity(intent);
            finish();
        });

        // Configurar botón de filtro (puedes agregar más lógica aquí)
        findViewById(R.id.btnFiltrar).setOnClickListener(v -> {
            // Filtrar productos según los criterios seleccionados
            // cargarProductosFiltrados();
        });
    }

    private void cargarProductos() {
        productoDao.getAllProducts(new ProductoDao.FirestoreCallback<List<Producto>>() {
            @Override
            public void onComplete(List<Producto> productos) {
                llProductList.removeAllViews();

                if (productos != null) {
                    for (Producto producto : productos) {
                        View cardView = getLayoutInflater().inflate(R.layout.card_producto, null);

                        TextView tvTipoProducto = cardView.findViewById(R.id.tvTipoProducto);
                        TextView tvMarca = cardView.findViewById(R.id.tvPrecio);
                        TextView tvModelo = cardView.findViewById(R.id.tvModelo);
                        TextView tvStock = cardView.findViewById(R.id.tvStock);
                        TextView tvPrecio = cardView.findViewById(R.id.tvMarca);
                        Button btnEditar = cardView.findViewById(R.id.btnEditar);
                        Button btnAgregarInventario = cardView.findViewById(R.id.btnAgregarProductoVenta);

                        producto.getTipoProductoRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String tipoProductoNombre = documentSnapshot.getString(TipoProductoEntry.FIELD_NOMBRE);
                                    tvTipoProducto.setText("Producto: " + tipoProductoNombre);
                                }
                            }
                        });

                        // Obtener el nombre de la marca
                        producto.getMarcaRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String marcaNombre = documentSnapshot.getString(MarcaEntry.FIELD_NOMBRE);
                                    tvMarca.setText("Marca: " + marcaNombre);
                                }
                            }
                        });
                        tvModelo.setText("Modelo: "+producto.getModelo());
                        tvStock.setText("Stock: "+String.valueOf(producto.getStock()));
                        tvPrecio.setText("Precio: S/."+String.valueOf(producto.getPrecio()));

                        btnEditar.setOnClickListener(v -> {
                            // Lógica para editar producto
                        });

                        btnAgregarInventario.setOnClickListener(v -> {
                            Intent intent = new Intent(ProductosActivity.this, EscanearCodeBarActivity.class);
                            startActivity(intent);
                            finish();
                        });

                        llProductList.addView(cardView);
                  }
                } else {
                    // Mostrar mensaje de error o vacío
                }
            }
        });
    }
}

