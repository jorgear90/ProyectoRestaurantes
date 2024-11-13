package com.android.proyectorestaurantes.entidades;

public class Opiniones {
    private Integer id;
    private String correoUsuario;
    private String idRestaurante;
    private float puntuacion;
    private String comentario;
    private String fecha;

    public Opiniones(int id, String correoUsuario, String idRestaurante, float puntuacion, String comentario, String fecha) {
        this.id = id;
        this.setCorreoUsuario(correoUsuario);
        this.setIdRestaurante(idRestaurante);
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = fecha;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(String idRestaurante) {
        this.idRestaurante = idRestaurante;
    }
}
