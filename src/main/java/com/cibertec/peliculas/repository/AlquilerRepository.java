package com.cibertec.peliculas.repository;

import com.cibertec.peliculas.model.entity.Alquiler;
import com.cibertec.peliculas.model.enums.EstadoAlquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Integer> {

    List<Alquiler> findByEstado(EstadoAlquiler estado);

    List<Alquiler> findByClienteIdCliente(Integer idCliente);

    List<Alquiler> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT a FROM Alquiler a JOIN FETCH a.cliente ORDER BY a.fecha DESC")
    List<Alquiler> findAllWithCliente();

    @Query("SELECT a FROM Alquiler a JOIN FETCH a.cliente LEFT JOIN FETCH a.detalles d LEFT JOIN FETCH d.pelicula WHERE a.idAlquiler = :id")
    Alquiler findByIdWithDetails(@Param("id") Integer id);

    @Query("SELECT COUNT(a) FROM Alquiler a WHERE a.estado = :estado")
    Long countByEstado(@Param("estado") EstadoAlquiler estado);
}