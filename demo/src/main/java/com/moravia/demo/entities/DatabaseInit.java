package com.moravia.demo.entities;

import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;

import com.moravia.demo.repository.UsuarioRepository;
import com.moravia.demo.repository.HabitacionRepository;
import com.moravia.demo.repository.ServicioRepository;
import com.moravia.demo.repository.RoomRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

import jakarta.transaction.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Random;

@Controller
@Transactional
public class DatabaseInit implements ApplicationRunner {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    HabitacionRepository habitacionRepository;

    @Autowired
    ServicioRepository servicioRepository;

    @Autowired
    RoomRepository roomRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Load usuarios
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("usuarios.json");
        JsonNode jsonNode = mapper.readTree(inputStream);

        for (JsonNode usuarioJson : jsonNode.get("usuarios")) {
            Usuario usuario = new Usuario(
                    usuarioJson.get("correo").asText(),
                    usuarioJson.get("clave").asText(),
                    usuarioJson.get("nombre").asText(),
                    usuarioJson.get("apellido").asText(),
                    usuarioJson.get("cedula").asText(),
                    usuarioJson.get("telefono").asText(),
                    usuarioJson.get("fotoPerfil").asText());
            usuarioRepository.save(usuario);
        }

        // Load habitaciones
        inputStream = getClass().getClassLoader().getResourceAsStream("habitaciones.json");
        jsonNode = mapper.readTree(inputStream);

        for (JsonNode habitacionJson : jsonNode.get("habitaciones")) {
            Habitacion habitacion = new Habitacion(
                    habitacionJson.get("nombre").asText(),
                    habitacionJson.get("tipo").asText(),
                    habitacionJson.get("descripcion").asText(),
                    habitacionJson.get("precio").floatValue(),
                    habitacionJson.get("capacidad").asText(),
                    habitacionJson.get("numeroCamas").asInt(),
                    habitacionJson.get("imagenUrl").asText());
            habitacionRepository.save(habitacion);
        }

        // Load servicios
        inputStream = getClass().getClassLoader().getResourceAsStream("servicios.json");
        jsonNode = mapper.readTree(inputStream);

        for (JsonNode servicioJson : jsonNode.get("servicios")) {
            Servicio servicio = new Servicio(
                    servicioJson.get("nombre").asText(),
                    servicioJson.get("descripcion").asText(),
                    servicioJson.get("precio").asInt(),
                    servicioJson.get("imagenUrl").asText());
            servicioRepository.save(servicio);
        }

        // Load rooms
        inputStream = getClass().getClassLoader().getResourceAsStream("room.json");
        jsonNode = mapper.readTree(inputStream);

        List<Habitacion> habitaciones = habitacionRepository.findAll();
        Random random = new Random();

        for (JsonNode roomJson : jsonNode.get("rooms")) {
            Room room = new Room(
                    roomJson.get("numeroHabitacion").asText(),
                    roomJson.get("disponible").asBoolean());

            // Selecciona una habitación al azar
            Habitacion habitacion = habitaciones.get(random.nextInt(habitaciones.size()));
            room.setTipo(habitacion);
            roomRepository.save(room);

            habitacion.addRoom(room);
            habitacionRepository.save(habitacion);
        }
    }
}