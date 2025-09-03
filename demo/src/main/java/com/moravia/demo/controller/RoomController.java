package com.moravia.demo.controller;

import com.moravia.demo.entities.Room;
import com.moravia.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // http://localhost:8081/rooms/tabla
    @GetMapping("/tabla")
    public String tablaRooms(Model model) {
        List<Room> rooms = roomService.findAll();
        model.addAttribute("rooms", rooms);
        return "tabla_rooms"; // crea tabla_rooms.html
    }

    // http://localhost:8081/rooms/{id}
    @GetMapping("/{id}")
    public String detalleRoom(Model model, @PathVariable String id) {
        Room room = roomService.findById(id);
        model.addAttribute("room", room);
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
    public String editarRoomForm(@PathVariable String id, Model model) {
        Room r = roomService.findById(id);
        if (r == null) return "redirect:/rooms/tabla";
        model.addAttribute("room", r);
        model.addAttribute("modo", "editar");
        return "form_rooms";
    }

    // http://localhost:8081/rooms/delete/{id}
    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable String id) {
        roomService.deleteById(id);
        return "redirect:/rooms/tabla";
    }

    // http://localhost:8081/rooms/save
    @PostMapping("/save")
    public String saveRoom(@ModelAttribute Room room) {
        roomService.add(room);
        return "redirect:/rooms/tabla";
    }
}
