package com.cibertec.peliculas.repository;

import com.cibertec.peliculas.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByEmail(String email);

    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT c FROM Cliente c ORDER BY c.nombre ASC")
    List<Cliente> findAllOrderByNombre();

    boolean existsByEmail(String email);
}
