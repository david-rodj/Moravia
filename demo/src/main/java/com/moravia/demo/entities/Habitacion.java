package com.moravia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Habitacion {
    private String idHabitacion;
    private String nombre;
    private String tipo;
    private String descripcion;
    private float precio;
    private String capacidad;
    private int numeroCamas;
    private String imagenUrl;
}