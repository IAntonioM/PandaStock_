package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SalidaProductoActivity extends AppCompatActivity {

    Spinner proc1, marca1;
    EditText cant, model;
    FirebaseAuth mAuth;
    ImageButton btn1, btn2, btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_producto);
        mAuth= FirebaseAuth.getInstance();
        proc1 = findViewById(R.id.snpProductoSP);
        marca1 = findViewById(R.id.snpMarcaSP);
        cant = findViewById(R.id.edtCantidadSP);
        model = findViewById(R.id.edtmodel);
        btn1 = findViewById(R.id.btnASP);
        btn2 = findViewById(R.id.btnDSP);
        btn3 = findViewById(R.id.btnRSP);
        actualizarStock();
        Seleccion();
        Regresar();
        Denegar();
    }

    private void Seleccion(){
        proc1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String product = proc1.getSelectedItem().toString();

                if(product.equals("Laptop")) {
                    String[] opcionMarca = getResources().getStringArray(R.array.MarcasLaptop);
                    ArrayAdapter<String> array1 = new ArrayAdapter<>(SalidaProductoActivity.this, android.R.layout.simple_spinner_item, opcionMarca);
                    array1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marca1.setAdapter(array1);
                }else if(product.equals("Mouse")){
                    String [] opcionMarca = getResources().getStringArray(R.array.MarcasMouse);
                    ArrayAdapter<String> array1 = new ArrayAdapter<>(SalidaProductoActivity.this, android.R.layout.simple_spinner_item, opcionMarca);
                    array1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marca1.setAdapter(array1);
                }else if(product.equals("Teclado")){
                    String [] opcionMarca = getResources().getStringArray(R.array.MarcasTeclado);
                    ArrayAdapter<String> array1 = new ArrayAdapter<>(SalidaProductoActivity.this, android.R.layout.simple_spinner_item, opcionMarca);
                    array1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marca1.setAdapter(array1);
                }else if(product.equals("Monitor")){
                    String [] opcionMarca = getResources().getStringArray(R.array.MarcasMonitor);
                    ArrayAdapter<String> array1 = new ArrayAdapter<>(SalidaProductoActivity.this, android.R.layout.simple_spinner_item, opcionMarca);
                    array1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marca1.setAdapter(array1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void Regresar(){
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SalidaProductoActivity.this, MenuActivity.class));

            }
        });
    }
    private void Denegar(){
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SalidaProductoActivity.this, "Datos eliminados.",
                        Toast.LENGTH_SHORT).show();
                proc1.setSelection(0);
                marca1.setSelection(0);
                model.setText(" ");
                cant.setText(" ");
            }
        });
    }
    private void actualizarStock() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String producto = proc1.getSelectedItem().toString();
                String marca = marca1.getSelectedItem().toString();
                String documento = model.getText().toString();
                int cantidad = Integer.parseInt(cant.getText().toString());

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String path = String.format("Registro/%s/%s/%s", producto, marca, documento);
                db.document(path).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Long stockActual = document.getLong("Stock");
                                if (stockActual != null) {
                                    long nuevoStock = stockActual - cantidad;
                                    actualizarDocumento(path, nuevoStock);
                                } else {
                                    Toast.makeText(SalidaProductoActivity.this, "El documento no tiene un campo 'Stock'.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(SalidaProductoActivity.this, "No se encontró el documento.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(SalidaProductoActivity.this, "Error al obtener el documento: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void actualizarDocumento(String path, long nuevoStock) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(path).update("Stock", nuevoStock)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SalidaProductoActivity.this, "Stock actualizado con éxito.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SalidaProductoActivity.this, "Error al actualizar el stock: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}