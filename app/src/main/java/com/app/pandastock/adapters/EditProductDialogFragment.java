package com.app.pandastock.adapters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.app.pandastock.R;
import com.app.pandastock.activities.ConsultaRapidaActivity;
import com.app.pandastock.activities.RegistrarVentaActivity;
import com.app.pandastock.firebase.MarcaDao;
import com.app.pandastock.firebase.MovimientoInventarioDao;
import com.app.pandastock.firebase.ProductoDao;
import com.app.pandastock.models.Marca;
import com.app.pandastock.models.Producto;
import com.app.pandastock.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditProductDialogFragment extends DialogFragment {

    private EditText edtModelo, edtPrecio, edtCantidad;
    private Spinner spnTipoProducto, spnMarca;
    private ProductoDao productoDao;
    private Producto producto;
    private Marca marca;
    private MarcaDao marcaDao;
    private Map<String, String> tipoProductoMap, marcaMap;
    private Runnable onProductUpdatedCallback;
    private MovimientoInventarioDao movimientoInventarioDao;
    private SessionManager sessionManager;
    public EditProductDialogFragment(ProductoDao productoDao, Producto producto, Map<String, String> tipoProductoMap, Map<String, String> marcaMap, MarcaDao marcaDao,Runnable onProductUpdatedCallback,SessionManager sessionManager,MovimientoInventarioDao movimientoInventarioDao ) {
        this.productoDao = productoDao;
        this.producto = producto;
        this.tipoProductoMap = tipoProductoMap;
        this.marcaMap = marcaMap;
        this.marcaDao = marcaDao;
        this.onProductUpdatedCallback = onProductUpdatedCallback;
        this.sessionManager = sessionManager;
        this.movimientoInventarioDao= movimientoInventarioDao;
    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = (int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.7); // El 70% del alto de la pantalla
            window.setAttributes(params);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_product, container, false);

        edtModelo = view.findViewById(R.id.edtEditModelo);
        edtPrecio = view.findViewById(R.id.edtEditPrecio);
        edtCantidad = view.findViewById(R.id.edtEditCantidad);
        spnTipoProducto = view.findViewById(R.id.spnEditTipoProducto);
        spnMarca = view.findViewById(R.id.spnEditMarca);
        Button btnGuardar = view.findViewById(R.id.btnEditGuardar);
        Button btnCancelar = view.findViewById(R.id.btnEditCancelar);

        // Set initial values
        edtModelo.setText(producto.getModelo());
        edtPrecio.setText(String.valueOf(producto.getPrecio()));
        edtCantidad.setText(String.valueOf(producto.getStock()));

        loadTipoProductos();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarProducto();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void loadTipoProductos() {
        ArrayList<String> tipProductList = new ArrayList<>();
        tipProductList.add("-- Seleccionar --"); // Agregar primer ítem
        tipProductList.addAll(tipoProductoMap.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tipProductList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoProducto.setAdapter(adapter);

        // Set selected value
        if (producto.getTipoProductoRef() != null) {
            for (Map.Entry<String, String> entry : tipoProductoMap.entrySet()) {
                if (entry.getValue().equals(producto.getTipoProductoRef().getId())) {
                    spnTipoProducto.setSelection(adapter.getPosition(entry.getKey()));
                    break;
                }
            }
        }

        spnTipoProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Cargando", Toast.LENGTH_SHORT).show();
                loadMarcas();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadMarcas() {
        String tipoProductoSeleccionado = spnTipoProducto.getSelectedItem().toString();
        if (tipoProductoSeleccionado.equals("-- Seleccionar --") || tipoProductoSeleccionado.isEmpty()) {
            return; // Salir si no hay un tipo de producto seleccionado
        }
        Toast.makeText(getContext(), "tema:"+tipoProductoSeleccionado, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "marca"+marca.getNombre(), Toast.LENGTH_SHORT).show();
                    }
                }

                // Actualizar el adaptador en el hilo principal
                getActivity().runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, marcasList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnMarca.setAdapter(adapter);
                    Toast.makeText(getContext(), "marca"+spnMarca.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                    // Set selected value if available
                    if (producto.getMarcaRef() != null) {
                        for (Map.Entry<String, String> entry : marcaMap.entrySet()) {
                            if (entry.getValue().equals(producto.getMarcaRef().getId())) {
                                spnMarca.setSelection(adapter.getPosition(entry.getKey()));
                                break;
                            }
                        }
                    }
                });
            }
        });
    }
    private void RegistrarMovimiento(int cantidad, String tipo, String idProducto) {
        movimientoInventarioDao.createMovimientoInv(sessionManager.getUserId(), idProducto, cantidad, tipo, new MovimientoInventarioDao.FirestoreCallback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                if (result) {
                    if (isAdded()) { // Verifica si el fragmento todavía está adjunto a la actividad
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Movimiento registrado exitosamente", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Error al registrar el movimiento", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (isAdded()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Error al registrar el movimiento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void editarProducto() {
        String modelo = edtModelo.getText().toString();
        String precioStr = edtPrecio.getText().toString();
        String cantidadStr = edtCantidad.getText().toString();
        String tipoProducto = spnTipoProducto.getSelectedItem().toString();
        String marca = spnMarca.getSelectedItem().toString();

        if (TextUtils.isEmpty(modelo) || TextUtils.isEmpty(precioStr) || TextUtils.isEmpty(cantidadStr) || tipoProducto.equals("-- Seleccionar --") || marca.equals("-- Seleccionar --")) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioStr);
        int cantidadNueva = Integer.parseInt(cantidadStr);
        int cantidadOriginal = producto.getStock(); // Guardar la cantidad original

        String idTipoProducto = tipoProductoMap.get(tipoProducto);
        String idMarca = marcaMap.get(marca);

        if (idTipoProducto == null || idMarca == null) {
            Toast.makeText(getContext(), "Selecciona un tipo de producto y marca válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        productoDao.updateProduct(producto.getId(), idTipoProducto, idMarca, modelo, precio, cantidadNueva, new ProductoDao.FirestoreCallback<Boolean, Void>() {
            @Override
            public void onComplete(Boolean result, Void aVoid) {
                if (result) {
                    Toast.makeText(getContext(), "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();

                    // Verificar la diferencia en la cantidad y registrar el movimiento
                    int diferenciaCantidad = cantidadNueva - cantidadOriginal;
                    if (diferenciaCantidad > 0) {
                        RegistrarMovimiento(diferenciaCantidad, "Ingreso", producto.getId());
                    } else if (diferenciaCantidad < 0) {
                        RegistrarMovimiento(-diferenciaCantidad, "Salida", producto.getId());
                    }

                    if (onProductUpdatedCallback != null) {
                        onProductUpdatedCallback.run(); // Ejecutar callback
                    }
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
