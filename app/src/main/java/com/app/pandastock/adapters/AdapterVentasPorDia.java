package com.app.pandastock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.pandastock.R;
import com.app.pandastock.models.VentaData;

import java.util.List;

public class AdapterVentasPorDia extends RecyclerView.Adapter<AdapterVentasPorDia.ViewHolder> {
    private List<VentaData> ventasPorDiaList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fechaTextView;
        public TextView gananciaTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            fechaTextView = itemView.findViewById(R.id.textViewFecha);
            gananciaTextView = itemView.findViewById(R.id.textViewGanancia);
        }
    }

    public AdapterVentasPorDia(List<VentaData> ventasPorDiaList) {
        this.ventasPorDiaList = ventasPorDiaList;
    }

    @Override
    public AdapterVentasPorDia.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta_por_dia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VentaData ventaPorDia = ventasPorDiaList.get(position);
        holder.fechaTextView.setText(ventaPorDia.getFecha());
        holder.gananciaTextView.setText(String.valueOf(ventaPorDia.getMontoTotal()));
    }

    @Override
    public int getItemCount() {
        return ventasPorDiaList.size();
    }
}

