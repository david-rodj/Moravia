package com.moravia.demo.service;

import java.util.List;

import com.moravia.demo.entities.Servicio;

public interface ServicioService {

    public List<Servicio> findAll();

    public Servicio findById(String idServicio);

    public Servicio save(Servicio servicio);

    public Servicio delete(String idServicio);

}
