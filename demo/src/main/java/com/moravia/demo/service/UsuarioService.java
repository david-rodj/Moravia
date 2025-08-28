package com.moravia.demo.service;

import java.util.List;
import com.moravia.demo.entities.Usuario;

public interface UsuarioService {
    // Operaciones CRUD básicas
    public List<Usuario> findAll();
    public Usuario findById(String idUsuario);
    public Usuario save(Usuario usuario);
    public Usuario update(Usuario usuario);
    public boolean deleteById(String idUsuario);
    
    // Operaciones de validación
    public boolean existsByEmail(String email);
    public boolean existsByCedula(String cedula);
    public Usuario findByEmail(String email);
    
    // Operaciones de seguridad
    public boolean validarCredenciales(String email, String clave);
}