package com.app.pandastock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.pandastock.R;
import com.app.pandastock.models.VentaData;
import com.app.pandastock.models.VentaProductoData;

import java.util.List;

public class AdapterVentasPorProducto extends RecyclerView.Adapter<AdapterVentasPorProducto.ViewHolder> {
    private List<VentaProductoData> ventasPorProductoList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descripcionProductoTextView;
        public TextView gananciaTotalTextView;
        public TextView cantidadVendidaTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            descripcionProductoTextView = itemView.findViewById(R.id.textViewDescripcionProducto);
            gananciaTotalTextView = itemView.findViewById(R.id.textViewGananciaTotal);
            cantidadVendidaTextView = itemView.findViewById(R.id.textViewCantidadVendida);
        }
    }

    public AdapterVentasPorProducto(List<VentaProductoData> ventasPorProductoList) {
        this.ventasPorProductoList = ventasPorProductoList;
    }

    @Override
    public AdapterVentasPorProducto.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta_por_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VentaProductoData ventaData = ventasPorProductoList.get(position);
        holder.descripcionProductoTextView.setText(ventaData.getProducto());
        holder.gananciaTotalTextView.setText(String.valueOf(ventaData.getGanancia()));
        holder.cantidadVendidaTextView.setText(String.valueOf(ventaData.getCantidadVendida()));
    }

    @Override
    public int getItemCount() {
        return ventasPorProductoList.size();
    }
}

