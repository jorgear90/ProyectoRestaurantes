package com.android.proyectorestaurantes.entidades;

public class Usuario {
    private Integer id;
    private String nombre;
    private String correo;
    private String contraseña;

    public Usuario(int id, String nombre, String correo, String contraseña){
        this.setId(id);
        this.setNombre(nombre);
        this.setCorreo(correo);
        this.setContraseña(contraseña);

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
