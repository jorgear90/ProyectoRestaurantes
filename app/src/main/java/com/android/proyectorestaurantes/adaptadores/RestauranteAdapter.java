package com.android.proyectorestaurantes.adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.entidades.Restaurante;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder>{

    //Context context;
    ArrayList<Restaurante> restaurantes;
    ArrayList<Restaurante> listaOriginal;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Restaurante restaurante);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RestauranteAdapter(ArrayList<Restaurante> restaurantes) {
        this.restaurantes = restaurantes;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(restaurantes);
    }

    public ArrayList<Restaurante> getListaOriginal() {
        return listaOriginal;
    }

    // Método para setear (actualizar) la lista de restaurantes
    public void setRestaurantes(ArrayList<Restaurante> nuevosRestaurantes) {
        this.restaurantes.clear();
        this.restaurantes.addAll(nuevosRestaurantes);

        // Actualiza también la lista original
        this.listaOriginal.clear();
        this.listaOriginal.addAll(nuevosRestaurantes);

        notifyDataSetChanged();  // Notifica al adaptador que los datos han cambiado y debe refrescar la vista
    }

    //HECHO
    @NonNull
    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new RestauranteViewHolder(LayoutInflater.from(context).inflate(R.layout.lista_restaurante,parent,false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_restaurante, parent, false);
        return new RestauranteViewHolder(view);
    }

    public ArrayList<Restaurante> filtrado(String txtBuscar){
        ArrayList<Restaurante> restaurantesFiltrados = new ArrayList<>();
        int longitud = txtBuscar.length();
        if(longitud==0){
            Log.e("resultado","chaoooo");
            restaurantes.clear();
            restaurantes.addAll(listaOriginal);
        }else{
            Log.e("resultado","holaaaa");
            List<Restaurante> collecion = restaurantes.stream().filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
            restaurantes.clear();
            restaurantes.addAll(collecion);
            restaurantesFiltrados.addAll(collecion);
        }
        notifyDataSetChanged();
        return restaurantesFiltrados;
    }

    @Override
    public void onBindViewHolder(@NonNull RestauranteViewHolder holder, int position) {
        holder.nombre.setText(restaurantes.get(position).getNombre());
        holder.direccion.setText(restaurantes.get(position).getDireccion());
        holder.ciudad.setText(restaurantes.get(position).getCiudad());
        Restaurante restaurante = restaurantes.get(position);
        holder.bind(restaurante, listener);
    }

    @Override
    public int getItemCount() {
        return restaurantes.size();
    }

    public void filterList(ArrayList<Restaurante> filteredList) {
        restaurantes = filteredList;
        notifyDataSetChanged();
    }

    public class RestauranteViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView direccion;
        TextView ciudad;

        public RestauranteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.viewNombre);
            direccion = itemView.findViewById(R.id.viewDireccion);
            ciudad = itemView.findViewById(R.id.viewCiudad);
        }
        public void bind(final Restaurante restaurante, final OnItemClickListener listener) {
            // Configura las vistas y agrega el listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(restaurante);
                }
            });
        }
    }
}
