package com.app.pandastock.firebase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.pandastock.models.DetalleVenta;
import com.app.pandastock.models.Personal;
import com.app.pandastock.utils.SessionManager;
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

public class PersonalDao {

    private FirebaseFirestore db;
    private CollectionReference personalRef;
    private SessionManager sessionManager;

    public PersonalDao(Context context) {
        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(context);
    }

    public void addPerson(Personal person, OnPersonAddedListener listener) {
        personalRef = db.collection(person.getUsername() + "_persons");
        personalRef.document(person.getId()).set(person)
                .addOnSuccessListener(aVoid -> {
                    if (listener != null) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure(e);
                    }
                });
    }

    public interface OnPersonAddedListener {
        void onSuccess();
        void onFailure(Exception e);
    }
}