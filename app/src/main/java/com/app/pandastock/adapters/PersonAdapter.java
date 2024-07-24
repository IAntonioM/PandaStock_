package com.app.pandastock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.pandastock.R;
import com.app.pandastock.models.Personal;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private List<Personal> personList;
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public PersonAdapter(List<Personal> personList, OnEditClickListener onEditClickListener, OnDeleteClickListener onDeleteClickListener) {
        this.personList = personList;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        public TextView fullName;
        public TextView username;
        public TextView role;
        public Button editButton;
        public Button deleteButton;

        public PersonViewHolder(View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.textViewFullName);  // Cambiado de R.textViewFullName a R.id.textViewFullName
            username = itemView.findViewById(R.id.textViewUsername);
            role = itemView.findViewById(R.id.textViewRole);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Personal person = personList.get(position);
        holder.fullName.setText(person.getFullName());
        holder.username.setText(person.getUsername());
        holder.role.setText(person.getRole());
        holder.editButton.setOnClickListener(v -> onEditClickListener.onEditClick(person));
        holder.deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(person));
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public void updateList(List<Personal> newList) {
        personList.clear();
        personList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnEditClickListener {
        void onEditClick(Personal person);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Personal person);
    }
}


