package com.cibertec.peliculas.dao;

import com.cibertec.peliculas.model.Alquiler;
import com.cibertec.peliculas.model.DetalleAlquiler;
import com.cibertec.peliculas.model.Pelicula;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AlquilerDAO {

    private EntityManagerFactory emf;

    public AlquilerDAO() {
        emf = Persistence.createEntityManagerFactory("AlquilerPU");
    }

    public void guardar(Alquiler alquiler) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Guardar el alquiler
            em.persist(alquiler);
            em.flush(); // Obtiene el ID generado

            // Recorre los detalles y asegura relación inversa
            for (DetalleAlquiler detalle : alquiler.getDetalles()) {
                detalle.setAlquiler(alquiler);

                // Solo establece la película sin persistirla
                Pelicula pelicula = detalle.getPelicula();
                if (pelicula != null && pelicula.getIdPelicula() != null) {
                    // Carga la película desde la BD para evitar detached entity
                    Pelicula managedPelicula = em.find(Pelicula.class, pelicula.getIdPelicula());
                    detalle.setPelicula(managedPelicula); // Usa la entidad gestionada
                }

                em.persist(detalle);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar alquiler", e);
        } finally {
            em.close();
        }
    }

}