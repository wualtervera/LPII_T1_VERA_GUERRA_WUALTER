//package com.cibertec.peliculas.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//
//import java.util.List;
//
//@Entity
//@Table(name = "clientes")
//public class Cliente {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_cliente")
//    private Integer idCliente;
//
//    @NotNull
//    @Column(name = "nombre", length = 100, nullable = false)
//    private String nombre;
//
//    @NotNull
//    @Column(name = "email", length = 150, nullable = false, unique = true)
//    private String email;
//
//    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
//    private List<Alquiler> alquileres;
//
//    // Constructores
//    public Cliente() {
//    }
//
//    public Cliente(String nombre, String email) {
//        this.nombre = nombre;
//        this.email = email;
//    }
//
//    // Getters y Setters
//    public Integer getIdCliente() {
//        return idCliente;
//    }
//
//    public void setIdCliente(Integer idCliente) {
//        this.idCliente = idCliente;
//    }
//
//    public String getNombre() {
//        return nombre;
//    }
//
//    public void setNombre(String nombre) {
//        this.nombre = nombre;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public List<Alquiler> getAlquileres() {
//        return alquileres;
//    }
//
//    public void setAlquileres(List<Alquiler> alquileres) {
//        this.alquileres = alquileres;
//    }
//}