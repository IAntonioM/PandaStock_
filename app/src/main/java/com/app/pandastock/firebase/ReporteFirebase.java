package com.app.pandastock.firebase;

import com.app.pandastock.firebase.FirestoreContract;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteFirebase {

    private FirebaseFirestore db;

    public ReporteFirebase() {
        this.db = FirebaseFirestore.getInstance();
    }

    public interface FirestoreCallback {
        void onCallback(List<DataEntry> dataEntries);
    }

    public void obtenerDatosProductosVendidos(FirestoreCallback callback) {
        CollectionReference ventasRef = db.collection(FirestoreContract.VentaEntry.COLLECTION_NAME);

        ventasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Integer> productosVendidos = new HashMap<>();

                for (QueryDocumentSnapshot ventaDoc : task.getResult()) {
                    CollectionReference detallesVentaRef = ventaDoc.getReference().collection(FirestoreContract.DetalleVentaEntry.COLLECTION_NAME);
                    detallesVentaRef.get().addOnCompleteListener(detalleTask -> {
                        if (detalleTask.isSuccessful()) {
                            for (QueryDocumentSnapshot detalleDoc : detalleTask.getResult()) {
                                String productoRef = detalleDoc.getString(FirestoreContract.DetalleVentaEntry.FIELD_PRODUCTO_REF);
                                int cantidad = detalleDoc.getLong(FirestoreContract.DetalleVentaEntry.FIELD_CANTIDAD).intValue();

                                if (productosVendidos.containsKey(productoRef)) {
                                    productosVendidos.put(productoRef, productosVendidos.get(productoRef) + cantidad);
                                } else {
                                    productosVendidos.put(productoRef, cantidad);
                                }
                            }
                            List<DataEntry> dataEntries = new ArrayList<>();
                            for (Map.Entry<String, Integer> entry : productosVendidos.entrySet()) {
                                dataEntries.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                            }
                            callback.onCallback(dataEntries);
                        }
                    });
                }
            }
        });
    }

    public void obtenerDatosTipoProductoVendido(FirestoreCallback callback) {
        CollectionReference ventasRef = db.collection(FirestoreContract.VentaEntry.COLLECTION_NAME);

        ventasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Integer> tiposProductoVendidos = new HashMap<>();

                for (QueryDocumentSnapshot ventaDoc : task.getResult()) {
                    CollectionReference detallesVentaRef = ventaDoc.getReference().collection(FirestoreContract.DetalleVentaEntry.COLLECTION_NAME);
                    detallesVentaRef.get().addOnCompleteListener(detalleTask -> {
                        if (detalleTask.isSuccessful()) {
                            for (QueryDocumentSnapshot detalleDoc : detalleTask.getResult()) {
                                DocumentReference productoRef = db.document(detalleDoc.getString(FirestoreContract.DetalleVentaEntry.FIELD_PRODUCTO_REF));
                                productoRef.get().addOnCompleteListener(productoTask -> {
                                    if (productoTask.isSuccessful()) {
                                        String tipoProductoRef = productoTask.getResult().getString(FirestoreContract.ProductoEntry.FIELD_TIPO_PRODUCTO_REF);
                                        int cantidad = detalleDoc.getLong(FirestoreContract.DetalleVentaEntry.FIELD_CANTIDAD).intValue();

                                        if (tiposProductoVendidos.containsKey(tipoProductoRef)) {
                                            tiposProductoVendidos.put(tipoProductoRef, tiposProductoVendidos.get(tipoProductoRef) + cantidad);
                                        } else {
                                            tiposProductoVendidos.put(tipoProductoRef, cantidad);
                                        }
                                    }
                                });
                            }
                            List<DataEntry> dataEntries = new ArrayList<>();
                            for (Map.Entry<String, Integer> entry : tiposProductoVendidos.entrySet()) {
                                dataEntries.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                            }
                            callback.onCallback(dataEntries);
                        }
                    });
                }
            }
        });
    }

    public void obtenerDatosVentasPorTiempo(FirestoreCallback callback) {
        CollectionReference ventasRef = db.collection(FirestoreContract.VentaEntry.COLLECTION_NAME);

        ventasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Integer> ventasPorFecha = new HashMap<>();

                for (QueryDocumentSnapshot ventaDoc : task.getResult()) {
                    String fecha = ventaDoc.getDate(FirestoreContract.VentaEntry.FIELD_FECHA_CREACION).toString();
                    int montoTotal = ventaDoc.getLong(FirestoreContract.VentaEntry.FIELD_MONTO_TOTAL).intValue();

                    if (ventasPorFecha.containsKey(fecha)) {
                        ventasPorFecha.put(fecha, ventasPorFecha.get(fecha) + montoTotal);
                    } else {
                        ventasPorFecha.put(fecha, montoTotal);
                    }
                }
                List<DataEntry> dataEntries = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : ventasPorFecha.entrySet()) {
                    dataEntries.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                }
                callback.onCallback(dataEntries);
            }
        });
    }

    public void obtenerDatosMovimientoInventario(FirestoreCallback callback) {
        CollectionReference movimientoInventarioRef = db.collection(FirestoreContract.MovimientoInventarioEntry.COLLECTION_NAME);

        movimientoInventarioRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, int[]> movimientosInventario = new HashMap<>();

                for (QueryDocumentSnapshot movimientoDoc : task.getResult()) {
                    String productoRef = movimientoDoc.getString(FirestoreContract.MovimientoInventarioEntry.FIELD_PRODUCTO_REF);
                    int cantidad = movimientoDoc.getLong(FirestoreContract.MovimientoInventarioEntry.FIELD_CANTIDAD).intValue();
                    String tipo = movimientoDoc.getString(FirestoreContract.MovimientoInventarioEntry.FIELD_TIPO);

                    if (!movimientosInventario.containsKey(productoRef)) {
                        movimientosInventario.put(productoRef, new int[]{0, 0});
                    }

                    if ("Entrada".equals(tipo)) {
                        movimientosInventario.get(productoRef)[0] += cantidad;
                    } else if ("Salida".equals(tipo)) {
                        movimientosInventario.get(productoRef)[1] += cantidad;
                    }
                }
                List<DataEntry> dataEntries = new ArrayList<>();
                for (Map.Entry<String, int[]> entry : movimientosInventario.entrySet()) {
                    dataEntries.add(new CustomDataEntry(entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
                }
                callback.onCallback(dataEntries);
            }
        });
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }
}


