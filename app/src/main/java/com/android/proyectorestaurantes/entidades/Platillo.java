package com.android.proyectorestaurantes.entidades;

import java.io.Serializable;

public class Platillo implements Serializable {
    private String nombre;
    private Integer precio;

    public Platillo(String nombre, int precio){
        this.setNombre(nombre);
        this.setPrecio(precio);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }
}
