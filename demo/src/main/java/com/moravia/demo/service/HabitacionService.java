package com.moravia.demo.service;

import java.util.List;
import com.moravia.demo.entities.Habitacion;

public interface HabitacionService {

    public List<Habitacion> findAll();

    public Habitacion findById(String idHabitacion);

    public void add(Habitacion habitacion);

    public void update(Habitacion habitacion);

    public void deleteById(String idHabitacion);
}
