package com.android.proyectorestaurantes.bd;

import com.android.proyectorestaurantes.entidades.Platillo;
import com.android.proyectorestaurantes.entidades.Restaurante;

import java.util.ArrayList;
import java.util.List;

public class BaseDatos {

    public static void main(String[] args) {

        List<Platillo> platillos1 = new ArrayList<>();
        platillos1.add(new Platillo("Papas fritas"));
        platillos1.add(new Platillo("Completo"));
        platillos1.add(new Platillo("Hamburguesa"));

        List<Platillo> platillos2 = new ArrayList<>();
        platillos2.add(new Platillo("Sushi"));
        platillos2.add(new Platillo("Ramen"));

        List<Restaurante> restaurantes = new ArrayList<>();
        restaurantes.add(new Restaurante(1,"Comida rapida", "Una calle 123", "09:00", "22:00", -33.456, -70.648, 4.5, platillos1));
        restaurantes.add(new Restaurante(2,"Restaurante Japonés", "Otra calle 123", "12:00", "23:00", -33.467, -70.650, 4.7, platillos2));

    }
}
