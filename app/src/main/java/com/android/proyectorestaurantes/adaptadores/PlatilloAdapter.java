package com.android.proyectorestaurantes.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.entidades.Platillo;

import java.util.ArrayList;
import java.util.List;

public class PlatilloAdapter extends RecyclerView.Adapter<PlatilloAdapter.PlatilloViewHolder> {
    private List<Platillo> platillosList;

    public PlatilloAdapter(List<Platillo> platillosList) {
        this.platillosList = platillosList;
    }

    @NonNull
    @Override
    public PlatilloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_platillo, parent, false);
        return new PlatilloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatilloViewHolder holder, int position) {
        Platillo platillo = platillosList.get(position);
        holder.tvPlatillo.setText(platillo.getNombre());
        holder.tvPrecio.setText(String.valueOf(platillo.getPrecio()));
    }

    @Override
    public int getItemCount() {
        return platillosList.size();
    }

    public static class PlatilloViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlatillo;
        TextView tvPrecio;

        public PlatilloViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlatillo = itemView.findViewById(R.id.tvPlatillo);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
