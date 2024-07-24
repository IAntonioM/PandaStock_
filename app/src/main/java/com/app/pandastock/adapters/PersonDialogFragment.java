package com.app.pandastock.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.app.pandastock.R;
import com.app.pandastock.models.Personal;

import java.util.UUID;

public class PersonDialogFragment extends DialogFragment {

    private EditText editTextFullName, editTextLastName, editTextUsername, editTextPassword, editTextEmail;
    private Spinner spinnerRole;
    private Button buttonSave;
    private Personal person;
    private OnSaveListener onSaveListener;

    public interface OnSaveListener {
        void onSave(Personal person);
    }

    public PersonDialogFragment(Personal person, OnSaveListener onSaveListener) {  // Cambiado Person a Personal
        this.person = person;
        this.onSaveListener = onSaveListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_person, container, false);

        editTextFullName = view.findViewById(R.id.editTextFullName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        buttonSave = view.findViewById(R.id.buttonSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        if (person != null) {
            editTextFullName.setText(person.getFullName());
            editTextLastName.setText(person.getLastName());
            editTextUsername.setText(person.getUsername());
            editTextPassword.setText(person.getPassword());
            editTextEmail.setText(person.getEmail());
            spinnerRole.setSelection(((ArrayAdapter) spinnerRole.getAdapter()).getPosition(person.getRole()));
        }

        buttonSave.setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();
            String email = editTextEmail.getText().toString();
            String role = spinnerRole.getSelectedItem().toString();

            if (person == null) {
                person = new Personal(UUID.randomUUID().toString(), fullName, lastName, username, password, email, role);
            } else {
                person = new Personal(person.getId(), fullName, lastName, username, password, email, role);
            }

            onSaveListener.onSave(person);
            dismiss();
        });

        return view;
    }
}

