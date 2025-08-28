package com.moravia.demo.service;

import java.util.List;
import com.moravia.demo.entities.Habitacion;

public interface HabitacionService {

    List<Habitacion> findAll();

    Habitacion findById(String idHabitacion);

    Habitacion save(Habitacion habitacion);

    Habitacion delete(String idHabitacion);
}
