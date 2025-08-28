package com.moravia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Servicio {
    private String idServicio;
    private String nombre;
    private String descripcion;
    private float precio;
    private String imagenUrl;
}