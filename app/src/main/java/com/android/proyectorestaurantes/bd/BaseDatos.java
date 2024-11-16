package com.android.proyectorestaurantes.bd;

import com.android.proyectorestaurantes.entidades.Favoritos;
import com.android.proyectorestaurantes.entidades.Opiniones;
import com.android.proyectorestaurantes.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class BaseDatos {
    public static ArrayList<Opiniones> opinionesList = obtenerOpinionesIniciales();
    public static ArrayList<Usuario> usuariosList = obtenerUsuariosIniciales();

    public static void main(String[] args) {

    }

    // Método para inicializar opiniones (simulado)
    private static ArrayList<Opiniones> obtenerOpinionesIniciales() {
        ArrayList<Opiniones> opiniones = new ArrayList<>();
        opiniones.add(new Opiniones(1, "usuario1@example.com", "1", 4.5f, "Excelente comida", "2023-10-01"));
        opiniones.add(new Opiniones(2, "usuario2@example.com", "1", 3.5f, "Buen servicio", "2023-10-02"));
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
