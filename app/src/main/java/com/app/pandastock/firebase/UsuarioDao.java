package com.app.pandastock.firebase;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import com.app.pandastock.models.Usuario;
import com.app.pandastock.utils.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.app.pandastock.firebase.FirestoreContract.UsuarioEntry;

import java.util.HashMap;
import java.util.Map;

public class UsuarioDao {
    private SessionManager session;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public UsuarioDao(Context context) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth
        session=new SessionManager(context);
    }

    public void register(final String empresa, final String email, final String password, final FirestoreCallback<Boolean> callback) {
        // Consultar si ya existe un usuario con el mismo nombre y email
        db.collection(UsuarioEntry.COLLECTION_NAME)
                .whereEqualTo(UsuarioEntry.FIELD_NOMBRE, empresa)
                .whereEqualTo(UsuarioEntry.FIELD_EMAIL, email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                // Usuario ya registrado
                                callback.onComplete(false); // Devuelve false si el usuario ya está registrado
                            } else {
                                // No existe usuario registrado con el mismo nombre y email, proceder con el registro
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    if (user != null) {
                                                        // Crear un objeto Map para almacenar los datos del usuario
                                                        Map<String, Object> userData = new HashMap<>();
                                                        userData.put(UsuarioEntry.DOC_ID, user.getUid());
                                                        userData.put(UsuarioEntry.FIELD_NOMBRE, empresa);
                                                        userData.put(UsuarioEntry.FIELD_EMAIL, email);

                                                        // Guardar los datos del usuario en Firestore
                                                        db.collection(UsuarioEntry.COLLECTION_NAME).document(user.getUid())
                                                                .set(userData)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        callback.onComplete(true); // Devuelve true si el registro fue exitoso
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        callback.onComplete(false); // Devuelve false si hubo un error al guardar los datos
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    callback.onComplete(false); // Devuelve false si el registro de usuario falló
                                                }
                                            }
                                        });
                            }
                        } else {
                            callback.onComplete(false); // Devuelve false si hubo un error al realizar la consulta
                        }
                    }
                });
    }


    // Verificar si el usuario existe en Firestore y obtener sus datos
    public void login(String email, String password, final FirestoreCallback<Boolean> callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Obtener los datos adicionales del usuario desde Firestore
                                db.collection(UsuarioEntry.COLLECTION_NAME).document(user.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        // Obtener los datos del documento
                                                        String empresa = document.getString(UsuarioEntry.FIELD_NOMBRE);
                                                        // Guardar los datos en SharedPreferences
                                                        session.createLoginSession(empresa,email);
                                                        callback.onComplete(true);
                                                    } else {
                                                        callback.onComplete(false);
                                                    }
                                                } else {
                                                    callback.onComplete(false);
                                                }
                                            }
                                        });
                            } else {
                                callback.onComplete(false);
                            }
                        } else {
                            callback.onComplete(false);
                        }
                    }
                });
    }

    // Obtener usuario por email en Firestore
    public void getUserByEmail(String email, final FirestoreCallback<Usuario> callback) {
        db.collection(UsuarioEntry.COLLECTION_NAME)
                .whereEqualTo(UsuarioEntry.FIELD_EMAIL, email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            Usuario user = document.toObject(Usuario.class);
                            callback.onComplete(user);
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




