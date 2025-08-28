package com.moravia.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.moravia.demo.entities.Usuario;
import com.moravia.demo.repository.UsuarioRepository;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Override
    public List<Usuario> findAll() {
        return repo.findAll();
    }

    @Override
    public Usuario findById(String idUsuario) {
        return repo.findById(idUsuario);
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Generar ID único si no tiene uno
        if (usuario.getIdUsuario() == null || usuario.getIdUsuario().isEmpty()) {
            usuario.setIdUsuario(UUID.randomUUID().toString());
        }
        
        return repo.save(usuario);
    }

    @Override
    public Usuario update(Usuario usuario) {
        return repo.update(usuario);
    }

    @Override
    public boolean deleteById(String idUsuario) {
        return repo.deleteById(idUsuario);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repo.findByEmail(email) != null;
    }

    @Override
    public boolean existsByCedula(String cedula) {
        return repo.findByCedula(cedula) != null;
    }

    @Override
    public Usuario findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public boolean validarCredenciales(String email, String clave) {
        Usuario usuario = repo.findByEmail(email);
        return usuario != null && usuario.getClave().equals(clave);
    }
}