//package com.cibertec.peliculas.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import java.util.List;
//
//@Entity
//@Table(name = "peliculas")
//public class Pelicula {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_pelicula")
//    private Integer idPelicula;
//
//    @NotNull
//    @Column(name = "titulo", length = 200, nullable = false)
//    private String titulo;
//
//    @NotNull
//    @Column(name = "genero", length = 50, nullable = false)
//    private String genero;
//
//    @Column(name = "stock")
//    private Integer stock;
//
//    @OneToMany(mappedBy = "pelicula", cascade = {}, fetch = FetchType.LAZY)
//    private List<DetalleAlquiler> detalles;
//
//    // Constructores
//    public Pelicula() {
//    }
//
//    public Pelicula(String titulo, String genero, Integer stock) {
//        this.titulo = titulo;
//        this.genero = genero;
//        this.stock = stock;
//    }
//
//    // Getters y Setters
//    public Integer getIdPelicula() {
//        return idPelicula;
//    }
//
//    public void setIdPelicula(Integer idPelicula) {
//        this.idPelicula = idPelicula;
//    }
//
//    public String getTitulo() {
//        return titulo;
//    }
//
//    public void setTitulo(String titulo) {
//        this.titulo = titulo;
//    }
//
//    public String getGenero() {
//        return genero;
//    }
//
//    public void setGenero(String genero) {
//        this.genero = genero;
//    }
//
//    public Integer getStock() {
//        return stock;
//    }
//
//    public void setStock(Integer stock) {
//        this.stock = stock;
//    }
//
//    public List<DetalleAlquiler> getDetalles() {
//        return detalles;
//    }
//
//    public void setDetalles(List<DetalleAlquiler> detalles) {
//        this.detalles = detalles;
//    }
//}