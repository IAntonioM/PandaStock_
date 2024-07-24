package com.app.pandastock.adapters;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.app.pandastock.R;
import com.app.pandastock.models.Personal;

public class PasswordDialogFragment extends DialogFragment {

    private Personal user;
    private OnPasswordSubmitListener onPasswordSubmitListener;

    public interface OnPasswordSubmitListener {
        void onPasswordSubmit(Personal user, String password);
    }

    public PasswordDialogFragment(Personal user, OnPasswordSubmitListener onPasswordSubmitListener) {
        this.user = user;
        this.onPasswordSubmitListener = onPasswordSubmitListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_password, container, false);

        EditText etPassword = view.findViewById(R.id.etPassword);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            String password = etPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
            } else {
                dismiss(); // Cierra el diálogo
                onPasswordSubmitListener.onPasswordSubmit(user, password);
            }
        });

        return view;
    }
}
