package com.app.pandastock.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.app.pandastock.models.Marca;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MarcaDao {
    private FirebaseFirestore db;

    public MarcaDao(Context context) {
        db = FirebaseFirestore.getInstance();
    }

    public void getMarcasByTipoProducto(String tipoProducto, final FirestoreCallback<List<Marca>> callback) {
        // Get the TipoProducto ID based on its name
        db.collection(FirestoreContract.TipoProductoEntry.COLLECTION_NAME)
                .whereEqualTo(FirestoreContract.TipoProductoEntry.FIELD_NOMBRE, tipoProducto)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            String tipoProductoId = task.getResult().getDocuments().get(0).getId();
                            // Get the Marca documents where IdTipoProducto matches the obtained TipoProducto ID
                            db.collection(FirestoreContract.MarcaEntry.COLLECTION_NAME)
                                    .whereEqualTo(FirestoreContract.MarcaEntry.FIELD_TIPO_PRODUCTO_ID, tipoProductoId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                List<Marca> marcasList = new ArrayList<>();
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    Marca marca = document.toObject(Marca.class);
                                                    marcasList.add(marca);
                                                }
                                                callback.onComplete(marcasList);
                                            } else {
                                                callback.onComplete(null);
                                            }
                                        }
                                    });
                        } else {
                            callback.onComplete(null);
                        }
                    }
                });
    }

    // Callback interface para manejar resultados asíncronos
    public interface FirestoreCallback<T> {
        void onComplete(T result);
    }
}

