package com.cibertec.peliculas.repository;

import com.cibertec.peliculas.model.entity.DetalleAlquiler;
import com.cibertec.peliculas.model.entity.DetalleAlquilerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleAlquilerRepository extends JpaRepository<DetalleAlquiler, DetalleAlquilerId> {

    List<DetalleAlquiler> findByAlquilerIdAlquiler(Integer idAlquiler);

    List<DetalleAlquiler> findByPeliculaIdPelicula(Integer idPelicula);

    @Query("SELECT d FROM DetalleAlquiler d JOIN FETCH d.pelicula WHERE d.alquiler.idAlquiler = :idAlquiler")
    List<DetalleAlquiler> findByAlquilerIdWithPelicula(@Param("idAlquiler") Integer idAlquiler);

    void deleteByAlquilerIdAlquiler(Integer idAlquiler);
}