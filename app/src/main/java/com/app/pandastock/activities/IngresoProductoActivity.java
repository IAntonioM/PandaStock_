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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class IngresoProductoActivity extends AppCompatActivity {
    
    Spinner proc1, marca1;
    EditText cant, mol;
    FirebaseAuth mAuth;
    ImageButton btn1, btn2, btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_producto);
        mAuth= FirebaseAuth.getInstance();
        proc1 = findViewById(R.id.spnProducto);
        cant = findViewById(R.id.edtCantidad);
        mol = findViewById(R.id.edtModelo);
        marca1 = findViewById(R.id.snpMarca);
        btn1 = findViewById(R.id.btningresarp);
        btn2 = findViewById(R.id.btndenegar);
        btn3 = findViewById(R.id.btnregresarp);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Registrar();
        Seleccion();
        Regresar();
        Denegar();
    }
    private void Registrar(){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(proc1.getSelectedItem().toString().trim().isEmpty() || cant.getText().toString().trim().isEmpty() || mol.getText().toString().trim().isEmpty()){
                    Toast.makeText(IngresoProductoActivity.this, "Complete los campos faltantes", Toast.LENGTH_LONG).show();
                }else{
                    String product = proc1.getSelectedItem().toString();
                    String marca = marca1.getSelectedItem().toString();
                    String moldelo = mol.getText().toString();
                    int cantidad = Integer.parseInt(cant.getText().toString());

                    Date day = new Date();
                    TimeZone myTimeZone = TimeZone.getTimeZone("America/Lima");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");
                    shf.setTimeZone(myTimeZone);
                    String fecha = sdf.format(day);
                    String hora = shf.format(day);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    String url = "/Registro/"+product+"/"+marca;
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        Map<String, Object> registro = new HashMap<>();
                        registro.put("Usuario", user.getUid());
                        registro.put("Stock", cantidad);
                        registro.put("Modelo", moldelo);
                        registro.put("Fecha", fecha);
                        registro.put("Hora", hora);

                        db.collection(url).document(moldelo)
                                .set(registro, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(IngresoProductoActivity.this, "Registro exitoso.",
                                                Toast.LENGTH_SHORT).show();
                                        proc1.setSelection(0);
                                        marca1.setSelection(0);
                                        mol.setText(" ");
                                        cant.setText(" ");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(IngresoProductoActivity.this, "Registro no elaborado.",
                                                Toast.LENGTH_SHORT).show();
                                        proc1.setSelection(0);
                                        marca1.setSelection(0);
                                        mol.setText(" ");
                                        cant.setText(" ");
                                    }
                                });
                    }
                }
            }
        });
    }

    private void Seleccion(){
        proc1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String product = proc1.getSelectedItem().toString();
                if(product.equals("Laptop")){
                    String [] opcionMarca = getResources().getStringArray(R.array.MarcasLaptop);
                    ArrayAdapter<String> array1 = new ArrayAdapter<>(IngresoProductoActivity.this, android.R.layout.simple_spinner_item, opcionMarca);
                    array1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marca1.setAdapter(array1);
                }else if(product.equals("Mouse")){
                    String [] opcionMarca = getResources().getStringArray(R.array.MarcasMouse);
                    ArrayAdapter<String> array1 = new ArrayAdapter<>(IngresoProductoActivity.this, android.R.layout.simple_spinner_item, opcionMarca);
                    array1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marca1.setAdapter(array1);
                }else if(product.equals("Teclado")){
                    String [] opcionMarca = getResources().getStringArray(R.array.MarcasTeclado);
                    ArrayAdapter<String> array1 = new ArrayAdapter<>(IngresoProductoActivity.this, android.R.layout.simple_spinner_item, opcionMarca);
                    array1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    marca1.setAdapter(array1);
                }else if(product.equals("Monitor")){
                    String [] opcionMarca = getResources().getStringArray(R.array.MarcasMonitor);
                    ArrayAdapter<String> array1 = new ArrayAdapter<>(IngresoProductoActivity.this, android.R.layout.simple_spinner_item, opcionMarca);
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
                startActivity(new Intent(IngresoProductoActivity.this, MenuActivity.class));

            }
        });
    }

    private void Denegar(){
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IngresoProductoActivity.this, "Datos eliminados.",
                        Toast.LENGTH_SHORT).show();
                proc1.setSelection(0);
                marca1.setSelection(0);
                mol.setText(" ");
                cant.setText(" ");
            }
        });
    }
}