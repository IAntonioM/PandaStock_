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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class BuscarProductoActivity extends AppCompatActivity {

    private Spinner tipoProduc1,marca1;
    private LinearLayout llProductList;
    private ProductoDao productoDao;
    private TipoProductoDao tipoProductoDao;
    private MarcaDao marcaDao;
    private Button buscar;
    private EditText etModelo;
    private boolean isTipoProductosLoaded = false;

    private Map<String, String> tipoProductoMap = new HashMap<>();
    private Map<String, String> marcaMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);

        llProductList = findViewById(R.id.llProductList);
        ImageButton btnBack = findViewById(R.id.btnBack);
        tipoProduc1 = findViewById(R.id.spinnerTipoProducto);
        marca1 = findViewById(R.id.spinnerMarca);


        buscar = findViewById(R.id.btnBuscar1);
        etModelo = findViewById(R.id.etModelo);


        productoDao = new ProductoDao(this);
        tipoProductoDao = new TipoProductoDao(this);
        marcaDao = new MarcaDao(this);
        loadTipoProductos();

        cargarProductos();


        // Configurar botón de filtro (puedes agregar más lógica aquí)
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipoProductoSeleccionado = tipoProduc1.getSelectedItem().toString();
                String marcaSeleccionada = marca1.getSelectedItem().toString();
                String modeloIngresado = etModelo.getText().toString().trim();

                // Validar y aplicar filtros
                productoDao.getAllProducts(new ProductoDao.FirestoreCallback<List<Producto>, Void>() {
                    @Override
                    public void onComplete(List<Producto> productos, Void aVoid) {
                        llProductList.removeAllViews();

                        if (productos != null) {
                            for (Producto producto : productos) {
                                // Aplicar filtro por tipo de producto
                                if (!tipoProductoSeleccionado.equals("-- Seleccionar --") &&
                                        !producto.getTipoProductoRef().getId().equals(tipoProductoMap.get(tipoProductoSeleccionado))) {
                                    continue; // Saltar producto si no coincide con el tipo seleccionado
                                }

                                // Aplicar filtro por marca
                                if (!marcaSeleccionada.equals("-- Seleccionar --") &&
                                        !producto.getMarcaRef().getId().equals(marcaMap.get(marcaSeleccionada))) {
                                    continue; // Saltar producto si no coincide con la marca seleccionada
                                }

                                // Aplicar filtro por modelo
                                if (!modeloIngresado.isEmpty() &&
                                        !producto.getModelo().toLowerCase().contains(modeloIngresado.toLowerCase())) {
                                    continue; // Saltar producto si no contiene el modelo ingresado
                                }

                                // Mostrar el producto que pasa todos los filtros
                                View cardView = getLayoutInflater().inflate(R.layout.item_buscar_producto, null);
                                TextView tvTipoProducto = cardView.findViewById(R.id.tvTipoProducto);
                                TextView tvMarca = cardView.findViewById(R.id.tvMarca);
                                TextView tvModelo = cardView.findViewById(R.id.tvModelo);
                                TextView tvStock = cardView.findViewById(R.id.tvStock);
                                TextView tvPrecio = cardView.findViewById(R.id.tvPrecio);
                                Button btnAgregarInventario = cardView.findViewById(R.id.btnAgregarProductoVenta);
                                final String[] tipoProductoNombre = {""};
                                final String[] marcaNombre = {""};
                                // Obtener el nombre del tipo de producto
                                producto.getTipoProductoRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            tipoProductoNombre[0] = documentSnapshot.getString(FirestoreContract.TipoProductoEntry.FIELD_NOMBRE);
                                            tvTipoProducto.setText("Producto: " + tipoProductoNombre[0]);
                                        }
                                    }
                                });

                                // Obtener el nombre de la marca
                                producto.getMarcaRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            marcaNombre[0] = documentSnapshot.getString(FirestoreContract.MarcaEntry.FIELD_NOMBRE);
                                            tvMarca.setText("Marca: " + marcaNombre[0]);
                                        }
                                    }
                                });
                                tvModelo.setText("Modelo: " + producto.getModelo());
                                tvStock.setText("Stock: " + producto.getStock());
                                tvPrecio.setText("Precio: S/." + producto.getPrecio());
                                // Configurar el botón para devolver el producto seleccionado
                                btnAgregarInventario.setOnClickListener(v -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("id", producto.getId());
                                    intent.putExtra("producto", tipoProductoNombre[0]);
                                    intent.putExtra("modelo", producto.getModelo());
                                    intent.putExtra("marca", marcaNombre[0]);
                                    intent.putExtra("precioUnitario", producto.getPrecio());
                                    intent.putExtra("stock", producto.getStock());
                                    setResult(RESULT_OK, intent);
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
        });

        // Agrega un listener al Spinner tipoProduc1
        tipoProduc1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void cargarProductos() {
        productoDao.getAllProducts(new ProductoDao.FirestoreCallback<List<Producto>, Void>() {
            @Override
            public void onComplete(List<Producto> productos, Void aVoids) {
                llProductList.removeAllViews();
                if (productos != null) {
                    for (Producto producto : productos) {
                        View cardView = getLayoutInflater().inflate(R.layout.item_buscar_producto, null);
                        TextView tvTipoProducto = cardView.findViewById(R.id.tvTipoProducto);
                        TextView tvMarca = cardView.findViewById(R.id.tvMarca);
                        TextView tvModelo = cardView.findViewById(R.id.tvModelo);
                        TextView tvStock = cardView.findViewById(R.id.tvStock);
                        TextView tvPrecio = cardView.findViewById(R.id.tvPrecio);
                        Button btnAgregarInventario = cardView.findViewById(R.id.btnAgregarProductoVenta);
                        final String[] tipoProductoNombre = {""};
                        final String[] marcaNombre = {""};
                        // Obtener el nombre del tipo de producto
                        producto.getTipoProductoRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    tipoProductoNombre[0] = documentSnapshot.getString(FirestoreContract.TipoProductoEntry.FIELD_NOMBRE);
                                    tvTipoProducto.setText("Producto: " + tipoProductoNombre[0]);
                                }
                            }
                        });

                        // Obtener el nombre de la marca
                        producto.getMarcaRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    marcaNombre[0] = documentSnapshot.getString(FirestoreContract.MarcaEntry.FIELD_NOMBRE);
                                    tvMarca.setText("Marca: " + marcaNombre[0]);
                                }
                            }
                        });
                        tvModelo.setText("Modelo: " + producto.getModelo());
                        tvStock.setText("Stock: " + producto.getStock());
                        tvPrecio.setText("Precio: S/." + producto.getPrecio());
                        // Configurar el botón para devolver el producto seleccionado
                        btnAgregarInventario.setOnClickListener(v -> {
                            Intent intent = new Intent();
                            intent.putExtra("id", producto.getId());
                            intent.putExtra("producto", tipoProductoNombre[0]);
                            intent.putExtra("modelo", producto.getModelo());
                            intent.putExtra("marca", marcaNombre[0]);
                            intent.putExtra("precioUnitario", producto.getPrecio());
                            intent.putExtra("stock", producto.getStock());
                            setResult(RESULT_OK, intent);
                            finish();
                        });
                        llProductList.addView(cardView);
                    }
                } else {
                    Toast.makeText(BuscarProductoActivity.this, "No se encontraron productos.", Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BuscarProductoActivity.this, android.R.layout.simple_spinner_item, tipProductList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tipoProduc1.setAdapter(adapter);

                isTipoProductosLoaded = true;
            }
        });
    }

    private void loadMarcas() {
        String tipoProductoSeleccionado = tipoProduc1.getSelectedItem().toString();
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BuscarProductoActivity.this, android.R.layout.simple_spinner_item, marcasList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marca1.setAdapter(adapter);
                });
            }
        });
    }
}

