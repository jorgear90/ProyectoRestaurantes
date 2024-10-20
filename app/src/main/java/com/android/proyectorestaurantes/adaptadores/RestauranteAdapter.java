package com.android.proyectorestaurantes.adaptadores;

import android.content.Context;
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
        //this.context = context;
        this.restaurantes = restaurantes;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(restaurantes);
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

    public void filtrado(String txtBuscar){
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
        }
        notifyDataSetChanged();
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
