package com.cibertec.peliculas.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "detalle_alquiler")
@IdClass(DetalleAlquilerId.class)
public class DetalleAlquiler implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alquiler")
    private Alquiler alquiler;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pelicula")
    private Pelicula pelicula;

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad = 1;

}

