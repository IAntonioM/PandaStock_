package com.app.pandastock.firebase;

import android.content.Context;
import androidx.annotation.NonNull;
import com.app.pandastock.models.TipoProducto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TipoProductoDao {
    private FirebaseFirestore db;

    public TipoProductoDao(Context context) {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllTipoProductos(final FirestoreCallback<List<TipoProducto>> callback) {
        db.collection(FirestoreContract.TipoProductoEntry.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<TipoProducto> tipoProductos = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                TipoProducto tipoProducto = document.toObject(TipoProducto.class);
                                tipoProductos.add(tipoProducto);
                            }
                            callback.onComplete(tipoProductos);
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

