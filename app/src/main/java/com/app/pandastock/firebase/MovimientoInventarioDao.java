package com.app.pandastock.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.app.pandastock.models.MovimientoInventario;
import com.app.pandastock.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.app.pandastock.firebase.FirestoreContract.MovimientoInventarioEntry;
import com.app.pandastock.firebase.FirestoreContract.UsuarioEntry;
import com.app.pandastock.firebase.FirestoreContract.ProductoEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimientoInventarioDao {
    private FirebaseFirestore db;
    private CollectionReference movimientoInventarioRef;
    private SessionManager sessionManager;

    public MovimientoInventarioDao(Context context) {
        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(context);
        movimientoInventarioRef = db.collection(sessionManager.getEmpresa()+"_"+MovimientoInventarioEntry.COLLECTION_NAME);
    }

    public void createMovimientoInv(String idUsuario, String idProducto, int cantidad, String tipo,
                                    final FirestoreCallback<Boolean> callback) {
        DocumentReference usuarioRef = db.collection(sessionManager.getEmpresa()+"_persons/").document(idUsuario);
        DocumentReference productoRef = db.collection(sessionManager.getEmpresa()+"_AProductos/").document(idProducto);

        Map<String, Object> movimientoInvData = new HashMap<>();
        movimientoInvData.put(MovimientoInventarioEntry.FIELD_USUARIO_REF, usuarioRef);
        movimientoInvData.put(MovimientoInventarioEntry.FIELD_PRODUCTO_REF, productoRef);
        movimientoInvData.put(MovimientoInventarioEntry.FIELD_CANTIDAD, cantidad);
        movimientoInvData.put(MovimientoInventarioEntry.FIELD_TIPO, tipo);
        movimientoInvData.put(MovimientoInventarioEntry.ARRAY_CODES_BAR, new ArrayList<>());
        movimientoInvData.put(MovimientoInventarioEntry.FIELD_FECHA,  new Date());

        movimientoInventarioRef.add(movimientoInvData)
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

    // Listar todos los registros ordenados por fecha de más reciente a más antiguo
    public void getAllMovimientoInv(final FirestoreCallback<List<MovimientoInventario>> callback) {
        movimientoInventarioRef.orderBy(MovimientoInventarioEntry.FIELD_FECHA, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<MovimientoInventario> movimientoList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                DocumentReference usuarioRef = document.getDocumentReference(MovimientoInventarioEntry.FIELD_USUARIO_REF); // Ajusta según el nombre del campo en Firestore
                                DocumentReference productoRef = document.getDocumentReference(MovimientoInventarioEntry.FIELD_PRODUCTO_REF); // Ajusta según el nombre del campo en Firestore
                                int cantidad = document.getLong(MovimientoInventarioEntry.FIELD_CANTIDAD).intValue(); // Ajusta según el nombre del campo en Firestore
                                String tipo = document.getString(MovimientoInventarioEntry.FIELD_TIPO); // Ajusta según el nombre del campo en Firestore
                                ArrayList<String> codigosBarra = (ArrayList<String>) document.get(MovimientoInventarioEntry.ARRAY_CODES_BAR); // Ajusta según el nombre del campo en Firestore
                                Date fecha = document.getDate(MovimientoInventarioEntry.FIELD_FECHA); // Ajusta según el nombre del campo en Firestore

                                MovimientoInventario movimientoInv = new MovimientoInventario(id, usuarioRef, productoRef, codigosBarra, cantidad, tipo, fecha);
                                movimientoList.add(movimientoInv);
                            }
                            callback.onComplete(movimientoList);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }


    // Interfaz de callback para manejar resultados asíncronos
    public interface FirestoreCallback<T> {
        void onComplete(T result);
        void onFailure(Exception e);
    }
}
