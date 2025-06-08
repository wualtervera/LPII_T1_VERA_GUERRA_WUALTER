package com.cibertec.peliculas.dao;

import com.cibertec.peliculas.model.Pelicula;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class PeliculaDAO {

    private EntityManagerFactory emf;

    public PeliculaDAO() {
        emf = Persistence.createEntityManagerFactory("AlquilerPU");
    }

    public List<Pelicula> listarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pelicula p", Pelicula.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void actualizar(Pelicula pelicula) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pelicula);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

}