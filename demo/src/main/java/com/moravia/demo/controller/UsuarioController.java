/*package com.moravia.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.moravia.demo.entities.Usuario;
import com.moravia.demo.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuarios/{idUsuario}")
    public String perfilUsuario(@PathVariable String idUsuario, Model model) {
        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "perfil_usuario";
        } else {
            return "redirect:/usuarios/lista";
        }
    }

    @GetMapping("/usuarios/lista")
    public String listaUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "lista_usuarios";
    }
}
*/