package com.cibertec.peliculas.dao;

import com.cibertec.peliculas.model.Cliente;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ClienteDAO {

    private EntityManagerFactory emf;

    public ClienteDAO() {
        emf = Persistence.createEntityManagerFactory("AlquilerPU");
    }

    public List<Cliente> listarTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

}