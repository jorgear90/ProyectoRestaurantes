package com.android.proyectorestaurantes.entidades;

import java.io.Serializable;

public class Servicios implements Serializable {
    private String nombre;

    public Servicios (String nombre){
        this.setNombre(nombre);
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

}
