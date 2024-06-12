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
import com.app.pandastock.firebase.DetalleVentaDao;
import com.app.pandastock.firebase.VentaDao;
import com.app.pandastock.models.DetalleVenta;
import com.app.pandastock.models.Venta;
import com.app.pandastock.utils.SessionManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app.pandastock.firebase.FirestoreContract.UsuarioEntry;
import com.app.pandastock.firebase.FirestoreContract.VentaEntry;
import com.app.pandastock.firebase.FirestoreContract.ProductoEntry;

import java.util.Date;

public class RegistrarVentaActivity extends AppCompatActivity {
    private LinearLayout llProductList;
    private TextView tvMontoTotal;
    private VentaDao ventaDao;
    private DetalleVentaDao detalleVentaDao;
    private SessionManager session;
    private EditText nombresCliente, apellidosCliente, dni, celular;
    private double montoTotal = 0.0;
    private CollectionReference ventasCollectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ventasCollectionRef = db.collection(VentaEntry.COLLECTION_NAME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);
        ventaDao = new VentaDao();
        detalleVentaDao = new DetalleVentaDao();
        session = new SessionManager(this);
        nombresCliente = findViewById(R.id.etNombreCliente);
        apellidosCliente = findViewById(R.id.etApellidoCliente);
        dni = findViewById(R.id.etDniCliente);
        celular = findViewById(R.id.etCelularCliente);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        Button btnRegistrarVenta = findViewById(R.id.btnRegistrarVenta);
        btnRegistrarVenta.setOnClickListener(v -> {
            if (validateFields()) {
                registrarVenta();
            }
        });

        Button btnBuscarProducto = findViewById(R.id.btnBuscarProducto);
        btnBuscarProducto.setOnClickListener(v -> {
            Intent intent = new Intent(this, BuscarProductoActivity.class);
            startActivityForResult(intent, 1);
        });

        llProductList = findViewById(R.id.llProductList);
        tvMontoTotal = findViewById(R.id.tvMontoTotal);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String id = data.getStringExtra("id");
            String producto = data.getStringExtra("producto");
            String modelo = data.getStringExtra("modelo");
            String marca = data.getStringExtra("marca");
            double precioUnitario = data.getDoubleExtra("precioUnitario", 0.0);
            int stockProducto = data.getIntExtra("stock", 0);

            View productItem = getLayoutInflater().inflate(R.layout.item_producto_venta, null);

            TextView tvNombreProducto = productItem.findViewById(R.id.tvNombreProducto);
            TextView tvPrecioUnitario = productItem.findViewById(R.id.tvPrecioUnitario);
            TextView tvModeloProducto = productItem.findViewById(R.id.tvModelo);
            TextView tvMarcaProducto = productItem.findViewById(R.id.tvMarca);
            TextView tvProductId = productItem.findViewById(R.id.tvIdProducto);
            tvProductId.setText(id); // Almacenar el ID del producto

            tvNombreProducto.setText("Producto: " + producto);
            tvModeloProducto.setText("Modelo: " + modelo);
            tvMarcaProducto.setText("Marca: " + marca);
            tvPrecioUnitario.setText("S/." + precioUnitario);

            configureProductSpinner(productItem, stockProducto);
            llProductList.addView(productItem);
        }
    }

    private void configureProductSpinner(View productItem, int stockProducto) {
        Spinner spinnerCantidad = productItem.findViewById(R.id.spinnerCantidad);
        int maxCantidad = Math.min(stockProducto, 10); // Determinar el valor máximo entre el stock del producto y 10
        Integer[] cantidades = new Integer[maxCantidad];
        for (int i = 1; i <= maxCantidad; i++) {
            cantidades[i - 1] = i;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cantidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCantidad.setAdapter(adapter);

        spinnerCantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSubtotal(productItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateSubtotal(View productItem) {
        Spinner spinnerCantidad = productItem.findViewById(R.id.spinnerCantidad);
        int cantidad = (int) spinnerCantidad.getSelectedItem();

        TextView tvPrecioUnitario = productItem.findViewById(R.id.tvPrecioUnitario);
        double precioUnitario = Double.parseDouble(tvPrecioUnitario.getText().toString().replace("S/.", ""));

        double subtotal = cantidad * precioUnitario;

        TextView tvSubtotal = productItem.findViewById(R.id.tvSubtotal);
        tvSubtotal.setText("Subtotal: S/." + subtotal);

        updateMontoTotal();
    }

    private void updateMontoTotal() {
        montoTotal = 0.0;
        for (int i = 0; i < llProductList.getChildCount(); i++) {
            View productItem = llProductList.getChildAt(i);
            TextView tvSubtotal = productItem.findViewById(R.id.tvSubtotal);
            double subtotal = Double.parseDouble(tvSubtotal.getText().toString().replace("Subtotal: S/.", ""));
            montoTotal += subtotal;
        }
        tvMontoTotal.setText("Monto Total: S/." + montoTotal);
    }

    private boolean validateFields() {
        if (nombresCliente.getText().toString().isEmpty() ||
                apellidosCliente.getText().toString().isEmpty() ||
                dni.getText().toString().isEmpty() ||
                celular.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos del cliente", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (llProductList.getChildCount() == 0) {
            Toast.makeText(this, "Por favor, agregue al menos un producto", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registrarVenta() {
        // Crear la venta
        try {
            String isUsuario = session.getUserId();
            Venta venta = new Venta();
            venta.setNombreCliente(nombresCliente.getText().toString());
            venta.setApellidoCliente(apellidosCliente.getText().toString());
            venta.setCelular(celular.getText().toString());
            venta.setDni(dni.getText().toString());
            venta.setEmpleado(FirebaseFirestore.getInstance().document(UsuarioEntry.COLLECTION_NAME + "/" + isUsuario));
            venta.setMontoTotal(montoTotal);
            venta.setFechaCreacion(new Date());
            ventaDao.insertVenta(venta, new VentaDao.FirestoreCallback<Boolean, String>() {
                @Override
                public void onComplete(Boolean result, String idVenta) {
                    if (result) {
                        //Toast.makeText(RegistrarVentaActivity.this, "Venta Registrada Exitosamente", Toast.LENGTH_SHORT).show();
                        registrarDetallesVenta(idVenta);
                    } else {
                        Toast.makeText(RegistrarVentaActivity.this, "Error al registrar la venta", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error al registrar la venta", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarDetallesVenta(String idVenta) {
        try {

            for (int i = 0; i < llProductList.getChildCount(); i++) {
                View productItem = llProductList.getChildAt(i);
                TextView tvPrecioUnitario = productItem.findViewById(R.id.tvPrecioUnitario);
                TextView tvModelo = productItem.findViewById(R.id.tvModelo);
                String modelotv = tvModelo.getText().toString();
                Spinner spinnerCantidad = productItem.findViewById(R.id.spinnerCantidad);
                TextView tvSubtotal = productItem.findViewById(R.id.tvSubtotal);
                TextView tvProductId = productItem.findViewById(R.id.tvIdProducto);
                double precioUnitario = Double.parseDouble(tvPrecioUnitario.getText().toString().replace("S/.", ""));
                int cantidad = Integer.parseInt(spinnerCantidad.getSelectedItem().toString());
                double subtotal = Double.parseDouble(tvSubtotal.getText().toString().replace("Subtotal: S/.", ""));
                String idProducto = tvProductId.getText().toString();

                Toast.makeText(RegistrarVentaActivity.this, idProducto, Toast.LENGTH_SHORT).show();

                DetalleVenta detalleVenta = new DetalleVenta();
                DocumentReference ventaRef = ventasCollectionRef.document(idVenta);
                detalleVenta.setVenta(ventaRef);
                detalleVenta.setProducto(FirebaseFirestore.getInstance().document(ProductoEntry.COLLECTION_NAME + "/" + idProducto));
                detalleVenta.setCantidad(cantidad);
                detalleVenta.setPrecioUnitario(precioUnitario);
                detalleVenta.setSubtotal(subtotal);

                detalleVentaDao.insertDetalleVenta(detalleVenta, new DetalleVentaDao.FirestoreCallback<Boolean>() {
                    @Override
                    public void onComplete(Boolean result) {
                        if (result) {
                       } else {
                            Toast.makeText(RegistrarVentaActivity.this, "Error al agregar producto " + modelotv + " a la venta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(RegistrarVentaActivity.this, "Error al agregar los productos al dellate de venta", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(this, VentasActivity.class);
        startActivity(intent);
        finish();


    }

}




