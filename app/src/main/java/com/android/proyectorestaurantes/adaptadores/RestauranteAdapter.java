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
        Log.e("respuesta","se infla MAL");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_restaurante, parent, false);
        Log.e("respuesta","se infla bien");
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

        public RestauranteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.viewNombre);
        }
    }
}
