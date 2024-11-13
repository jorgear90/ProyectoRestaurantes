package com.android.proyectorestaurantes.bd;

import com.android.proyectorestaurantes.entidades.Favoritos;
import com.android.proyectorestaurantes.entidades.Opiniones;
import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;
import com.android.proyectorestaurantes.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;

public class BaseDatos {
    public static ArrayList<Opiniones> opinionesList = obtenerOpinionesIniciales();
    public static ArrayList<Usuario> usuariosList = obtenerUsuariosIniciales();

    public static void main(String[] args) {

        /*List<Platillo> platillos1 = new ArrayList<>();
        platillos1.add(new Platillo("Papas fritas"));
        platillos1.add(new Platillo("Completo"));
        platillos1.add(new Platillo("Hamburguesa"));

        List<Platillo> platillos2 = new ArrayList<>();
        platillos2.add(new Platillo("Sushi"));
        platillos2.add(new Platillo("Ramen"));

        List<Restaurante> restaurantes = new ArrayList<>();
        restaurantes.add(new Restaurante(1,"Comida rapida", "Una calle 123", "09:00", "22:00", -33.456, -70.648, 4.5, platillos1));
        restaurantes.add(new Restaurante(2,"Restaurante Japonés", "Otra calle 123", "12:00", "23:00", -33.467, -70.650, 4.7, platillos2));*/



    }

    // Lista estática para que sea accesible desde otras clases
    //public static ArrayList<Favoritos> favoritos = new ArrayList<>();

    // Método para inicializar opiniones (simulado)
    private static ArrayList<Opiniones> obtenerOpinionesIniciales() {
        ArrayList<Opiniones> opiniones = new ArrayList<>();
        opiniones.add(new Opiniones(1, "usuario1@example.com", "1", 4.5f, "Excelente comida", "2023-10-01"));
        opiniones.add(new Opiniones(2, "usuario2@example.com", "2", 3.5f, "Buen servicio", "2023-10-02"));
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
