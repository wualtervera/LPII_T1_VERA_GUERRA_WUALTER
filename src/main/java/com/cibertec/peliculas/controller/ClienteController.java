package com.cibertec.peliculas.controller;

import com.cibertec.peliculas.model.entity.Cliente;
import com.cibertec.peliculas.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
        if (clienteOpt.isPresent()) {
            model.addAttribute("cliente", clienteOpt.get());
            model.addAttribute("titulo", "Editar Cliente");
            return "clientes/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
            return "redirect:/clientes";
        }
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Cliente cliente,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        // Validar email único
        if (cliente.getIdCliente() == null) {
            // Nuevo cliente
            if (clienteService.existePorEmail(cliente.getEmail())) {
                bindingResult.rejectValue("email", "error.cliente", "El email ya está registrado");
            }
        } else {
            // Cliente existente
            if (clienteService.existePorEmailYDiferenteId(cliente.getEmail(), cliente.getIdCliente())) {
                bindingResult.rejectValue("email", "error.cliente", "El email ya está registrado");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("titulo", cliente.getIdCliente() == null ? "Nuevo Cliente" : "Editar Cliente");
            return "clientes/formulario";
        }

        try {
            clienteService.guardar(cliente);
            String mensaje = cliente.getIdCliente() == null ?
                    "Cliente creado exitosamente" : "Cliente actualizado exitosamente";
            redirectAttributes.addFlashAttribute("exito", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el cliente: " + e.getMessage());
        }

        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
            if (clienteOpt.isPresent()) {
                clienteService.eliminar(id);
                redirectAttributes.addFlashAttribute("exito", "Cliente eliminado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el cliente. Puede que tenga alquileres asociados.");
        }

        return "redirect:/clientes";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
        if (clienteOpt.isPresent()) {
            model.addAttribute("cliente", clienteOpt.get());
            return "clientes/ver";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
            return "redirect:/clientes";
        }
    }
}
