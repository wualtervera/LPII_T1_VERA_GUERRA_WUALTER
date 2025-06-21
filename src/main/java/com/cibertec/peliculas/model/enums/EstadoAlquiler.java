package com.cibertec.peliculas.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EstadoAlquiler {
    ACTIVO("Activo"),
    DEVUELTO("Devuelto"),
    RETRASADO("Retrasado");

    private final String descripcion;
}