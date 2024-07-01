package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.pandastock.R;
import com.app.pandastock.firebase.FirestoreContract;
import com.app.pandastock.firebase.MarcaDao;
import com.app.pandastock.firebase.ProductoDao;
import com.app.pandastock.firebase.TipoProductoDao;
import com.app.pandastock.models.Marca;
import com.app.pandastock.models.Producto;
import com.app.pandastock.models.TipoProducto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaRapidaActivity extends AppCompatActivity {
    private ProductoDao productoDao;
    private TipoProductoDao tipoProductoDao;
    private MarcaDao marcaDao;
    private Spinner spinnerTipoProducto, spinnerMarca;
    private LinearLayout llProductList;
    private boolean isTipoProductosLoaded = false;
    private  Button buscar;
    private EditText etModelo;

    private Map<String, String> tipoProductoMap = new HashMap<>();
    private Map<String, String> marcaMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_rapida);
        tipoProductoDao = new TipoProductoDao(this);
        marcaDao = new MarcaDao(this);
        productoDao = new ProductoDao(this);

        // Inicializar vistas
        spinnerTipoProducto = findViewById(R.id.spinnerTipoProducto);
        spinnerMarca = findViewById(R.id.spinnerMarca);
        llProductList = findViewById(R.id.llProductList);
        //Button btnNuevoProducto = findViewById(R.id.btnNuevoProducto);
        ImageButton btnBack = findViewById(R.id.btnBack);
        tipoProductoDao = new TipoProductoDao(this);
        marcaDao = new MarcaDao(this);
        productoDao = new ProductoDao(this);
        loadTipoProductos();
        //buscar=findViewById(R.id.btnBuscar1);
        etModelo=findViewById(R.id.etModelo);

        // Cargar productos
        cargarProductos();

        btnBack.setOnClickListener(v ->{
            Intent intent = new Intent(ConsultaRapidaActivity.this, MenuInicoActivity.class);
            startActivity(intent);
            finish();
        });



        // Agrega un listener al Spinner tipoProduc1
        spinnerTipoProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isTipoProductosLoaded) {
                    loadMarcas();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hagas nada si no se selecciona nada
            }
        });
    }


    private void cargarProductos() {
        productoDao.getAllProducts(new ProductoDao.FirestoreCallback<List<Producto>, Void>() {
            @Override
            public void onComplete(List<Producto> productos, Void aVoid) {
                llProductList.removeAllViews();

                if (productos != null) {
                    for (Producto producto : productos) {
                        View cardView = getLayoutInflater().inflate(R.layout.item_consulta_rapida, null);

                        TextView tvTipoProducto = cardView.findViewById(R.id.tvTipoProducto);
                        TextView tvMarca = cardView.findViewById(R.id.tvPrecio);
                        TextView tvModelo = cardView.findViewById(R.id.tvModelo);
                        TextView tvStock = cardView.findViewById(R.id.tvStock);
                        TextView tvPrecio = cardView.findViewById(R.id.tvMarca);
                        Button btnVenta = cardView.findViewById(R.id.btnVenta);

                        producto.getTipoProductoRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String tipoProductoNombre = documentSnapshot.getString(FirestoreContract.TipoProductoEntry.FIELD_NOMBRE);
                                    tvTipoProducto.setText("Producto: " + tipoProductoNombre);
                                }
                            }
                        });

                        // Obtener el nombre de la marca
                        producto.getMarcaRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String marcaNombre = documentSnapshot.getString(FirestoreContract.MarcaEntry.FIELD_NOMBRE);
                                    tvMarca.setText("Marca: " + marcaNombre);
                                }
                            }
                        });
                        tvModelo.setText("Modelo: "+producto.getModelo());
                        tvStock.setText("Stock: "+String.valueOf(producto.getStock()));
                        tvPrecio.setText("Precio: S/."+String.valueOf(producto.getPrecio()));

                        btnVenta.setOnClickListener(v -> {
                            // Lógica para editar producto
                        });

                        llProductList.addView(cardView);
                    }
                } else {
                    // Mostrar mensaje de error o vacío
                }
            }
        });
    }
    private void loadTipoProductos() {
        tipoProductoDao.getAllTipoProductos(new TipoProductoDao.FirestoreCallback<List<TipoProducto>>() {
            @Override
            public void onComplete(List<TipoProducto> result) {
                ArrayList<String> tipProductList = new ArrayList<>();
                tipProductList.add("-- Seleccionar --"); // Agregar primer ítem

                // Clear previous data
                tipoProductoMap.clear();

                if (result != null) {
                    for (TipoProducto tipoProducto : result) {
                        tipProductList.add(tipoProducto.getNombre());
                        tipoProductoMap.put(tipoProducto.getNombre(), tipoProducto.getId()); // Store name and ID
                    }
                }

                // Set the new adapter with the updated data
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ConsultaRapidaActivity.this, android.R.layout.simple_spinner_item, tipProductList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTipoProducto.setAdapter(adapter);

                isTipoProductosLoaded = true;
            }
        });
    }

    private void loadMarcas() {
        String tipoProductoSeleccionado = spinnerTipoProducto.getSelectedItem().toString();
        if (tipoProductoSeleccionado.equals("-- Seleccionar --") || tipoProductoSeleccionado.isEmpty()) {
            return; // Salir si no hay un tipo de producto seleccionado
        }

        marcaDao.getMarcasByTipoProducto(tipoProductoSeleccionado, new MarcaDao.FirestoreCallback<List<Marca>>() {
            @Override
            public void onComplete(List<Marca> result) {
                ArrayList<String> marcasList = new ArrayList<>();
                marcasList.add("-- Seleccionar --"); // Agregar primer ítem

                // Clear previous data
                marcaMap.clear();

                if (result != null) {
                    for (Marca marca : result) {
                        marcasList.add(marca.getNombre());
                        marcaMap.put(marca.getNombre(), marca.getId()); // Store name and ID
                    }
                }

                // Actualizar el adaptador en el hilo principal
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ConsultaRapidaActivity.this, android.R.layout.simple_spinner_item, marcasList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMarca.setAdapter(adapter);
                });
            }
        });
    }
}