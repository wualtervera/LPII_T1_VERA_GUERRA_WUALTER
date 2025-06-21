//package com.cibertec.peliculas.model;
//
//import jakarta.persistence.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "alquileres")
//public class Alquiler {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_alquiler")
//    private Integer idAlquiler;
//
//    @Column(name = "fecha", nullable = false)
//    private LocalDate fecha;
//
//    @ManyToOne
//    @JoinColumn(name = "id_cliente", nullable = false)
//    private Cliente cliente;
//
//    @Column(name = "total", precision = 10, scale = 2)
//    private BigDecimal total;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "estado")
//    private EstadoAlquiler estado = EstadoAlquiler.ACTIVO;
//
//    @OneToMany(mappedBy = "alquiler", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<DetalleAlquiler> detalles = new ArrayList<>();
//
//    // Constructores
//    public Alquiler() {
//    }
//
//    public Alquiler(LocalDate fecha, Cliente cliente, BigDecimal total) {
//        this.fecha = fecha;
//        this.cliente = cliente;
//        this.total = total;
//    }
//
//    public List<DetalleAlquiler> getDetalles() {
//        return detalles;
//    }
//
//    public void setDetalles(List<DetalleAlquiler> detalles) {
//        this.detalles = detalles;
//    }
//
//    public EstadoAlquiler getEstado() {
//        return estado;
//    }
//
//    public void setEstado(EstadoAlquiler estado) {
//        this.estado = estado;
//    }
//
//    public BigDecimal getTotal() {
//        return total;
//    }
//
//    public void setTotal(BigDecimal total) {
//        this.total = total;
//    }
//
//    public Cliente getCliente() {
//        return cliente;
//    }
//
//    public void setCliente(Cliente cliente) {
//        this.cliente = cliente;
//    }
//
//    public LocalDate getFecha() {
//        return fecha;
//    }
//
//    public void setFecha(LocalDate fecha) {
//        this.fecha = fecha;
//    }
//
//    public Integer getIdAlquiler() {
//        return idAlquiler;
//    }
//
//    public void setIdAlquiler(Integer idAlquiler) {
//        this.idAlquiler = idAlquiler;
//    }
//
//    public void addDetalle(DetalleAlquiler detalle) {
//        if (detalles == null) {
//            detalles = new ArrayList<>();
//        }
//        detalle.setAlquiler(this); // <-- Importante: asignar relación inversa
//        detalles.add(detalle);
//    }
//}