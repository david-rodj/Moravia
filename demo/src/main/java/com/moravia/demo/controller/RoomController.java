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
import com.moravia.demo.entities.Room;
import com.moravia.demo.service.HabitacionService;
import com.moravia.demo.service.RoomService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private HabitacionService habitacionService;

    // http://localhost:8081/rooms/tabla
    @GetMapping("/tabla")
    public String tablaRooms(Model model, HttpSession session) {
        List<Room> rooms = roomService.findAll();
        model.addAttribute("rooms", rooms);
        return "tabla_rooms"; // crea tabla_rooms.html
    }

    // http://localhost:8081/rooms/{id}
    @GetMapping("/{id}")
    public String detalleRoom(Model model, @PathVariable String id, HttpSession session) {

        System.out.println("ID recibido: " + id); // Depuración
        Room room = roomService.findById(id);
        model.addAttribute("room", room);
        model.addAttribute("tipo", room.getTipo().getNombre());
        return "detalle_room"; // crea detalle_room.html
    }

    // http://localhost:8081/rooms/nuevo
    @GetMapping("/nuevo")
    public String nuevoRoomForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("modo", "crear");
        return "form_rooms"; // crea form_rooms.html
    }

    // http://localhost:8081/rooms/editar/{id}
    @GetMapping("/editar/{id}")
    public String editarRoomForm(@PathVariable String id, Model model, HttpSession session) {
        List<Habitacion> habitaciones = habitacionService.findAll();
        model.addAttribute("habitaciones", habitaciones);
        Room room = roomService.findById(id);
        if (room == null)
            return "redirect:/rooms/tabla";
        model.addAttribute("room", room);
        model.addAttribute("modo", "editar");
        return "form_rooms";
    }

    // http://localhost:8081/rooms/delete/{id}
    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable String id) {
        roomService.deleteById(id);
        return "redirect:/rooms/tabla";
    }


    @PostMapping("/save")
    public String saveRoom(@ModelAttribute Room room) {
        // Si vino solo el id dentro de room.tipo.idHabitacion, lo buscamos y seteamos
        // la entidad:
        if (room.getTipo() != null && room.getTipo().getIdHabitacion() != null) {
            habitacionService.findById(room.getTipo().getIdHabitacion());
        } else {
            room.setTipo(null); // por si envían vacío
        }

        roomService.add(room);
        return "redirect:/rooms/tabla";
    }
}
