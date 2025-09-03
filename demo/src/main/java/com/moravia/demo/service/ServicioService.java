package com.moravia.demo.service;

import java.util.List;

import com.moravia.demo.entities.Servicio;

public interface ServicioService {

    public List<Servicio> findAll();

    public Servicio findById(String idServicio);

    public void add(Servicio servicio);

    public void update(Servicio servicio);

    public void deleteById(String idServicio);

}
