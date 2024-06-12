package com.app.pandastock.firebase;

import androidx.annotation.NonNull;

import com.app.pandastock.models.DetalleVenta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetalleVentaDao {

    private FirebaseFirestore db;
    private CollectionReference detallesVentaRef;

    public DetalleVentaDao() {
        db = FirebaseFirestore.getInstance();
        detallesVentaRef = db.collection(FirestoreContract.DetalleVentaEntry.COLLECTION_NAME);
    }

    public void insertDetalleVenta(DetalleVenta detalleVenta, final FirestoreCallback<Boolean> callback) {
        Map<String, Object> detalleVentaData = new HashMap<>();
        detalleVentaData.put(FirestoreContract.DetalleVentaEntry.FIELD_VENTA_REF, detalleVenta.getVenta());
        detalleVentaData.put(FirestoreContract.DetalleVentaEntry.FIELD_PRODUCTO_REF, detalleVenta.getProducto());
        detalleVentaData.put(FirestoreContract.DetalleVentaEntry.FIELD_CANTIDAD, detalleVenta.getCantidad());
        detalleVentaData.put(FirestoreContract.DetalleVentaEntry.FIELD_PRECIO, detalleVenta.getPrecioUnitario());
        detalleVentaData.put(FirestoreContract.DetalleVentaEntry.FIELD_SUBTOTAL, detalleVenta.getSubtotal());

        detallesVentaRef.add(detalleVentaData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onComplete(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(false);
                    }
                });
    }
    public void getDetallesPorVenta(String idVenta, final FirestoreCallback<List<DetalleVenta>> callback) {
        detallesVentaRef.whereEqualTo(FirestoreContract.DetalleVentaEntry.FIELD_VENTA_REF, db.document(FirestoreContract.VentaEntry.COLLECTION_NAME + "/" + idVenta))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DetalleVenta> detallesVenta = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        DetalleVenta detalleVenta = new DetalleVenta();
                        detalleVenta.setId(document.getId());
                        detalleVenta.setVenta(document.getDocumentReference(FirestoreContract.DetalleVentaEntry.FIELD_VENTA_REF));
                        detalleVenta.setProducto(document.getDocumentReference(FirestoreContract.DetalleVentaEntry.FIELD_PRODUCTO_REF));
                        detalleVenta.setCantidad(document.getLong(FirestoreContract.DetalleVentaEntry.FIELD_CANTIDAD).intValue());
                        detalleVenta.setPrecioUnitario(document.getDouble(FirestoreContract.DetalleVentaEntry.FIELD_PRECIO));
                        detalleVenta.setSubtotal(document.getDouble(FirestoreContract.DetalleVentaEntry.FIELD_SUBTOTAL));
                        detallesVenta.add(detalleVenta);
                    }
                    callback.onComplete(detallesVenta);
                })
                .addOnFailureListener(e -> callback.onComplete(null));
    }




    public void getAllDetallesVenta(final FirestoreCallback<List<DetalleVenta>> callback) {
        detallesVentaRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DetalleVenta> detallesVenta = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        DetalleVenta detalleVenta = new DetalleVenta();
                        detalleVenta.setId(documentSnapshot.getId());
                        detalleVenta.setVenta(documentSnapshot.getDocumentReference(FirestoreContract.DetalleVentaEntry.FIELD_VENTA_REF));
                        detalleVenta.setProducto(documentSnapshot.getDocumentReference(FirestoreContract.DetalleVentaEntry.FIELD_PRODUCTO_REF));
                        detalleVenta.setCantidad(documentSnapshot.getLong(FirestoreContract.DetalleVentaEntry.FIELD_CANTIDAD).intValue());
                        detalleVenta.setPrecioUnitario(documentSnapshot.getDouble(FirestoreContract.DetalleVentaEntry.FIELD_PRECIO));
                        detalleVenta.setSubtotal(documentSnapshot.getDouble(FirestoreContract.DetalleVentaEntry.FIELD_SUBTOTAL));
                        detallesVenta.add(detalleVenta);
                    }
                    callback.onComplete(detallesVenta);
                })
                .addOnFailureListener(e -> {
                    // Manejo de errores
                    callback.onComplete(null);
                });
    }

    public interface FirestoreCallback<T> {
        void onComplete(T result);
    }
}
