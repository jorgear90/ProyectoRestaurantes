package com.android.proyectorestaurantes.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.proyectorestaurantes.R;
import com.android.proyectorestaurantes.entidades.Opiniones;
import com.android.proyectorestaurantes.entidades.Usuario;

import java.util.ArrayList;

public class OpinionesAdapter extends RecyclerView.Adapter<OpinionesAdapter.OpinionesViewHolder> {

    private ArrayList<Opiniones> opinionesList;
    private ArrayList<Usuario> usuariosList; // Para obtener el correo del usuario basado en idUsuario

    public OpinionesAdapter(ArrayList<Opiniones> opinionesList, ArrayList<Usuario> usuariosList) {
        this.opinionesList = opinionesList;
        this.usuariosList = usuariosList;
    }

    @NonNull
    @Override
    public OpinionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_opinion, parent, false);
        return new OpinionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpinionesViewHolder holder, int position) {
        Opiniones opinion = opinionesList.get(position);
        holder.tvFecha.setText(opinion.getFecha());
        holder.tvComentario.setText(opinion.getComentario());
        holder.ratingBar.setRating(opinion.getPuntuacion());

        // Buscar el correo del usuario correspondiente a idUsuario
        for (Usuario usuario : usuariosList) {
            if (usuario.getId() == opinion.getIdUsuario()) {
                holder.tvCorreoUsuario.setText(usuario.getCorreo());
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return opinionesList.size();
    }

    public static class OpinionesViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvCorreoUsuario, tvComentario;
        RatingBar ratingBar;

        public OpinionesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCorreoUsuario = itemView.findViewById(R.id.tvCorreoUsuario);
            tvComentario = itemView.findViewById(R.id.tvComentario);
            ratingBar = itemView.findViewById(R.id.ratingBarOpinion);
        }
    }

}
