package com.cibertec.peliculas.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "peliculas")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula")
    private Integer idPelicula;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "El género es obligatorio")
    @Size(max = 50, message = "El género no puede exceder 50 caracteres")
    @Column(name = "genero", nullable = false, length = 50)
    private String genero;

    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    @Column(name = "stock", nullable = false)
    private Integer stock = 0;
}