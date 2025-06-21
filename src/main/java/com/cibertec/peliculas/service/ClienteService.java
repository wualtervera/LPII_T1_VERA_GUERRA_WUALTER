package com.cibertec.peliculas.service;

import com.cibertec.peliculas.model.entity.Cliente;
import com.cibertec.peliculas.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAllOrderByNombre();
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public boolean existePorEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    public boolean existePorEmailYDiferenteId(String email, Integer id) {
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        return cliente.isPresent() && !cliente.get().getIdCliente().equals(id);
    }
}