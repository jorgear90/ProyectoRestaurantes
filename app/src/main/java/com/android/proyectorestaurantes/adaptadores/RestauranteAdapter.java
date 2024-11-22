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
        this.listaOriginal = new ArrayList<>(restaurantes); // Inicializa la lista original
        //listaOriginal = new ArrayList<>();
        //listaOriginal.addAll(restaurantes);

    }

    public ArrayList<Restaurante> getListaOriginal() {
        return listaOriginal;
    }

    // Método para setear (actualizar) la lista de restaurantes
    public void setRestaurantes(ArrayList<Restaurante> nuevosRestaurantes) {
        // Si nuevosRestaurantes está vacío, lo llenamos con los valores iniciales
        if (nuevosRestaurantes == null || nuevosRestaurantes.isEmpty()) {
            nuevosRestaurantes = new ArrayList<>(listaOriginal);
            Log.e("la lista", "entro en el IF");
        }
        else{
            // Asigna directamente la lista nueva
            this.restaurantes = nuevosRestaurantes;
            Log.e("la lista", "entro en el ELSE");
        }


        // Actualiza también la lista original (siempre que corresponda)
        this.listaOriginal.clear();
        this.listaOriginal = new ArrayList<>(nuevosRestaurantes); // Actualiza la lista original

        Log.e("la lista", String.valueOf(nuevosRestaurantes.size()));
        Log.e("original", String.valueOf(listaOriginal.size()));
        Log.e("restaurantes", String.valueOf(restaurantes.size()));

        // Notifica al adaptador que los datos han cambiado
        notifyDataSetChanged();
    }

    public void restaurarListaOriginal() {
        restaurantes.clear();
        restaurantes.addAll(listaOriginal);
        notifyDataSetChanged();
    }

    //HECHO
    @NonNull
    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new RestauranteViewHolder(LayoutInflater.from(context).inflate(R.layout.lista_restaurante,parent,false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_restaurante, parent, false);
        return new RestauranteViewHolder(view);
    }

    public ArrayList<Restaurante> filtrado(String texto){
        if (texto == null || texto.isEmpty()) {
            restaurarListaOriginal();
            return listaOriginal;
        }

        List<Restaurante> filtrados = new ArrayList<>();
        for (Restaurante restaurante : listaOriginal) {
            if (restaurante.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filtrados.add(restaurante);
            }
        }

        restaurantes.clear();
        restaurantes.addAll(filtrados);
        notifyDataSetChanged();
        return (ArrayList<Restaurante>) filtrados;
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
