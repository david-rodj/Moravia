package com.moravia.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.moravia.demo.entities.Servicio;
import com.moravia.demo.service.ServicioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    // http://localhost:8081/servicios/tabla
    @GetMapping("/tabla")
    public String tablaServicios(Model model) {
        List<Servicio> servicios = servicioService.findAll();
        model.addAttribute("servicios", servicios);
        return "tabla_servicios";
    }

    // http://localhost:8081/servicios/lista
    @GetMapping("/lista")
    public String mostrarServicios(Model model, HttpSession session) {
        model.addAttribute("servicios", servicioService.findAll());
        return "servicioscl";
    }

    // http://localhost:8081/servicios/{idServicio}
    @GetMapping("/{idServicio}")
    public String detalleServicio(Model model, @PathVariable Long idServicio, HttpSession session) { 
        Servicio servicio = servicioService.findById(idServicio);
        if (servicio == null) {
            return "redirect:/servicios/lista?error=Servicio+no+encontrado";
        }
        
        model.addAttribute("servicio", servicio);
        return "detalle_servicio";
    }

    // http://localhost:8081/servicios/nuevo
    @GetMapping("/nuevo")
    public String nuevoServicioForm(Model model) {
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("modo", "crear");
        return "form_servicios";
    }

    // http://localhost:8081/servicios/delete/{idServicio}
    @GetMapping("/delete/{idServicio}")
    public String deleteServicio(@PathVariable Long idServicio) {
        servicioService.deleteById(idServicio);
        return "redirect:/servicios/tabla";
    }

    @GetMapping("/editar/{idServicio}")
    public String editarServicioForm(@PathVariable Long idServicio, Model model) {
        Servicio s = servicioService.findById(idServicio);
        if (s == null) return "redirect:/servicios/lista";
        model.addAttribute("servicio", s);
        model.addAttribute("modo", "editar");
        return "form_servicios";
    }

    @PostMapping("/save")
    public String saveServicio(@ModelAttribute Servicio servicio) {
        servicioService.add(servicio); 
        return "redirect:/servicios/tabla";
    }
}