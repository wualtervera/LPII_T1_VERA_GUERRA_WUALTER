package com.cibertec.peliculas.model.entity;

import com.cibertec.peliculas.model.enums.EstadoAlquiler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "alquileres")
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alquiler")
    private Integer idAlquiler;

    @NotNull(message = "La fecha es obligatoria")
    @JsonIgnore
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @DecimalMin(value = "0.0", message = "El total debe ser mayor o igual a 0")
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoAlquiler estado = EstadoAlquiler.ACTIVO;

    @OneToMany(mappedBy = "alquiler", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleAlquiler> detalles = new ArrayList<>();

    public void addDetalle(DetalleAlquiler detalle) {
        detalles.add(detalle);
        detalle.setAlquiler(this);
    }

    public void removeDetalle(DetalleAlquiler detalle) {
        detalles.remove(detalle);
        detalle.setAlquiler(null);
    }

}