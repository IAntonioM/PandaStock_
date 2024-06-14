package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.app.pandastock.firebase.MarcaDao;
import com.app.pandastock.firebase.MovimientoInventarioDao;
import com.app.pandastock.firebase.ProductoDao;
import com.app.pandastock.firebase.TipoProductoDao;
import com.app.pandastock.models.Marca;
import com.app.pandastock.models.MovimientoInventario;
import com.app.pandastock.models.TipoProducto;
import com.app.pandastock.utils.SessionManager;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngresoProductoActivity extends AppCompatActivity {

    Spinner tipoProduc1, marca1;
    ProductoDao productoDao;
    TipoProductoDao tipoProductoDao;
    MovimientoInventarioDao movimientoInventarioDao;
    SessionManager sessionManager;
    MarcaDao marcaDao;
    EditText cant, model, preci;
    ImageButton btn1, btn2, btn3;
    private boolean isTipoProductosLoaded = false;

    private Map<String, String> tipoProductoMap = new HashMap<>();
    private Map<String, String> marcaMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_producto);
        tipoProduc1 = findViewById(R.id.spnTipoProducto);
        cant = findViewById(R.id.edtCantidad);
        model = findViewById(R.id.edtModelo1);
        marca1 = findViewById(R.id.snpMarca);
        preci = findViewById(R.id.edtPrecio);
        btn1 = findViewById(R.id.btningresarp);
        btn2 = findViewById(R.id.btndenegar);
        btn3 = findViewById(R.id.btnregresarp);
        sessionManager = new SessionManager(this);
        productoDao = new ProductoDao(this);
        tipoProductoDao = new TipoProductoDao(this);
        movimientoInventarioDao = new MovimientoInventarioDao(this);
        marcaDao = new MarcaDao(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Registrar();
        Regresar();
        Denegar();
        loadTipoProductos();

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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(IngresoProductoActivity.this, android.R.layout.simple_spinner_item, tipProductList);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(IngresoProductoActivity.this, android.R.layout.simple_spinner_item, marcasList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marca1.setAdapter(adapter);
                });
            }
        });
    }

    private void Registrar() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Obtener los valores ingresados por el usuario
                    String tipoProducto = tipoProduc1.getSelectedItem().toString();
                    String marca = marca1.getSelectedItem().toString();
                    String cantidadStr = cant.getText().toString();
                    String modelo = model.getText().toString();
                    String precioStr = preci.getText().toString();

                    // Obtener el ID del tipo de producto y el ID de la marca seleccionados
                    String idTipoProducto = tipoProductoMap.get(tipoProducto);
                    String idMarca = marcaMap.get(marca);
                    if (idTipoProducto == null || idMarca == null) {
                        Toast.makeText(IngresoProductoActivity.this, "Por favor, selecciona un tipo de producto y una marca válidos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(IngresoProductoActivity.this, "Tipo: " + idTipoProducto + " marca: " + idMarca + " cantidad: " + cantidadStr + " precio: " + precioStr, Toast.LENGTH_SHORT).show();
                    // Validar que se hayan ingresado todos los campos necesarios
                    if (tipoProducto.isEmpty() || marca.isEmpty() || cantidadStr.isEmpty() || modelo.isEmpty() || precioStr.isEmpty()) {
                        Toast.makeText(IngresoProductoActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Convertir los valores de cantidad y precio a los tipos de datos necesarios
                    int cantidad = Integer.parseInt(cantidadStr);
                    double precio = Double.parseDouble(precioStr);
                    // Aquí puedes agregar la lógica para insertar el producto en la base de datos usando Firestore
                    productoDao.createProduct(idTipoProducto, idMarca, modelo, precio, cantidad, new ProductoDao.FirestoreCallback<Boolean,String>() {
                        @Override
                        public void onComplete(Boolean result,String idProducto) {
                            if (result) {
                                Toast.makeText(IngresoProductoActivity.this, "Producto registrado exitosamente", Toast.LENGTH_SHORT).show();
                                RegistrarMovimiento(cantidad,"Ingreso",idTipoProducto);
                                Denegar();
                            } else {
                                Toast.makeText(IngresoProductoActivity.this, "Error al registrar el producto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(IngresoProductoActivity.this, "Error al registrar el producto en la db", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void RegistrarMovimiento(int cantidad, String tipo, String idProducto) {
        movimientoInventarioDao.createMovimientoInv(sessionManager.getUserId(), idProducto, cantidad, tipo, new MovimientoInventarioDao.FirestoreCallback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(IngresoProductoActivity.this, "Movimiento registrado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(IngresoProductoActivity.this, "Error al registrar el movimiento", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(IngresoProductoActivity.this, "Error al registrar el movimiento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void Regresar() {
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IngresoProductoActivity.this, ProductosActivity.class));
                finish();
            }
        });
    }

    private void Denegar() {
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IngresoProductoActivity.this, "Datos eliminados.", Toast.LENGTH_SHORT).show();
                tipoProduc1.setSelection(0);
                marca1.setSelection(0);
                cant.setText(" ");
                preci.setText(" ");
                model.setText(" ");
            }
        });
    }
}
