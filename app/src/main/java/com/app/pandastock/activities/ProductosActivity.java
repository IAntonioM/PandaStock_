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

import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.app.pandastock.adapters.EditProductDialogFragment;
import com.app.pandastock.firebase.MarcaDao;
import com.app.pandastock.firebase.MovimientoInventarioDao;
import com.app.pandastock.firebase.ProductoDao;
import com.app.pandastock.firebase.TipoProductoDao;
import com.app.pandastock.models.Marca;
import com.app.pandastock.models.Producto;
import com.app.pandastock.models.TipoProducto;
import com.app.pandastock.utils.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.pandastock.firebase.FirestoreContract.MarcaEntry;
import com.app.pandastock.firebase.FirestoreContract.TipoProductoEntry;

public class ProductosActivity extends AppCompatActivity {
    private Spinner spinnerTipoProducto, spinnerMarca;
    private LinearLayout llProductList;
    private ProductoDao productoDao;
    private TipoProductoDao tipoProductoDao;
    private MarcaDao marcaDao;
    private boolean isTipoProductosLoaded = false;
    private  Button buscar;
    private EditText etModelo;
    private MovimientoInventarioDao movimientoInventarioDao;
    private SessionManager sessionManager;

    private Map<String, String> tipoProductoMap = new HashMap<>();
    private Map<String, String> marcaMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        // Inicializar vistas
        sessionManager = new SessionManager(this);
        movimientoInventarioDao=new MovimientoInventarioDao(this);
        spinnerTipoProducto = findViewById(R.id.spinnerTipoProducto);
        spinnerMarca = findViewById(R.id.spinnerMarca);
        llProductList = findViewById(R.id.llProductList);
        Button btnNuevoProducto = findViewById(R.id.btnNuevoProducto);
        ImageButton btnBack = findViewById(R.id.btnBack);
        tipoProductoDao = new TipoProductoDao(this);
        marcaDao = new MarcaDao(this);
        productoDao = new ProductoDao(this);
        loadTipoProductos();
        buscar=findViewById(R.id.btnBuscar1);
        etModelo=findViewById(R.id.etModelo);

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

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipoProductoSeleccionado = spinnerTipoProducto.getSelectedItem().toString();
                String marcaSeleccionada = spinnerMarca.getSelectedItem().toString();
                String modeloIngresado = etModelo.getText().toString().trim();

                // Validar y aplicar filtros
                productoDao.getAllProducts(new ProductoDao.FirestoreCallback<List<Producto>,Void>() {
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
                                View cardView = getLayoutInflater().inflate(R.layout.item_producto, null);

                                TextView tvTipoProducto = cardView.findViewById(R.id.tvTipoProducto);
                                TextView tvMarca = cardView.findViewById(R.id.tvPrecio);
                                TextView tvModelo = cardView.findViewById(R.id.tvModelo);
                                TextView tvStock = cardView.findViewById(R.id.tvStock);
                                TextView tvPrecio = cardView.findViewById(R.id.tvMarca);
                                Button btnEditar = cardView.findViewById(R.id.btnEditar);

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

                                btnEditar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int position = llProductList.indexOfChild(cardView); // Obtener la posición del producto en la lista
                                        Producto productoSeleccionado = productos.get(position); // Obtener el producto seleccionado

                                        EditProductDialogFragment editProductDialog = new EditProductDialogFragment(
                                                productoDao,
                                                productoSeleccionado,
                                                tipoProductoMap,
                                                marcaMap,
                                                marcaDao,
                                                ProductosActivity.this::cargarProductos,
                                                sessionManager,
                                                movimientoInventarioDao // Pasar el método cargarProductos como callback
                                        );

                                        editProductDialog.show(getSupportFragmentManager(), "EditProductDialogFragment");
                                    }
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
                        View cardView = getLayoutInflater().inflate(R.layout.item_producto, null);

                        TextView tvTipoProducto = cardView.findViewById(R.id.tvTipoProducto);
                        TextView tvMarca = cardView.findViewById(R.id.tvPrecio);
                        TextView tvModelo = cardView.findViewById(R.id.tvModelo);
                        TextView tvStock = cardView.findViewById(R.id.tvStock);
                        TextView tvPrecio = cardView.findViewById(R.id.tvMarca);
                        Button btnEditar = cardView.findViewById(R.id.btnEditar);

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



                        btnEditar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int position = llProductList.indexOfChild(cardView); // Obtener la posición del producto en la lista
                                Producto productoSeleccionado = productos.get(position); // Obtener el producto seleccionado

                                EditProductDialogFragment editProductDialog = new EditProductDialogFragment(
                                        productoDao,
                                        productoSeleccionado,
                                        tipoProductoMap,
                                        marcaMap,
                                        marcaDao,
                                        ProductosActivity.this::cargarProductos,
                                        sessionManager,
                                        movimientoInventarioDao // Pasar el método cargarProductos como callback
                                );

                                editProductDialog.show(getSupportFragmentManager(), "EditProductDialogFragment");
                            }
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProductosActivity.this, android.R.layout.simple_spinner_item, tipProductList);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ProductosActivity.this, android.R.layout.simple_spinner_item, marcasList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMarca.setAdapter(adapter);
                });
            }
        });
    }
}

