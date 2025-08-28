package com.moravia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private String idUsuario;
    private String correo;
    private String clave;
    private String nombre;
    private String apellido;
    private String cedula;
    private String telefono;
    private String fotoPerfil;
}
