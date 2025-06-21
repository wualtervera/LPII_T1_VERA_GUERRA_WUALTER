package com.cibertec.peliculas.controller;

import com.cibertec.peliculas.model.enums.EstadoAlquiler;
import com.cibertec.peliculas.service.AlquilerService;
import com.cibertec.peliculas.service.ClienteService;
import com.cibertec.peliculas.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private AlquilerService alquilerService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalClientes", clienteService.listarTodos().size());
        model.addAttribute("totalPeliculas", peliculaService.listarTodas().size());
        model.addAttribute("alquileresActivos", alquilerService.contarPorEstado(EstadoAlquiler.ACTIVO));
        model.addAttribute("alquileresRetrasados", alquilerService.contarPorEstado(EstadoAlquiler.RETRASADO));

        return "dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return index(model);
    }
}