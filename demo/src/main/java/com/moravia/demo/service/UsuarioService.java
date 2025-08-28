package com.moravia.demo.service;

import java.util.List;

import com.moravia.demo.entities.Usuario;

public interface UsuarioService {
    public List<Usuario> findAll();
    public Usuario findById(String idUsuario);
    
}
