package com.cibertec.peliculas.controller;

import com.cibertec.peliculas.model.entity.Alquiler;
import com.cibertec.peliculas.model.entity.Cliente;
import com.cibertec.peliculas.model.entity.DetalleAlquiler;
import com.cibertec.peliculas.model.entity.Pelicula;
import com.cibertec.peliculas.model.enums.EstadoAlquiler;
import com.cibertec.peliculas.service.AlquilerService;
import com.cibertec.peliculas.service.ClienteService;
import com.cibertec.peliculas.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PeliculaService peliculaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alquileres", alquilerService.listarTodos());
        return "alquileres/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Alquiler alquiler = new Alquiler();
        alquiler.setTotal(BigDecimal.ZERO);
        alquiler.setEstado(EstadoAlquiler.ACTIVO);
        alquiler.setFecha(LocalDate.now());

        List<Cliente> clientes = clienteService.listarTodos();
        List<Pelicula> peliculas = peliculaService.buscarDisponibles();

        // Debug - agrega estos logs temporalmente
        System.out.println("Número de clientes: " + clientes.size());
        System.out.println("Número de películas: " + peliculas.size());
        if (!peliculas.isEmpty()) {
            System.out.println("Primera película: " + peliculas.get(0).getTitulo());
        }

        model.addAttribute("alquiler", alquiler);
        model.addAttribute("clientes", clientes);
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("estados", EstadoAlquiler.values());
        model.addAttribute("titulo", "Nuevo Alquiler");

        // Agregamos campos adicionales para el formulario
        model.addAttribute("peliculaSeleccionada", "");
        model.addAttribute("cantidad", 1);
        model.addAttribute("precioUnitario", BigDecimal.ZERO);

        return "alquileres/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Alquiler> alquilerOpt = alquilerService.buscarPorId(id);
        if (alquilerOpt.isPresent()) {
            Alquiler alquiler = alquilerOpt.get();

            model.addAttribute("alquiler", alquiler);
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("peliculas", peliculaService.listarTodas());
            model.addAttribute("estados", EstadoAlquiler.values());
            model.addAttribute("titulo", "Editar Alquiler");

            // Si tiene detalles, cargar el primero para edición
            if (!alquiler.getDetalles().isEmpty()) {
                DetalleAlquiler primerDetalle = alquiler.getDetalles().get(0);
                model.addAttribute("peliculaSeleccionada", primerDetalle.getPelicula().getIdPelicula());
                model.addAttribute("cantidad", primerDetalle.getCantidad());
                // Calcular precio unitario basado en el total
                BigDecimal precioUnitario = alquiler.getTotal().divide(
                        BigDecimal.valueOf(primerDetalle.getCantidad()), 2, BigDecimal.ROUND_HALF_UP);
                model.addAttribute("precioUnitario", precioUnitario);
            } else {
                model.addAttribute("peliculaSeleccionada", "");
                model.addAttribute("cantidad", 1);
                model.addAttribute("precioUnitario", BigDecimal.ZERO);
            }

            return "alquileres/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Alquiler no encontrado");
            return "redirect:/alquileres";
        }
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Alquiler alquiler,
                          @RequestParam("peliculaSeleccionada") Integer idPelicula,
                          @RequestParam("cantidad") Integer cantidad,
                          @RequestParam("precioUnitario") BigDecimal precioUnitario,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || idPelicula == null || cantidad <= 0) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("peliculas", peliculaService.listarTodas());
            model.addAttribute("estados", EstadoAlquiler.values());
            model.addAttribute("titulo", alquiler.getIdAlquiler() == null ? "Nuevo Alquiler" : "Editar Alquiler");
            model.addAttribute("peliculaSeleccionada", idPelicula);
            model.addAttribute("cantidad", cantidad);
            model.addAttribute("precioUnitario", precioUnitario);

            if (idPelicula == null) {
                model.addAttribute("errorPelicula", "Debe seleccionar una película");
            }
            if (cantidad <= 0) {
                model.addAttribute("errorCantidad", "La cantidad debe ser mayor a 0");
            }

            return "alquileres/formulario";
        }

        try {
            // Buscar la película seleccionada
            Optional<Pelicula> peliculaOpt = peliculaService.buscarPorId(idPelicula);
            if (!peliculaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Película no encontrada");
                return "redirect:/alquileres/nuevo";
            }

            Pelicula pelicula = peliculaOpt.get();

            // Calcular el total
            BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
            alquiler.setTotal(total);

            // Si es nuevo alquiler, establecer fecha
            if (alquiler.getIdAlquiler() == null) {
                alquiler.setFecha(LocalDate.now());
            }

            // Limpiar detalles existentes si es edición
            alquiler.getDetalles().clear();

            // Crear el detalle del alquiler
            DetalleAlquiler detalle = new DetalleAlquiler();
            detalle.setPelicula(pelicula);
            detalle.setCantidad(cantidad);
            alquiler.addDetalle(detalle);

            alquilerService.guardar(alquiler);

            String mensaje = alquiler.getIdAlquiler() == null ?
                    "Alquiler creado exitosamente" : "Alquiler actualizado exitosamente";
            redirectAttributes.addFlashAttribute("exito", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el alquiler: " + e.getMessage());
        }

        return "redirect:/alquileres";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Alquiler> alquilerOpt = alquilerService.buscarPorId(id);
            if (alquilerOpt.isPresent()) {
                alquilerService.eliminar(id);
                redirectAttributes.addFlashAttribute("exito", "Alquiler eliminado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Alquiler no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el alquiler: " + e.getMessage());
        }

        return "redirect:/alquileres";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Alquiler alquiler = alquilerService.buscarPorIdConDetalles(id);
        if (alquiler != null) {
            model.addAttribute("alquiler", alquiler);
            return "alquileres/ver";
        } else {
            redirectAttributes.addFlashAttribute("error", "Alquiler no encontrado");
            return "redirect:/alquileres";
        }
    }

    @PostMapping("/cambiarEstado/{id}")
    public String cambiarEstado(@PathVariable Integer id,
                                @RequestParam EstadoAlquiler nuevoEstado,
                                RedirectAttributes redirectAttributes) {
        try {
            if (alquilerService.cambiarEstado(id, nuevoEstado)) {
                redirectAttributes.addFlashAttribute("exito", "Estado del alquiler actualizado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Alquiler no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar el estado: " + e.getMessage());
        }

        return "redirect:/alquileres";
    }

    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("alquileres", alquilerService.buscarPorEstado(EstadoAlquiler.ACTIVO));
        model.addAttribute("titulo", "Alquileres Activos");
        return "alquileres/listar";
    }

    @GetMapping("/retrasados")
    public String listarRetrasados(Model model) {
        model.addAttribute("alquileres", alquilerService.buscarPorEstado(EstadoAlquiler.RETRASADO));
        model.addAttribute("titulo", "Alquileres Retrasados");
        return "alquileres/listar";
    }
}


/*
package com.cibertec.peliculas.controller;

import com.cibertec.peliculas.model.entity.Alquiler;
import com.cibertec.peliculas.model.entity.Cliente;
import com.cibertec.peliculas.model.entity.Pelicula;
import com.cibertec.peliculas.model.enums.EstadoAlquiler;
import com.cibertec.peliculas.service.AlquilerService;
import com.cibertec.peliculas.service.ClienteService;
import com.cibertec.peliculas.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private PeliculaService peliculaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alquileres", alquilerService.listarTodos());
        return "alquileres/listar";
    }

//    @GetMapping("/nuevo")
//    public String mostrarFormularioNuevo(Model model) {
//        Alquiler alquiler = new Alquiler();
//        alquiler.setTotal(BigDecimal.ZERO);
//        alquiler.setEstado(EstadoAlquiler.ACTIVO);
//
//        model.addAttribute("alquiler", alquiler);
//        model.addAttribute("clientes", clienteService.listarTodos());
//        model.addAttribute("peliculas", peliculaService.buscarDisponibles());
//        model.addAttribute("estados", EstadoAlquiler.values());
//        model.addAttribute("titulo", "Nuevo Alquiler");
//        return "alquileres/formulario";
//    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Alquiler alquiler = new Alquiler();
        alquiler.setTotal(BigDecimal.ZERO);
        alquiler.setEstado(EstadoAlquiler.ACTIVO);

        List<Cliente> clientes = clienteService.listarTodos();
        List<Pelicula> peliculas = peliculaService.buscarDisponibles();

        // Debug - agrega estos logs temporalmente
        System.out.println("Número de clientes: " + clientes.size());
        System.out.println("Número de películas: " + peliculas.size());
        if (!peliculas.isEmpty()) {
            System.out.println("Primera película: " + peliculas.get(0).getTitulo());
        }

        model.addAttribute("alquiler", alquiler);
        model.addAttribute("clientes", clientes);
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("estados", EstadoAlquiler.values());
        model.addAttribute("titulo", "Nuevo Alquiler");
        return "alquileres/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Alquiler> alquilerOpt = alquilerService.buscarPorId(id);
        if (alquilerOpt.isPresent()) {
            model.addAttribute("alquiler", alquilerOpt.get());
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("peliculas", peliculaService.listarTodas());
            model.addAttribute("estados", EstadoAlquiler.values());
            model.addAttribute("titulo", "Editar Alquiler");
            return "alquileres/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Alquiler no encontrado");
            return "redirect:/alquileres";
        }
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Alquiler alquiler,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("peliculas", peliculaService.listarTodas());
            model.addAttribute("estados", EstadoAlquiler.values());
            model.addAttribute("titulo", alquiler.getIdAlquiler() == null ? "Nuevo Alquiler" : "Editar Alquiler");
            return "alquileres/formulario";
        }

        try {
            alquilerService.guardar(alquiler);
            String mensaje = alquiler.getIdAlquiler() == null ?
                    "Alquiler creado exitosamente" : "Alquiler actualizado exitosamente";
            redirectAttributes.addFlashAttribute("exito", mensaje);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el alquiler: " + e.getMessage());
        }

        return "redirect:/alquileres";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Alquiler> alquilerOpt = alquilerService.buscarPorId(id);
            if (alquilerOpt.isPresent()) {
                alquilerService.eliminar(id);
                redirectAttributes.addFlashAttribute("exito", "Alquiler eliminado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Alquiler no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el alquiler: " + e.getMessage());
        }

        return "redirect:/alquileres";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Alquiler alquiler = alquilerService.buscarPorIdConDetalles(id);
        if (alquiler != null) {
            model.addAttribute("alquiler", alquiler);
            return "alquileres/ver";
        } else {
            redirectAttributes.addFlashAttribute("error", "Alquiler no encontrado");
            return "redirect:/alquileres";
        }
    }

    @PostMapping("/cambiarEstado/{id}")
    public String cambiarEstado(@PathVariable Integer id,
                                @RequestParam EstadoAlquiler nuevoEstado,
                                RedirectAttributes redirectAttributes) {
        try {
            if (alquilerService.cambiarEstado(id, nuevoEstado)) {
                redirectAttributes.addFlashAttribute("exito", "Estado del alquiler actualizado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "Alquiler no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar el estado: " + e.getMessage());
        }

        return "redirect:/alquileres";
    }

    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("alquileres", alquilerService.buscarPorEstado(EstadoAlquiler.ACTIVO));
        model.addAttribute("titulo", "Alquileres Activos");
        return "alquileres/listar";
    }

    @GetMapping("/retrasados")
    public String listarRetrasados(Model model) {
        model.addAttribute("alquileres", alquilerService.buscarPorEstado(EstadoAlquiler.RETRASADO));
        model.addAttribute("titulo", "Alquileres Retrasados");
        return "alquileres/listar";
    }
}
*/
