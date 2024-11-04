package com.android.proyectorestaurantes.entidades;

import java.io.Serializable;
import java.util.List;

public class Favoritos implements Serializable {
    private String correoUsuario;
    private Integer idRestaurante;
    private String nombreRetaurante;
    private String direccion;
    private String horaApertura;
    private String horaCierre;
    private double promedio;
    private List<Platillo> platillos;
    private List<Servicios> servicios;

    public Favoritos (String correoUsuario, int idRestaurante, String nombreRetaurante, String direccion, String horaApertura, String horaCierre, double promedio,List<Platillo> platillos,List<Servicios> servicios){
        this.setCorreoUsuario(correoUsuario);
        this.setIdRestaurante(idRestaurante);
        this.setNombreRetaurante(nombreRetaurante);
        this.setDireccion(direccion);
        this.setHoraApertura(horaApertura);
        this.setHoraCierre(horaCierre);
        this.setPromedio(promedio);
        this.setPlatillos(platillos);
        this.setServicios(servicios);

    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public Integer getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(Integer idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public String getNombreRetaurante() {
        return nombreRetaurante;
    }

    public void setNombreRetaurante(String nombreRetaurante) {
        this.nombreRetaurante = nombreRetaurante;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(String horaApertura) {
        this.horaApertura = horaApertura;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public List<Platillo> getPlatillos() {
        return platillos;
    }

    public void setPlatillos(List<Platillo> platillos) {
        this.platillos = platillos;
    }

    public List<Servicios> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicios> servicios) {
        this.servicios = servicios;
    }
}
