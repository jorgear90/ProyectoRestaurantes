package com.android.proyectorestaurantes.entidades;

public class Platillo {
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
