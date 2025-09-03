package com.moravia.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.moravia.demo.entities.Habitacion;
import com.moravia.demo.service.HabitacionService;

@Controller
@RequestMapping("/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    // http://localhost:8081/habitaciones/tabla
    @GetMapping("/tabla")
    public String tablaHabitaciones(Model model) {
        List<Habitacion> habitaciones = habitacionService.findAll();
        model.addAttribute("habitaciones", habitaciones);
        return "tabla_habitaciones";
    }

    // http://localhost:8081/habitaciones/lista
    @GetMapping("/lista")
    public String mostrarHabitaciones(Model model) {
        model.addAttribute("habitaciones", habitacionService.findAll());
        return "habitacionescl";
    }

    // http://localhost:8081/habitaciones/{idHabitacion}
    
    @GetMapping("/{idHabitacion}")
    public String detalleHabitacion(Model model, @PathVariable String idHabitacion) {
        Habitacion habitacion = habitacionService.findById(idHabitacion);
        model.addAttribute("habitacion", habitacion);
        return "detalle_habitacion";
    }
        
    // http://localhost:8081/habitaciones/nuevo
    @GetMapping("/nuevo")
    public String nuevaHabitacionForm(Model model) {
        model.addAttribute("habitacion", new Habitacion());
        model.addAttribute("modo", "crear");
        return "form_habitaciones";
    }

    // http://localhost:8081/habitaciones/delete/{idHabitacion}
    @GetMapping("/delete/{idHabitacion}")
    public String deleteHabitacion(@PathVariable String idHabitacion) {
        habitacionService.deleteById(idHabitacion);
        return "redirect:/habitaciones/tabla";
    }

    // http://localhost:8081/habitaciones/editar/{idHabitacion}
    @GetMapping("/editar/{idHabitacion}")
    public String editarHabitacionForm(@PathVariable String idHabitacion, Model model) {
        Habitacion h = habitacionService.findById(idHabitacion);
        if (h == null) return "redirect:/habitaciones/lista";
        model.addAttribute("habitacion", h);
        model.addAttribute("modo", "editar");
        return "form_habitaciones";
    }

    // http://localhost:8081/habitaciones/save
    @PostMapping("/save")
    public String saveHabitacion(@ModelAttribute Habitacion habitacion) {
        habitacionService.add(habitacion);
        return "redirect:/habitaciones/tabla";
    }
}
