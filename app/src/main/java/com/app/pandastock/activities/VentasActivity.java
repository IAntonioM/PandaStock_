package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.app.pandastock.R;
import com.app.pandastock.firebase.DetalleVentaDao;
import com.app.pandastock.firebase.FirestoreContract;
import com.app.pandastock.firebase.VentaDao;
import com.app.pandastock.models.DetalleVenta;
import com.app.pandastock.models.Venta;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VentasActivity extends AppCompatActivity {
    Button nuevaVenta;
    VentaDao ventaDao;
    EditText etCodigoVenta;
    EditText etDatosCliente;
    Button buscar;
    DetalleVentaDao detalleVentaDao;
    LinearLayout llSalesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        // Instanciar
        ventaDao = new VentaDao();
        detalleVentaDao = new DetalleVentaDao();
        Button btnFiltrar = findViewById(R.id.btnBuscar1);
        llSalesList = findViewById(R.id.llMovInvenatrioList);
        ImageButton btnBack = findViewById(R.id.btnBack);
        buscar= findViewById(R.id.btnBuscar1);
        etDatosCliente=findViewById(R.id.edtCliente);
        etCodigoVenta=findViewById(R.id.edtCodigoVenta);
        // Cargar las ventas
        cargarVentas();

        btnFiltrar.setOnClickListener(v -> {
            // Lógica para filtrar las ventas
        });
        nuevaVenta = findViewById(R.id.btnNuevaVenta);
        nuevaVenta.setOnClickListener(v -> {
            Intent intent = new Intent(VentasActivity.this, RegistrarVentaActivity.class);
            startActivity(intent);
            finish();
        });

        // Salir de ventas
        btnBack.setOnClickListener(v ->{
            Intent intent = new Intent(VentasActivity.this, MenuInicoActivity.class);
            startActivity(intent);
            finish();
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datosCliente = etDatosCliente.getText().toString().trim();
                String codigoVenta = etCodigoVenta.getText().toString().trim();

                // Validar y aplicar filtros
                ventaDao.getAllVentas(new VentaDao.FirestoreCallback<List<Venta>, Void>() {
                    @Override
                    public void onComplete(List<Venta> ventas, Void id) {
                        llSalesList.removeAllViews();
                        if (ventas != null && !ventas.isEmpty()) {
                            for (Venta venta : ventas) {
                                boolean matches = true;

                                // Aplicar filtro por datos del cliente
                                if (!datosCliente.isEmpty()) {
                                    matches = matches && (
                                            (venta.getNombreCliente() != null && venta.getNombreCliente().contains(datosCliente)) ||
                                                    (venta.getApellidoCliente() != null && venta.getApellidoCliente().contains(datosCliente)) ||
                                                    (venta.getCelular() != null && venta.getCelular().contains(datosCliente)) ||
                                                    (venta.getDni() != null && venta.getDni().contains(datosCliente))
                                    );
                                }

                                // Aplicar filtro por código de venta
                                if (!codigoVenta.isEmpty() && (venta.getId() == null || !venta.getId().contains(codigoVenta))) {
                                    matches = false;
                                }

                                // Mostrar el producto que pasa todos los filtros
                                if (matches) {
                                    View ventaItem = getLayoutInflater().inflate(R.layout.item_venta, null);

                                    TextView tvCliente = ventaItem.findViewById(R.id.tvCliente);
                                    TextView tvCVenta = ventaItem.findViewById(R.id.tvCodigoVenta);
                                    TextView tvDetalles = ventaItem.findViewById(R.id.tvDetalles);
                                    TextView tvFecha = ventaItem.findViewById(R.id.tvFecha);
                                    TextView tvMontoTotal = ventaItem.findViewById(R.id.tvMontoTotal);
                                    Button btnEditar = ventaItem.findViewById(R.id.btnEditar3);
                                    Button btnDetalles = ventaItem.findViewById(R.id.btnDetalles);

                                    Date fechaCreacion = venta.getFechaCreacion();
                                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); // Formato: 28/05/2022
                                    String fechaFormateada = fechaCreacion != null ? formatoFecha.format(fechaCreacion) : "N/A";

                                    tvCVenta.setText("C. Venta: "+venta.getId());
                                    tvCliente.setText(venta.getNombreCliente() + " " + venta.getApellidoCliente());
                                    tvDetalles.setText("Celular: " + venta.getCelular() + " | DNI: " + venta.getDni());
                                    tvFecha.setText("Fecha: " + fechaFormateada);
                                    tvMontoTotal.setText("Monto Total: S/ " + venta.getMontoTotal());

                                    btnEditar.setOnClickListener(v -> {
                                        // Lógica para editar la venta
                                    });

                                    btnDetalles.setOnClickListener(v -> {
                                        mostrarDetallesVenta(venta);
                                    });
                                    llSalesList.addView(ventaItem);
                                }
                            }
                        } else {
                            Toast.makeText(VentasActivity.this, "No se encontraron ventas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VentasActivity.this, MenuInicoActivity.class);
        startActivity(intent);
        finish();
    }

    private void cargarVentas() {
        ventaDao.getAllVentas(new VentaDao.FirestoreCallback<List<Venta>, Void>() {
            @Override
            public void onComplete(List<Venta> ventas, Void id) {
                llSalesList.removeAllViews();
                if(ventas!=null){

                    for (Venta venta : ventas) {
                        View ventaItem = getLayoutInflater().inflate(R.layout.item_venta, null);

                            TextView tvCliente = ventaItem.findViewById(R.id.tvCliente);
                            TextView tvCVenta = ventaItem.findViewById(R.id.tvCodigoVenta);
                            TextView tvDetalles = ventaItem.findViewById(R.id.tvDetalles);
                            TextView tvFecha = ventaItem.findViewById(R.id.tvFecha);
                            TextView tvMontoTotal = ventaItem.findViewById(R.id.tvMontoTotal);
                            Button btnEditar = ventaItem.findViewById(R.id.btnEditar3);
                            Button btnDetalles = ventaItem.findViewById(R.id.btnDetalles);


                            Date fechaCreacion = venta.getFechaCreacion();
                            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); // Formato: 28/05/2022
                            String fechaFormateada = formatoFecha.format(fechaCreacion);

                            tvCVenta.setText("C.Venta: "+venta.getId());
                            tvCliente.setText("Cliente: "+venta.getNombreCliente() + " " + venta.getApellidoCliente());
                            tvDetalles.setText("Celular: " + venta.getCelular() + " | DNI: " + venta.getDni());
                            tvFecha.setText("Fecha: " + fechaFormateada);
                            tvMontoTotal.setText("Monto Total: S/ " + venta.getMontoTotal());

                            btnEditar.setOnClickListener(v -> {
                                // Lógica para editar la venta
                            });

                            btnDetalles.setOnClickListener(v -> {
                                mostrarDetallesVenta(venta);
                            });
                            llSalesList.addView(ventaItem);
                    }
                }else{

                }
            }
        });
    }
    private void mostrarDetallesVenta(Venta venta) {
        detalleVentaDao.getDetallesPorVenta(venta.getId(), new DetalleVentaDao.FirestoreCallback<List<DetalleVenta>>() {
            @Override
            public void onComplete(List<DetalleVenta> detallesVenta) {
                if (detallesVenta != null) {
                    // Crear y mostrar el modal con los detalles de la venta
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_detalle_venta, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(VentasActivity.this);
                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();

                    // Obtener referencias de los TextView del diálogo
                    TextView tvCodigoVenta = dialogView.findViewById(R.id.tvCodigoVenta);
                    TextView tvVendedor = dialogView.findViewById(R.id.tvNombresVendedor);
                    TextView tvMontoTotal = dialogView.findViewById(R.id.tvMontoTotal);

                    // Establecer los valores de los TextView con los datos de la venta
                    tvCodigoVenta.setText("Código Venta: " + venta.getId());

                    // Obtener el nombre del vendedor
                    venta.getEmpleado().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String nombre = documentSnapshot.getString(FirestoreContract.UsuarioEntry.FIELD_NOMBRE);
                                String apellido = documentSnapshot.getString(FirestoreContract.UsuarioEntry.FIELD_APELLIDO);
                                tvVendedor.setText("Vendedor: " + nombre + " " + apellido);
                            }
                        }
                    });

                    // Establecer el monto total de la venta
                    tvMontoTotal.setText("Monto Total: S/ " + venta.getMontoTotal());

                    LinearLayout llDetalleVenta = dialogView.findViewById(R.id.llDetalleVenta);
                    Button btnCerrar = dialogView.findViewById(R.id.btnCerrar);

                    for (DetalleVenta detalle : detallesVenta) {
                        View detalleItem = getLayoutInflater().inflate(R.layout.item_detalle_venta, null);

                        TextView tvProducto = detalleItem.findViewById(R.id.tvProducto);
                        TextView tvCantidad = detalleItem.findViewById(R.id.tvCantidad);
                        TextView tvPrecioUnitario = detalleItem.findViewById(R.id.tvPrecioUnitario);
                        TextView tvSubtotal = detalleItem.findViewById(R.id.tvSubtotal);

                        // Obtener el modelo del producto
                        detalle.getProducto().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String modelo = documentSnapshot.getString(FirestoreContract.ProductoEntry.FIELD_MODELO);
                                    tvProducto.setText("Producto: " + modelo);
                                }
                            }
                        });

                        // Establecer los valores de los TextView con los datos del detalle de venta
                        tvCantidad.setText("Cantidad: " + detalle.getCantidad());
                        tvPrecioUnitario.setText("Precio Unitario: S/ " + detalle.getPrecioUnitario());
                        tvSubtotal.setText("Subtotal: S/ " + detalle.getSubtotal());

                        llDetalleVenta.addView(detalleItem);
                    }

                    btnCerrar.setOnClickListener(v -> dialog.dismiss());

                    dialog.show();
                } else {
                    Toast.makeText(VentasActivity.this, "No se encontraron detalles para esta venta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
