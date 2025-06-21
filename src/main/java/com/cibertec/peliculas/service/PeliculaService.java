package com.cibertec.peliculas.service;

import com.cibertec.peliculas.model.entity.Pelicula;
import com.cibertec.peliculas.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;

    public List<Pelicula> listarTodas() {
        return peliculaRepository.findAllOrderByTitulo();
    }

    public Optional<Pelicula> buscarPorId(Integer id) {
        return peliculaRepository.findById(id);
    }

    public Pelicula guardar(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }

    public void eliminar(Integer id) {
        peliculaRepository.deleteById(id);
    }

    public List<Pelicula> buscarPorGenero(String genero) {
        return peliculaRepository.findByGeneroIgnoreCase(genero);
    }

    public List<Pelicula> buscarPorTitulo(String titulo) {
        return peliculaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Pelicula> buscarDisponibles() {
        List<Pelicula> availableMovies = peliculaRepository.findAvailableMovies();
        return availableMovies;
    }

    public List<String> listarGeneros() {
        return peliculaRepository.findAllGenres();
    }

    public boolean actualizarStock(Integer idPelicula, Integer nuevaCantidad) {
        Optional<Pelicula> peliculaOpt = peliculaRepository.findById(idPelicula);
        if (peliculaOpt.isPresent()) {
            Pelicula pelicula = peliculaOpt.get();
            pelicula.setStock(nuevaCantidad);
            peliculaRepository.save(pelicula);
            return true;
        }
        return false;
    }

    public boolean reducirStock(Integer idPelicula, Integer cantidad) {
        Optional<Pelicula> peliculaOpt = peliculaRepository.findById(idPelicula);
        if (peliculaOpt.isPresent()) {
            Pelicula pelicula = peliculaOpt.get();
            if (pelicula.getStock() >= cantidad) {
                pelicula.setStock(pelicula.getStock() - cantidad);
                peliculaRepository.save(pelicula);
                return true;
            }
        }
        return false;
    }

    public void aumentarStock(Integer idPelicula, Integer cantidad) {
        Optional<Pelicula> peliculaOpt = peliculaRepository.findById(idPelicula);
        if (peliculaOpt.isPresent()) {
            Pelicula pelicula = peliculaOpt.get();
            pelicula.setStock(pelicula.getStock() + cantidad);
            peliculaRepository.save(pelicula);
        }
    }
}