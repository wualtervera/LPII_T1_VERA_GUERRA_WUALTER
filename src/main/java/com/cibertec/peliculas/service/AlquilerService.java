package com.cibertec.peliculas.service;

import com.cibertec.peliculas.model.entity.Alquiler;
import com.cibertec.peliculas.model.entity.DetalleAlquiler;
import com.cibertec.peliculas.model.enums.EstadoAlquiler;
import com.cibertec.peliculas.repository.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private PeliculaService peliculaService;

    public List<Alquiler> listarTodos() {
        return alquilerRepository.findAllWithCliente();
    }

    public Optional<Alquiler> buscarPorId(Integer id) {
        return alquilerRepository.findById(id);
    }

    public Alquiler buscarPorIdConDetalles(Integer id) {
        return alquilerRepository.findByIdWithDetails(id);
    }

    public Alquiler guardar(Alquiler alquiler) {
        // Si es un nuevo alquiler, establecer la fecha actual
        if (alquiler.getIdAlquiler() == null) {
            alquiler.setFecha(LocalDate.now());
        }

        // Calcular el total basado en los detalles
        BigDecimal total = calcularTotal(alquiler);
        alquiler.setTotal(total);

        // Guardar el alquiler
        Alquiler alquilerGuardado = alquilerRepository.save(alquiler);

        // Reducir el stock de las películas alquiladas
        for (DetalleAlquiler detalle : alquiler.getDetalles()) {
            peliculaService.reducirStock(detalle.getPelicula().getIdPelicula(), detalle.getCantidad());
        }

        return alquilerGuardado;
    }

    public void eliminar(Integer id) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(id);
        if (alquilerOpt.isPresent()) {
            Alquiler alquiler = alquilerOpt.get();

            // Restaurar el stock de las películas
            for (DetalleAlquiler detalle : alquiler.getDetalles()) {
                peliculaService.aumentarStock(detalle.getPelicula().getIdPelicula(), detalle.getCantidad());
            }

            alquilerRepository.deleteById(id);
        }
    }

    public List<Alquiler> buscarPorEstado(EstadoAlquiler estado) {
        return alquilerRepository.findByEstado(estado);
    }

    public List<Alquiler> buscarPorCliente(Integer idCliente) {
        return alquilerRepository.findByClienteIdCliente(idCliente);
    }

    public List<Alquiler> buscarPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return alquilerRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public boolean cambiarEstado(Integer idAlquiler, EstadoAlquiler nuevoEstado) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        if (alquilerOpt.isPresent()) {
            Alquiler alquiler = alquilerOpt.get();
            EstadoAlquiler estadoAnterior = alquiler.getEstado();
            alquiler.setEstado(nuevoEstado);

            // Si se devuelve el alquiler, restaurar el stock
            if (nuevoEstado == EstadoAlquiler.DEVUELTO && estadoAnterior != EstadoAlquiler.DEVUELTO) {
                for (DetalleAlquiler detalle : alquiler.getDetalles()) {
                    peliculaService.aumentarStock(detalle.getPelicula().getIdPelicula(), detalle.getCantidad());
                }
            }

            alquilerRepository.save(alquiler);
            return true;
        }
        return false;
    }

    public Long contarPorEstado(EstadoAlquiler estado) {
        return alquilerRepository.countByEstado(estado);
    }

    private BigDecimal calcularTotal(Alquiler alquiler) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal precioPorPelicula = new BigDecimal("5.50"); // Precio fijo por película

        for (DetalleAlquiler detalle : alquiler.getDetalles()) {
            BigDecimal subtotal = precioPorPelicula.multiply(new BigDecimal(detalle.getCantidad()));
            total = total.add(subtotal);
        }

        return total;
    }
}