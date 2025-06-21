package com.cibertec.peliculas.repository;

import com.cibertec.peliculas.model.entity.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {

    List<Pelicula> findByGeneroIgnoreCase(String genero);

    List<Pelicula> findByTituloContainingIgnoreCase(String titulo);

    List<Pelicula> findByStockGreaterThan(Integer stock);

    @Query("SELECT p FROM Pelicula p WHERE p.stock > 0 ORDER BY p.titulo ASC")
    List<Pelicula> findAvailableMovies();

    @Query("SELECT p FROM Pelicula p ORDER BY p.titulo ASC")
    List<Pelicula> findAllOrderByTitulo();

    @Query("SELECT DISTINCT p.genero FROM Pelicula p ORDER BY p.genero ASC")
    List<String> findAllGenres();
}