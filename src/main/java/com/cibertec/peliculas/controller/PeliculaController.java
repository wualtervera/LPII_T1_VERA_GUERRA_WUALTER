package com.cibertec.peliculas.controller;

import com.cibertec.peliculas.model.entity.Pelicula;
import com.cibertec.peliculas.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    @GetMapping
    public String listar(Model model) {
        List<Pelicula> peliculas = peliculaService.listarTodas();

        // Calcular estadísticas
        long disponibles = peliculas.stream().filter(p -> p.getStock() > 0).count();
        long agotadas = peliculas.stream().filter(p -> p.getStock() == 0).count();

        model.addAttribute("peliculas", peliculas);
        model.addAttribute("totalPeliculas", peliculas.size());
        model.addAttribute("peliculasDisponibles", disponibles);
        model.addAttribute("peliculasAgotadas", agotadas);

        return "peliculas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        model.addAttribute("titulo", "Nueva Película");
        return "peliculas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Pelicula> peliculaOpt = peliculaService.buscarPorId(id);
        if (peliculaOpt.isPresent()) {
            model.addAttribute("pelicula", peliculaOpt.get());
            model.addAttribute("titulo", "Editar Película");
            return "peliculas/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Película no encontrada");
            return "redirect:/peliculas";
        }
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Pelicula pelicula,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("titulo", pelicula.getIdPelicula() == null ? "Nueva Película" : "Editar Película");
            return "peliculas/formulario";
        }

        try {
            peliculaService.guardar(pelicula);
            String mensaje = pelicula.getIdPelicula() == null ?
                    "Película creada exitosamente" : "Película actualizada exitosamente";
            redirectAttributes.addFlashAttribute("exito", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la película: " + e.getMessage());
        }

        return "redirect:/peliculas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Pelicula> peliculaOpt = peliculaService.buscarPorId(id);
            if (peliculaOpt.isPresent()) {
                peliculaService.eliminar(id);
                redirectAttributes.addFlashAttribute("exito", "Película eliminada exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Película no encontrada");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la película. Puede que tenga alquileres asociados.");
        }

        return "redirect:/peliculas";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Pelicula> peliculaOpt = peliculaService.buscarPorId(id);
        if (peliculaOpt.isPresent()) {
            model.addAttribute("pelicula", peliculaOpt.get());
            return "peliculas/ver";
        } else {
            redirectAttributes.addFlashAttribute("error", "Película no encontrada");
            return "redirect:/peliculas";
        }
    }

    @GetMapping("/disponibles")
    public String listarDisponibles(Model model) {
        List<Pelicula> peliculasDisponibles = peliculaService.buscarDisponibles();

        // Calcular estadísticas para películas disponibles
        int totalCopias = peliculasDisponibles.stream().mapToInt(Pelicula::getStock).sum();
        long generosUnicos = peliculasDisponibles.stream().map(Pelicula::getGenero).distinct().count();

        model.addAttribute("peliculas", peliculasDisponibles);
        model.addAttribute("totalCopias", totalCopias);
        model.addAttribute("generosUnicos", generosUnicos);

        return "peliculas/disponibles";
    }
}



/*package com.cibertec.peliculas.controller;

import com.cibertec.peliculas.model.entity.Pelicula;
import com.cibertec.peliculas.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("peliculas", peliculaService.listarTodas());
        return "peliculas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        model.addAttribute("titulo", "Nueva Película");
        return "peliculas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Pelicula> peliculaOpt = peliculaService.buscarPorId(id);
        if (peliculaOpt.isPresent()) {
            model.addAttribute("pelicula", peliculaOpt.get());
            model.addAttribute("titulo", "Editar Película");
            return "peliculas/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Película no encontrada");
            return "redirect:/peliculas";
        }
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Pelicula pelicula,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("titulo", pelicula.getIdPelicula() == null ? "Nueva Película" : "Editar Película");
            return "peliculas/formulario";
        }

        try {
            peliculaService.guardar(pelicula);
            String mensaje = pelicula.getIdPelicula() == null ?
                    "Película creada exitosamente" : "Película actualizada exitosamente";
            redirectAttributes.addFlashAttribute("exito", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la película: " + e.getMessage());
        }

        return "redirect:/peliculas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Pelicula> peliculaOpt = peliculaService.buscarPorId(id);
            if (peliculaOpt.isPresent()) {
                peliculaService.eliminar(id);
                redirectAttributes.addFlashAttribute("exito", "Película eliminada exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Película no encontrada");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la película. Puede que tenga alquileres asociados.");
        }

        return "redirect:/peliculas";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Pelicula> peliculaOpt = peliculaService.buscarPorId(id);
        if (peliculaOpt.isPresent()) {
            model.addAttribute("pelicula", peliculaOpt.get());
            return "peliculas/ver";
        } else {
            redirectAttributes.addFlashAttribute("error", "Película no encontrada");
            return "redirect:/peliculas";
        }
    }

    @GetMapping("/disponibles")
    public String listarDisponibles(Model model) {
        model.addAttribute("peliculas", peliculaService.buscarDisponibles());
        return "peliculas/disponibles";
    }
}*/
