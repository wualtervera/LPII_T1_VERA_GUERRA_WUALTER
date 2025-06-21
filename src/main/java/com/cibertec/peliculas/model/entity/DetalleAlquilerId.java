package com.cibertec.peliculas.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class DetalleAlquilerId implements Serializable {
    private Integer alquiler;
    private Integer pelicula;
}
