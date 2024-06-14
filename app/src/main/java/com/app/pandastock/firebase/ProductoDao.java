package com.app.pandastock.firebase;

import android.content.Context;
import androidx.annotation.NonNull;

import com.app.pandastock.models.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.app.pandastock.firebase.FirestoreContract.ProductoEntry;
import com.app.pandastock.firebase.FirestoreContract.MarcaEntry;
import com.app.pandastock.firebase.FirestoreContract.TipoProductoEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoDao {
    private FirebaseFirestore db;
    private CollectionReference productosRef;

    public ProductoDao(Context context) {
        db = FirebaseFirestore.getInstance();
        productosRef = db.collection(FirestoreContract.ProductoEntry.COLLECTION_NAME);
    }

    // Método para insertar un nuevo producto
    public void createProduct(String categoriaId, String marcaId, String modelo, double precio,
                              int stock, final FirestoreCallback<Boolean, String> callback) {
        DocumentReference tipoProductoRef = db.collection(TipoProductoEntry.COLLECTION_NAME).document(categoriaId);
        DocumentReference marcaRef = db.collection(MarcaEntry.COLLECTION_NAME).document(marcaId);
        Map<String, Object> productData = new HashMap<>();
        productData.put(ProductoEntry.FIELD_TIPO_PRODUCTO_REF, tipoProductoRef);
        productData.put(ProductoEntry.FIELD_MARCA_REF, marcaRef);
        productData.put(ProductoEntry.FIELD_MODELO, modelo);
        productData.put(ProductoEntry.FIELD_PRECIO, precio);
        productData.put(ProductoEntry.FIELD_STOCK, stock);
        productData.put(ProductoEntry.ARRAY_CODES_BAR, null);

        // Agregar la fecha de creación y actualización
        long currentTimeMillis = System.currentTimeMillis();
        productData.put(ProductoEntry.FIELD_FECHA_CREACION, currentTimeMillis);
        productData.put(ProductoEntry.FIELD_FECHA_ACTUALIZACION, currentTimeMillis);

        productosRef.add(productData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onComplete(true,documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(false,null);
                    }
                });
    }

    // Método para obtener la lista de productos
    public void getAllProducts(final FirestoreCallback<List<Producto>, Void> callback) {
        productosRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<Producto> productList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String modelo = document.getString(ProductoEntry.FIELD_MODELO);
                                double precio = document.getDouble(ProductoEntry.FIELD_PRECIO);
                                //ArrayList<String> codigosBarra = document.getString(ProductoEntry.ARRAY_CODE_BARS);
                                int stock = document.getLong(ProductoEntry.FIELD_STOCK).intValue();
                                long fechaCreacion = document.getLong(ProductoEntry.FIELD_FECHA_CREACION);
                                long fechaActualizacion = document.getLong(ProductoEntry.FIELD_FECHA_ACTUALIZACION);

                                DocumentReference tipoProductoRef = document.getDocumentReference(ProductoEntry.FIELD_TIPO_PRODUCTO_REF);
                                DocumentReference marcaRef = document.getDocumentReference(ProductoEntry.FIELD_MARCA_REF);
                                Producto producto = new Producto(id, tipoProductoRef, marcaRef, modelo, precio,null, stock, fechaCreacion, fechaActualizacion);
                                productList.add(producto);
                            }
                            callback.onComplete(productList,null);
                        } else {
                            callback.onComplete(null,null);
                        }
                    }
                });
    }

    // Método para actualizar un producto
    public void updateProduct(int id, int categoriaId, int marcaId, String modelo, double precio, int stock, final FirestoreCallback<Boolean, Void> callback) {
        Map<String, Object> productData = new HashMap<>();
        //productData.put(FirestoreContract.ProductoEntry.FIELD_TIPO_PRODUCTO_ID, categoriaId);
        //productData.put(FirestoreContract.ProductoEntry.FIELD_MARCA_ID, marcaId);
        productData.put(FirestoreContract.ProductoEntry.FIELD_MODELO, modelo);
        productData.put(FirestoreContract.ProductoEntry.FIELD_PRECIO, precio);
        productData.put(FirestoreContract
                .ProductoEntry.FIELD_STOCK, stock);

        // Actualizar la fecha de actualización
        long currentTimeMillis = System.currentTimeMillis();
        productData.put(FirestoreContract.ProductoEntry.FIELD_FECHA_ACTUALIZACION, currentTimeMillis);

        productosRef.document(String.valueOf(id))
                .update(productData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onComplete(true,null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(false,null);
                    }
                });
    }


    public interface FirestoreCallback<T, U> {
        void onComplete(T result, U id);
    }
}

