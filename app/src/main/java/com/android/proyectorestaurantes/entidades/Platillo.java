package com.android.proyectorestaurantes.entidades;

import java.io.Serializable;

public class Platillo implements Serializable {
    private String nombre;

    public Platillo(String nombre){
        this.setNombre(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
