package com.app.pandastock.firebase;

import android.content.Context;
import android.widget.Toast;

import com.app.pandastock.models.VentaData;
import com.app.pandastock.models.VentaProductoData;
import com.app.pandastock.utils.SessionManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ReporteFirebase {

    private FirebaseFirestore db;
    private SessionManager sessionManager;
    private Context context;

    public ReporteFirebase(Context context) {
        this.db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(context);
        this.context=context;
    }

    public interface FirestoreCallback<T> {
        void onCallback(T dataEntries);
    }
    public interface FirestoreCallback2<T> {
        void onCallback(List<T> dataEntries);
    }

    public void obtenerDatosVentasPorTiempo(FirestoreCallback2<VentaData> callback) {
        CollectionReference ventasRef = db.collection(sessionManager.getEmpresa() + "_" + FirestoreContract.VentaEntry.COLLECTION_NAME);

        ventasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Double> ventasPorFecha = new HashMap<>();

                for (DocumentSnapshot document : task.getResult()) {
                    Double montoTotal = document.getDouble(FirestoreContract.VentaEntry.FIELD_MONTO_TOTAL);
                    Date fechaCreacion = document.getDate(FirestoreContract.VentaEntry.FIELD_FECHA_CREACION);

                    if (montoTotal != null && fechaCreacion != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
                        String fechaFormateada = dateFormat.format(fechaCreacion);

                        if (ventasPorFecha.containsKey(fechaFormateada)) {
                            Double montoActual = ventasPorFecha.get(fechaFormateada);
                            ventasPorFecha.put(fechaFormateada, montoActual + montoTotal);
                        } else {
                            ventasPorFecha.put(fechaFormateada, montoTotal);
                        }
                    }
                }

                List<VentaData> datos = new ArrayList<>();
                for (Map.Entry<String, Double> entry : ventasPorFecha.entrySet()) {
                    datos.add(new VentaData(entry.getKey(), entry.getValue()));
                }

                callback.onCallback(datos);
            } else {
                callback.onCallback(null);
            }
        });
    }

    public void obtenerDatosVentasPorProducto(FirestoreCallback<List<VentaProductoData>> callback) {
        CollectionReference detallesVentaRef = db.collection(sessionManager.getEmpresa() + "_" + FirestoreContract.DetalleVentaEntry.COLLECTION_NAME);
        detallesVentaRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Double> gananciasPorProducto = new HashMap<>();
                Map<String, Integer> cantidadPorProducto = new HashMap<>();

                for (DocumentSnapshot document : task.getResult()) {
                    DocumentReference productoRef = document.getDocumentReference(FirestoreContract.DetalleVentaEntry.FIELD_PRODUCTO_REF);
                    int cantidad = document.getLong(FirestoreContract.DetalleVentaEntry.FIELD_CANTIDAD).intValue();
                    double subtotal = document.getDouble(FirestoreContract.DetalleVentaEntry.FIELD_SUBTOTAL);
                    String productoId = productoRef.getId();

                    // Actualizar ganancias
                    Double gananciaActual = gananciasPorProducto.get(productoId);
                    gananciasPorProducto.put(productoId, (gananciaActual == null ? 0 : gananciaActual) + subtotal);

                    // Actualizar cantidad vendida
                    Integer cantidadActual = cantidadPorProducto.get(productoId);
                    cantidadPorProducto.put(productoId, (cantidadActual == null ? 0 : cantidadActual) + cantidad);
                }

                List<VentaProductoData> datos = new ArrayList<>();
                AtomicInteger pendingRequests = new AtomicInteger(gananciasPorProducto.size());

                for (Map.Entry<String, Double> entry : gananciasPorProducto.entrySet()) {
                    String productoId = entry.getKey();
                    double gananciaTotal = entry.getValue();
                    int cantidadTotal = cantidadPorProducto.get(productoId);

                    db.collection(sessionManager.getEmpresa() + "_" + FirestoreContract.ProductoEntry.COLLECTION_NAME)
                            .document(productoId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String modelo = documentSnapshot.getString(FirestoreContract.ProductoEntry.FIELD_MODELO);
                                    DocumentReference tipoProductoRef = documentSnapshot.getDocumentReference(FirestoreContract.ProductoEntry.FIELD_TIPO_PRODUCTO_REF);

                                    tipoProductoRef.get().addOnSuccessListener(tipoProductoSnapshot -> {
                                        if (tipoProductoSnapshot.exists()) {
                                            String tipoProducto = tipoProductoSnapshot.getString(FirestoreContract.TipoProductoEntry.FIELD_NOMBRE);
                                            datos.add(new VentaProductoData(cantidadTotal, gananciaTotal, tipoProducto + " - " + modelo));

                                            if (pendingRequests.decrementAndGet() == 0) {
                                                callback.onCallback(datos);
                                            }
                                        }
                                    });
                                }
                            });
                }
            } else {
                callback.onCallback(null);
            }
        });
    }




}

