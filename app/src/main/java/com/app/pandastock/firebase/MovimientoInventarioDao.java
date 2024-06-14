package com.app.pandastock.firebase;
import com.app.pandastock.models.MovimientoInventario;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.Date;
import java.util.UUID;
public class MovimientoInventarioDao {

    private FirebaseFirestore db;

    public MovimientoInventarioDao() {
        db = FirebaseFirestore.getInstance();
    }



}
