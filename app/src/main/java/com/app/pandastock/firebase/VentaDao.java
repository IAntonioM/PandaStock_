package com.app.pandastock.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.app.pandastock.models.Venta;
import com.app.pandastock.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app.pandastock.firebase.FirestoreContract.VentaEntry;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VentaDao {
    private FirebaseFirestore db;
    private CollectionReference ventasRef;
    private SessionManager sessionManager;

    public VentaDao(Context context) {
        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(context);
        ventasRef = db.collection(sessionManager.getEmpresa()+"_"+FirestoreContract.VentaEntry.COLLECTION_NAME);
    }

    public void insertVenta(Venta venta, final FirestoreCallback<Boolean, String> callback) {
        Map<String, Object> ventaData = new HashMap<>();
        ventaData.put(VentaEntry.FIELD_NOMBRE_CLIENTE, venta.getNombreCliente());
        ventaData.put(VentaEntry.FIELD_APELLIDO_CLIENTE, venta.getApellidoCliente());
        ventaData.put(VentaEntry.FIELD_CELULAR, venta.getCelular());
        ventaData.put(VentaEntry.FIELD_DNI, venta.getDni());
        ventaData.put(VentaEntry.FIELD_EMPLEADO_REF, venta.getEmpleado());
        ventaData.put(VentaEntry.FIELD_MONTO_TOTAL, venta.getMontoTotal());
        ventaData.put(VentaEntry.FIELD_FECHA_CREACION, new Date()); // Agregar la fecha actual

        ventasRef.add(ventaData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onComplete(true, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(false, null);
                    }
                });
    }

    public void getAllVentas(final FirestoreCallback<List<Venta>, Void> callback) {
        ventasRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Venta> ventasList = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Venta venta = new Venta();
                                venta.setId(documentSnapshot.getId());
                                venta.setNombreCliente(documentSnapshot.getString(VentaEntry.FIELD_NOMBRE_CLIENTE));
                                venta.setApellidoCliente(documentSnapshot.getString(VentaEntry.FIELD_APELLIDO_CLIENTE));
                                venta.setCelular(documentSnapshot.getString(VentaEntry.FIELD_CELULAR));
                                venta.setDni(documentSnapshot.getString(VentaEntry.FIELD_DNI));
                                venta.setFechaCreacion(documentSnapshot.getDate(VentaEntry.FIELD_FECHA_CREACION));
                                venta.setEmpleado(documentSnapshot.getDocumentReference(VentaEntry.FIELD_EMPLEADO_REF));
                                venta.setMontoTotal(documentSnapshot.getDouble(VentaEntry.FIELD_MONTO_TOTAL));
                                ventasList.add(venta);
                            }
                            callback.onComplete(ventasList, null);
                        } else {
                            callback.onComplete(new ArrayList<>(), null);
                        }
                    }
                });
    }

    public interface FirestoreCallback<T, U> {
        void onComplete(T result, U id);
    }
}


