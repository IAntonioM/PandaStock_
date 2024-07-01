package com.app.pandastock.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.app.pandastock.R;
import com.app.pandastock.firebase.FirestoreContract;
import com.app.pandastock.firebase.MovimientoInventarioDao;
import com.app.pandastock.firebase.ProductoDao;
import com.app.pandastock.firebase.TipoProductoDao;
import com.app.pandastock.models.MovimientoInventario;
import com.app.pandastock.models.Producto;
import com.app.pandastock.models.TipoProducto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimientoInventarioActivity extends AppCompatActivity {
    private Spinner tipoMovimiento,tipoProducto;
    private EditText usuarioMov;
    private Button buscar;
    private ImageButton btnback;
    private LinearLayout llMovInvenatrioList;

    private ProductoDao productoDao;
    private TipoProductoDao tipoProductoDao;
    private MovimientoInventarioDao movimientoInventarioDao;

    private Map<String, String> tipoProductoMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento_inventario);

        tipoMovimiento=findViewById(R.id.spnTipoMov);
        tipoProducto=findViewById(R.id.spnTipoProductoMov);
        usuarioMov=findViewById(R.id.edtUsuario);
        buscar=findViewById(R.id.btnBuscar1);
        llMovInvenatrioList=findViewById(R.id.llMovInvenatrioList);
        btnback=findViewById(R.id.btnBack);
        buscar=findViewById(R.id.btnBuscar1);

        tipoProductoDao = new TipoProductoDao(this);
        productoDao = new ProductoDao(this);
        movimientoInventarioDao = new MovimientoInventarioDao(this);

        loadTipoProductos();
        cargarMovimientosInventario();


        // Salir de ventas
        btnback.setOnClickListener(v ->{
            Intent intent = new Intent(MovimientoInventarioActivity.this, MenuInicoActivity.class);
            startActivity(intent);
            finish();
        });

        EditText etFechaMov = findViewById(R.id.edtFecha);
        etFechaMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MovimientoInventarioActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etFechaMov.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipoProductoSeleccionado = tipoProducto.getSelectedItem().toString();
                String tipoMovimientoSeleccionado = tipoMovimiento.getSelectedItem().toString();
                String usuarioSeleccionado = usuarioMov.getText().toString().trim();
                String fechaSeleccionada = etFechaMov.getText().toString().trim();

                movimientoInventarioDao.getAllMovimientoInv(new MovimientoInventarioDao.FirestoreCallback<List<MovimientoInventario>>() {
                    @Override
                    public void onComplete(List<MovimientoInventario> movimientosInventario) {
                        llMovInvenatrioList.removeAllViews();

                        if (movimientosInventario != null) {
                            for (MovimientoInventario movimientoInventario : movimientosInventario) {
                                // Aplicar filtros
                                boolean pasaFiltros = true;

                                if (!tipoProductoSeleccionado.equals("-- Seleccionar --") && !movimientoInventario.getProducto().equals(tipoProductoMap.get(tipoProductoSeleccionado))) {
                                    pasaFiltros = false;
                                }

                                if (!tipoMovimientoSeleccionado.equals("-- Seleccionar --") && !movimientoInventario.getTipo().equals(tipoMovimientoSeleccionado)) {
                                    pasaFiltros = false;
                                }

                                if (!usuarioSeleccionado.isEmpty() && !(movimientoInventario.getUsuario().equals(usuarioSeleccionado))) {
                                    pasaFiltros = false;
                                }

                                if (!fechaSeleccionada.isEmpty() && !(new SimpleDateFormat("dd/MM/yyyy").format(movimientoInventario.getFechaRegistro()).contains(fechaSeleccionada))) {
                                    pasaFiltros = false;
                                }

                                if (pasaFiltros) {
                                    // Mostrar el movimiento que pasa todos los filtros
                                    View cardView = getLayoutInflater().inflate(R.layout.item_movimiento_inventario, null);

                                    TextView codigoMov = cardView.findViewById(R.id.tvCodigoMov);
                                    TextView usuarioMov = cardView.findViewById(R.id.tvUsuarioMov);
                                    TextView tvProductoMov = cardView.findViewById(R.id.tvProductoMov);
                                    TextView cantidadMov = cardView.findViewById(R.id.tvCantidadMov);
                                    TextView tipoMov = cardView.findViewById(R.id.tvTipoMov);
                                    TextView fechaMov = cardView.findViewById(R.id.tvFechaMov);

                                    // Obtener información del producto
                                    movimientoInventario.getProducto().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String modelo = documentSnapshot.getString(FirestoreContract.ProductoEntry.FIELD_MODELO);
                                                tvProductoMov.setText("Producto: " + modelo);
                                            }
                                        }
                                    });

                                    movimientoInventario.getUsuario().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String nombres = documentSnapshot.getString(FirestoreContract.UsuarioEntry.FIELD_NOMBRE);
                                                String apellidos = documentSnapshot.getString(FirestoreContract.UsuarioEntry.FIELD_APELLIDO);
                                                usuarioMov.setText("Usuario: " + nombres + " " + apellidos);
                                            }
                                        }
                                    });

                                    codigoMov.setText("Código: " + movimientoInventario.getId());
                                    cantidadMov.setText("Cantidad: " + String.valueOf(movimientoInventario.getCantidad()));
                                    tipoMov.setText("Tipo: " + movimientoInventario.getTipo());
                                    fechaMov.setText("F. Registro: " + new SimpleDateFormat("dd/MM/yyyy").format(movimientoInventario.getFechaRegistro()));

                                    llMovInvenatrioList.addView(cardView);
                                }
                            }
                        } else {
                            // Mostrar mensaje de error o vacío
                            Toast.makeText(MovimientoInventarioActivity.this, "No se encontraron movimientos de inventario", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Manejar error de carga
                        Toast.makeText(MovimientoInventarioActivity.this, "Error al cargar los movimientos de inventario", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MovimientoInventarioActivity.this, MenuInicoActivity.class);
        startActivity(intent);
        finish();
    }
    private void loadTipoProductos() {
        tipoProductoDao.getAllTipoProductos(new TipoProductoDao.FirestoreCallback<List<TipoProducto>>() {
            @Override
            public void onComplete(List<TipoProducto> result) {
                ArrayList<String> tipProductList = new ArrayList<>();
                tipProductList.add("-- Seleccionar --");
                tipoProductoMap.clear();
                if (result != null) {
                    for (TipoProducto tipoProducto : result) {
                        tipProductList.add(tipoProducto.getNombre());
                        tipoProductoMap.put(tipoProducto.getNombre(), tipoProducto.getId());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MovimientoInventarioActivity.this, android.R.layout.simple_spinner_item, tipProductList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tipoProducto.setAdapter(adapter);
            }
        });
    }


    private void cargarMovimientosInventario() {
        movimientoInventarioDao.getAllMovimientoInv(new MovimientoInventarioDao.FirestoreCallback<List<MovimientoInventario>>() {
            @Override
            public void onComplete(List<MovimientoInventario> movimientosInventario) {
                llMovInvenatrioList.removeAllViews();

                if (movimientosInventario != null) {
                    for (MovimientoInventario movimientoInventario : movimientosInventario) {
                        View cardView = getLayoutInflater().inflate(R.layout.item_movimiento_inventario, null);

                        TextView codigoMov = cardView.findViewById(R.id.tvCodigoMov);
                        TextView usuarioMov = cardView.findViewById(R.id.tvUsuarioMov);
                        TextView tvProductoMov = cardView.findViewById(R.id.tvProductoMov);
                        TextView cantidadMov = cardView.findViewById(R.id.tvCantidadMov);
                        TextView tipoMov = cardView.findViewById(R.id.tvTipoMov);
                        TextView fechaMov = cardView.findViewById(R.id.tvFechaMov);
                        Toast.makeText(MovimientoInventarioActivity.this, "as:"+movimientoInventario.getProducto(), Toast.LENGTH_SHORT).show();

                        // Obtener información del producto
                        movimientoInventario.getProducto().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String modelo = documentSnapshot.getString(FirestoreContract.ProductoEntry.FIELD_MODELO);
                                    tvProductoMov.setText("Producto: " + modelo);
                                }
                            }
                        });

                        movimientoInventario.getUsuario().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String nombres = documentSnapshot.getString(FirestoreContract.UsuarioEntry.FIELD_NOMBRE);
                                    String apellidos = documentSnapshot.getString(FirestoreContract.UsuarioEntry.FIELD_APELLIDO);
                                    usuarioMov.setText("Usuario: " + nombres + " " + apellidos);
                                }
                            }
                        });

                        codigoMov.setText("Código: " + movimientoInventario.getId());
                        cantidadMov.setText("Cantidad: " + String.valueOf(movimientoInventario.getCantidad()));
                        tipoMov.setText("Tipo: " + movimientoInventario.getTipo());
                        fechaMov.setText("F. Registro: " + new SimpleDateFormat("dd/MM/yyyy").format(movimientoInventario.getFechaRegistro()));

                        llMovInvenatrioList.addView(cardView);
                    }
                } else {
                    // Mostrar mensaje de error o vacío
                    Toast.makeText(MovimientoInventarioActivity.this, "No se encontraron movimientos de inventario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Manejar error de carga
                Toast.makeText(MovimientoInventarioActivity.this, "Error al cargar los movimientos de inventario", Toast.LENGTH_SHORT).show();
            }
        });

    }

}