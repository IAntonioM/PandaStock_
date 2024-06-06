package com.app.pandastock.activities;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.pandastock.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ConsultarStockActivity extends AppCompatActivity {
    TableLayout tableProductos;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_stock);

        tableProductos = findViewById(R.id.tableProductos);
        db = FirebaseFirestore.getInstance();
        cargarProductos();
    }

    private void cargarProductos() {
        db.collectionGroup("Productos") // Ajusta el nombre según tu colección
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String producto = document.getString("Producto");
                                String marca = document.getString("Marca");
                                String modelo = document.getString("Modelo");
                                Long stock = document.getLong("Stock");

                                agregarFilaATabla(producto, marca, modelo, stock);
                            }
                        } else {
                            Toast.makeText(ConsultarStockActivity.this, "Error al obtener los datos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void agregarFilaATabla(String producto, String marca, String modelo, Long stock) {
        TableRow row = new TableRow(this);

        TextView tvProducto = new TextView(this);
        tvProducto.setText(producto);
        row.addView(tvProducto);

        TextView tvMarca = new TextView(this);
        tvMarca.setText(marca);
        row.addView(tvMarca);

        TextView tvModelo = new TextView(this);
        tvModelo.setText(modelo);
        row.addView(tvModelo);

        TextView tvStock = new TextView(this);
        tvStock.setText(String.valueOf(stock));
        row.addView(tvStock);

        tableProductos.addView(row);
    }
}
