package com.android.proyectorestaurantes.entidades;

import java.io.Serializable;
import java.util.List;

public class Restaurante implements Serializable {
    private Integer id;
    private String nombre;
    private String direccion;
    private String horaApertura;
    private String horaCierre;
    private String ciudad;
    private double latitud;
    private double longitud;
    private double promedio;
    private List<Platillo> platillos;
    private List<Servicios> servicios;

    public Restaurante(Integer id, String nombre, String direccion, String horaApertura, String horaCierre, double latitud, double longitud, double promedio, List<Platillo> platillos, List<Servicios> servicios, String ciudad) {
        this.setId(id);
        this.setNombre(nombre);
        this.setDireccion(direccion);
        this.setHoraApertura(horaApertura);
        this.setHoraCierre(horaCierre);
        this.setLatitud(latitud);
        this.setLongitud(longitud);
        this.setPromedio(promedio);
        this.setPlatillos(platillos);
        this.setServicios(servicios);
        this.setCiudad(ciudad);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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



    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
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

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Servicios> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicios> servicios) {
        this.servicios = servicios;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
