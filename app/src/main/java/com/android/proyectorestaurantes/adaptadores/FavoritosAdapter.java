package com.android.proyectorestaurantes.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.entidades.Favoritos;

import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritosViewHolder> {

    private List<Favoritos> listaFavoritos;
    private OnItemClickListener listener;

    // Constructor que incluye el listener
    public FavoritosAdapter(List<Favoritos> listaFavoritos, OnItemClickListener listener) {
        this.listaFavoritos = listaFavoritos;
        this.listener = listener;
    }

    // Constructor sin el listener
    public FavoritosAdapter(List<Favoritos> listaFavoritos) {
        this.listaFavoritos = listaFavoritos;
    }

    @NonNull
    @Override
    public FavoritosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_restaurante, parent, false);
        return new FavoritosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritosViewHolder holder, int position) {
        Favoritos favorito = listaFavoritos.get(position);
        holder.viewNombre.setText(favorito.getNombreRetaurante());

        // Configura el clic en el elemento solo si el listener está presente
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(favorito);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaFavoritos.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Favoritos favorito);
    }

    public static class FavoritosViewHolder extends RecyclerView.ViewHolder {
        TextView viewNombre;

        public FavoritosViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNombre = itemView.findViewById(R.id.viewNombre);
        }
    }
}