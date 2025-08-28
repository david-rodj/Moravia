package com.moravia.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.moravia.demo.entities.Habitacion;
import com.moravia.demo.repository.HabitacionRepository;

@Service
public class HabitacionServiceImpl implements HabitacionService {

    @Autowired
    private HabitacionRepository repo;

    @Override
    public List<Habitacion> findAll() {
        return repo.findAll();
    }

    @Override
    public Habitacion findById(String idHabitacion) {
        return repo.findById(idHabitacion);
    }

    @Override
    public Habitacion save(Habitacion habitacion) {
        return repo.save(habitacion);
    }

    @Override
    public Habitacion delete(String idHabitacion) {
        return repo.delete(idHabitacion);
    }
}
