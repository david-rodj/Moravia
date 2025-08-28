package com.moravia.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.moravia.demo.repository.ServicioRepository;

import jakarta.annotation.PostConstruct;

import com.moravia.demo.entities.Servicio;

@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    ServicioRepository repo;


    @Override
    public List<Servicio> findAll() {
        return repo.findAll();
    }

    @Override
    public Servicio findById(String idServicio) {
        return repo.findById(idServicio);
    }


    @Override
    public Servicio save(Servicio servicio) {
        return repo.save(servicio);
    }

    @Override
    public Servicio delete(String idServicio) {
        return repo.delete(idServicio);
    }

}
