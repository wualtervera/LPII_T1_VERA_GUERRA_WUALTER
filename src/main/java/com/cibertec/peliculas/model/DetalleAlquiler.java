//package com.cibertec.peliculas.model;
//
//import jakarta.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "detalle_alquiler")
//@IdClass(DetalleAlquilerId.class) // Usa la clase como clave primaria compuesta
//public class DetalleAlquiler implements Serializable {
//
//    // Campos de la clave primaria compuesta
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "id_alquiler", nullable = false)
//    private Alquiler alquiler;
//
//    @ManyToOne(cascade = {}) // No intentará persistir la película
//    @JoinColumn(name = "id_pelicula")
//    private Pelicula pelicula;
//
//    // Otros campos
//    @Column(name = "cantidad", nullable = false)
//    private Integer cantidad;
//
//    // Constructor vacío
//    public DetalleAlquiler() {
//    }
//
//    // Constructor completo
//    public DetalleAlquiler(Alquiler alquiler, Pelicula pelicula, Integer cantidad) {
//        this.alquiler = alquiler;
//        this.pelicula = pelicula;
//        this.cantidad = cantidad;
//    }
//
//    // Getters y Setters
//    public Alquiler getAlquiler() {
//        return alquiler;
//    }
//
//    public void setAlquiler(Alquiler alquiler) {
//        this.alquiler = alquiler;
//    }
//
//    public Pelicula getPelicula() {
//        return pelicula;
//    }
//
//    public void setPelicula(Pelicula pelicula) {
//        this.pelicula = pelicula;
//    }
//
//    public Integer getCantidad() {
//        return cantidad;
//    }
//
//    public void setCantidad(Integer cantidad) {
//        this.cantidad = cantidad;
//    }
//}