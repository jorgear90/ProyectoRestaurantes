package com.android.proyectorestaurantes.entidades;

public class Opiniones {
    private Integer id;
    private Integer idUsuario;
    private Integer idRestaurante;
    private float puntuacion;
    private String comentario;
    private String fecha;

    public Opiniones(int id, int idUsuario, int idRestaurante, float puntuacion, String comentario, String fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRestaurante = idRestaurante;
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

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(Integer idRestaurante) {
        this.idRestaurante = idRestaurante;
    }
}
