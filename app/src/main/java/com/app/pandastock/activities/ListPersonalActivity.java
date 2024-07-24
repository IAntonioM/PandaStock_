package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pandastock.R;
import com.app.pandastock.adapters.PersonAdapter;
import com.app.pandastock.adapters.PersonDialogFragment;
import com.app.pandastock.models.Personal;
import com.app.pandastock.utils.SessionManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListPersonalActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private PersonAdapter adapter;
    private RecyclerView recyclerView;
    private List<Personal> personList = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_personal);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new PersonAdapter(personList, this::showEditDialog, this::deletePerson);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        sessionManager=new SessionManager(this);

        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> showAddDialog());

        loadPersons();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ListPersonalActivity.this, MenuInicoActivity.class);
        startActivity(intent);
        finish();
    }

    private void showAddDialog() {
        PersonDialogFragment dialog = new PersonDialogFragment(null, this::addPerson);
        dialog.show(getSupportFragmentManager(), "Agregar Persona");
    }

    private void showEditDialog(Personal person) {
        PersonDialogFragment dialog = new PersonDialogFragment(person, this::updatePerson);
        dialog.show(getSupportFragmentManager(), "Editar");
    }

    private void addPerson(Personal person) {
        db.collection(sessionManager.getEmpresa()+"_persons").document(person.getId()).set(person)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ListPersonalActivity.this, "Agregado", Toast.LENGTH_SHORT).show();
                    loadPersons();
                })
                .addOnFailureListener(e -> Toast.makeText(ListPersonalActivity.this, "Error al agregar persona", Toast.LENGTH_SHORT).show());
    }

    private void updatePerson(Personal person) {
        db.collection(sessionManager.getEmpresa()+"_persons").document(person.getId()).set(person)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ListPersonalActivity.this, "Actualizado", Toast.LENGTH_SHORT).show();
                    loadPersons();
                })
                .addOnFailureListener(e -> Toast.makeText(ListPersonalActivity.this, "No se pudo actualizar la persona", Toast.LENGTH_SHORT).show());
    }

    private void deletePerson(Personal person) {
        db.collection(sessionManager.getEmpresa()+"_persons").document(person.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ListPersonalActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
                    loadPersons();
                })
                .addOnFailureListener(e -> Toast.makeText(ListPersonalActivity.this, "No se pudo eliminar a la persona", Toast.LENGTH_SHORT).show());
    }

    private void loadPersons() {
        db.collection(sessionManager.getEmpresa()+"_persons").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Personal> persons = queryDocumentSnapshots.toObjects(Personal.class);
                    adapter.updateList(persons);
                })
                .addOnFailureListener(e -> Toast.makeText(ListPersonalActivity.this, "No se pudieron cargar personas", Toast.LENGTH_SHORT).show());
    }
}