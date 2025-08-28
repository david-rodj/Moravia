package com.moravia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private String idUsuario;
    
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String correo;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String clave;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;
    
    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "^[0-9]{7,10}$", message = "La cédula debe tener entre 7 y 10 dígitos")
    private String cedula;
    
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]+$", message = "El formato del teléfono no es válido")
    private String telefono;
    
    private String fotoPerfil;
}