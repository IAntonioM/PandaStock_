package com.app.pandastock.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;;
import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.app.pandastock.database.DatabaseContract.TipoProductoEntry;
import com.app.pandastock.database.DatabaseContract.MarcaEntry;
import com.app.pandastock.database.MarcaDao;
import com.app.pandastock.database.ProductoDao;
import com.app.pandastock.database.TipoProductoDao;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class IngresoProductoActivity extends AppCompatActivity {
    
    Spinner tipoProduc1, marca1;
    ProductoDao productoDao;
    TipoProductoDao tipoProductoDao;
    MarcaDao marcaDao;
    EditText cant, model, preci;
    ImageButton btn1, btn2, btn3;

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
        productoDao = new ProductoDao(this);
        tipoProductoDao = new TipoProductoDao(this);
        marcaDao = new MarcaDao(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Registrar();
        Seleccion();
        Regresar();
        Denegar();
        loadTipoProductos();

        // Agrega un listener al Spinner tipoProduc1
        tipoProduc1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadMarcas(); // Llama al método loadMarcas cuando se seleccione un tipo de producto
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hagas nada si no se selecciona nada
            }
        });
    }
    @SuppressLint("Range")
    private void loadTipoProductos() {
        Cursor resTipProduct = tipoProductoDao.getAllTipoProductos();
        ArrayList<String> tipProductList = new ArrayList<>();

        // Clear the previous data
        tipoProduc1.setAdapter(null);

        if (resTipProduct.moveToFirst()) {
            do {
                tipProductList.add(resTipProduct.getString(resTipProduct.getColumnIndex(TipoProductoEntry.COL_NOMBRE)));
            } while (resTipProduct.moveToNext());
        }
        resTipProduct.close(); // Make sure to close the cursor

        // Set the new adapter with the updated data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipProductList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoProduc1.setAdapter(adapter);
    }

    @SuppressLint("Range")
    private void loadMarcas() {
        String tipoProductoSeleccionado = tipoProduc1.getSelectedItem().toString();
        if (tipoProductoSeleccionado == null || tipoProductoSeleccionado.isEmpty()) {
            return; // Salir si no hay un tipo de producto seleccionado
        }
        Cursor resMarca = marcaDao.getMarcasByTipoProducto(tipoProductoSeleccionado);
        ArrayList<String> marcasList = new ArrayList<>();
        if (resMarca.moveToFirst()) {
            do {
                marcasList.add(resMarca.getString(resMarca.getColumnIndex(MarcaEntry.COL_NOMBRE)));
            } while (resMarca.moveToNext());
        }
        resMarca.close(); // Make sure to close the cursor

        // Actualizar el adaptador en el hilo principal
        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, marcasList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            marca1.setAdapter(adapter);
        });
    }
    private void Registrar(){
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
                int idTipoProducto = tipoProduc1.getSelectedItemPosition() + 1; // El ID del tipo de producto es la posición + 1
                int idMarca = marca1.getSelectedItemPosition()+1;
                Toast.makeText(IngresoProductoActivity.this, "Tipo:"+idTipoProducto+" marca:"+marca+" cantidad:"+cantidadStr+" precio:"+precioStr, Toast.LENGTH_SHORT).show();

                    // Validar que se hayan ingresado todos los campos necesarios
                if (tipoProducto.isEmpty() || marca.isEmpty() || cantidadStr.isEmpty() || modelo.isEmpty() || precioStr.isEmpty()) {
                    Toast.makeText(IngresoProductoActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convertir los valores de cantidad y precio a los tipos de datos necesarios
                int cantidad = Integer.parseInt(cantidadStr);
                double precio = Double.parseDouble(precioStr);
                    // Aquí puedes agregar la lógica para insertar el producto en la base de datos
                    // Supongamos que tienes un método en tu ProductoDao para insertar un producto
                    boolean productoInsertado = productoDao.createProduct(idTipoProducto, idMarca, modelo, precio, cantidad);

                    if (productoInsertado) {
                        Toast.makeText(IngresoProductoActivity.this, "Producto registrado exitosamente", Toast.LENGTH_SHORT).show();
                        Denegar();
                    } else {
                        Toast.makeText(IngresoProductoActivity.this, "Error al registrar el producto", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(IngresoProductoActivity.this, "Error al registrar el producto en la db", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void Seleccion(){

    }

    private void Regresar(){
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IngresoProductoActivity.this, MenuActivity.class));

            }
        });
    }

    private void Denegar(){
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IngresoProductoActivity.this, "Datos eliminados.",
                        Toast.LENGTH_SHORT).show();
                tipoProduc1.setSelection(0);
                marca1.setSelection(0);
                cant.setText(" ");
                preci.setText(" ");
                model.setText(" ");
            }
        });
    }
}