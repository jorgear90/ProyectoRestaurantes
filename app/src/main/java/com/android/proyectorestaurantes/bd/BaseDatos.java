package com.android.proyectorestaurantes.bd;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.proyectorestaurantes.entidades.Opiniones;
import com.android.proyectorestaurantes.entidades.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BaseDatos {
    public static ArrayList<Opiniones> opinionesList = obtenerOpinionesIniciales();
    public static ArrayList<Usuario> usuariosList = obtenerUsuariosIniciales();

    public static void main(String[] args) {

    }

    public interface OpinionesCallback {
        void onOpinionesCargadas(ArrayList<Opiniones> opiniones);
    }

    // Método para inicializar opiniones (simulado)
    private static ArrayList<Opiniones> obtenerOpinionesIniciales() {
        ArrayList<Opiniones> opiniones = new ArrayList<>();

        // Referencia a la base de datos de Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Opiniones");

        // Agregamos un listener para obtener los datos
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot opinionSnapshot : dataSnapshot.getChildren()) {
                    // Obtenemos los datos de cada hijo
                    String id = opinionSnapshot.getKey(); // Id del child actual
                    String correoUsuario = opinionSnapshot.child("email").getValue(String.class);
                    String idRestaurante = opinionSnapshot.child("restauranteId").getValue(String.class);
                    Float puntuacion = opinionSnapshot.child("puntuacion").getValue(Float.class);
                    String comentario = opinionSnapshot.child("comentario").getValue(String.class);
                    String fecha = opinionSnapshot.child("fecha").getValue(String.class);

                    Log.e("rsepuesta BD", id);

                    // Creamos una nueva instancia de Opiniones y la agregamos al ArrayList
                    if (id != null && correoUsuario != null && idRestaurante != null
                            && puntuacion != null && comentario != null && fecha != null) {
                        opiniones.add(new Opiniones(id, correoUsuario, idRestaurante, puntuacion, comentario, fecha));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al obtener datos: " + databaseError.getMessage());
            }
        });

        return opiniones;
    }

    // Método para inicializar usuarios (simulado)
    private static ArrayList<Usuario> obtenerUsuariosIniciales() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("pepito", "usuario1@example.com", "1234"));
        usuarios.add(new Usuario("juanito", "usuario2@example.com", "1235"));
        return usuarios;
    }
}
