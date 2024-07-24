package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pandastock.R;
import com.app.pandastock.adapters.AdapterVentasPorDia;
import com.app.pandastock.adapters.AdapterVentasPorProducto;
import com.app.pandastock.firebase.DetalleVentaDao;
import com.app.pandastock.firebase.FirestoreContract;
import com.app.pandastock.firebase.ReporteFirebase;
import com.app.pandastock.firebase.VentaDao;
import com.app.pandastock.models.DetalleVenta;
import com.app.pandastock.models.ProductoData;
import com.app.pandastock.models.Venta;
import com.app.pandastock.models.VentaData;
import com.app.pandastock.models.VentaProductoData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReporteActivity extends AppCompatActivity {
    ReporteFirebase reporteFirebase;
    VentaDao ventaDao;
    DetalleVentaDao detalleVentaDao;
    LinearLayout llSalesList;
    Button pdf;

    private RecyclerView recyclerViewVentasPorDia;
    private RecyclerView recyclerViewProductosVendidos;

    private AdapterVentasPorDia adapterVentasPorDia;
    private AdapterVentasPorProducto adapterProductosVendidos;

    private List<VentaData> ventasPorDiaList = new ArrayList<>();
    private List<VentaProductoData> productosVendidosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        reporteFirebase = new ReporteFirebase(this);

        ventaDao = new VentaDao(this);

        detalleVentaDao = new DetalleVentaDao(this);

        recyclerViewVentasPorDia = findViewById(R.id.recyclerViewVentasPorDia);
        recyclerViewProductosVendidos = findViewById(R.id.recyclerViewProductosVendidos);


        pdf = findViewById(R.id.btnDescargarPDF);

        // Configurar los RecyclerView
        recyclerViewVentasPorDia.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductosVendidos.setLayoutManager(new LinearLayoutManager(this));

        // Configurar los adaptadores
        adapterVentasPorDia = new AdapterVentasPorDia(ventasPorDiaList);
        adapterProductosVendidos = new AdapterVentasPorProducto(productosVendidosList);

        recyclerViewVentasPorDia.setAdapter(adapterVentasPorDia);
        recyclerViewProductosVendidos.setAdapter(adapterProductosVendidos);

        llSalesList = findViewById(R.id.llMovInvenatrioList);

        cargarDatos();

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReporteActivity.this, PDFActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cargarDatos() {
        // Simular datos cargados desde la base de datos
        reporteFirebase.obtenerDatosVentasPorTiempo(dataEntries -> {
            if (dataEntries != null) {
                ventasPorDiaList.clear();
                ventasPorDiaList.addAll(dataEntries);
                adapterVentasPorDia.notifyDataSetChanged();
            }
        });

        reporteFirebase.obtenerDatosVentasPorProducto(dataEntries -> {
            if (dataEntries != null) {
                productosVendidosList.clear();
                productosVendidosList.addAll(dataEntries);
                adapterProductosVendidos.notifyDataSetChanged();
            }
        });


        // Notificar a los adaptadores que los datos han cambiado
        adapterVentasPorDia.notifyDataSetChanged();
        adapterProductosVendidos.notifyDataSetChanged();
        cargarVentas();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ReporteActivity.this, MenuInicoActivity.class);
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
                        Button btnDetalles = ventaItem.findViewById(R.id.btnDetalles);


                        Date fechaCreacion = venta.getFechaCreacion();
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); // Formato: 28/05/2022
                        String fechaFormateada = formatoFecha.format(fechaCreacion);

                        tvCVenta.setText("C.Venta: "+venta.getId());
                        tvCliente.setText("Cliente: "+venta.getNombreCliente() + " " + venta.getApellidoCliente());
                        tvDetalles.setText("Celular: " + venta.getCelular() + " | DNI: " + venta.getDni());
                        tvFecha.setText("Fecha: " + fechaFormateada);
                        tvMontoTotal.setText("Monto Total: S/ " + venta.getMontoTotal());

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReporteActivity.this);
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
                                String nombre = documentSnapshot.getString("fullName");
                                String apellido = documentSnapshot.getString("lastName");
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
                    Toast.makeText(ReporteActivity.this, "No se encontraron detalles para esta venta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}



