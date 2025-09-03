package com.moravia.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Servicio {
    
    @Id
    private String idServicio;
    
    private String nombre;
    
    private String descripcion;
    
    private float precio;
    
    private String imagenUrl;
}