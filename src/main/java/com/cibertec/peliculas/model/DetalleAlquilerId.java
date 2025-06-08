package com.cibertec.peliculas.model;

import java.io.Serializable;

public class DetalleAlquilerId implements Serializable {
    private Integer alquiler;
    private Integer pelicula;

    public DetalleAlquilerId() {
    }

    public DetalleAlquilerId(Integer alquiler, Integer pelicula) {
        this.alquiler = alquiler;
        this.pelicula = pelicula;
    }

    public Integer getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(Integer alquiler) {
        this.alquiler = alquiler;
    }

    public Integer getPelicula() {
        return pelicula;
    }

    public void setPelicula(Integer pelicula) {
        this.pelicula = pelicula;
    }
}