package com.moravia.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.moravia.demo.entities.Usuario;
import com.moravia.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AuthController {

  @Autowired
  private UsuarioService usuarioService;

  @GetMapping("/login")
  public String login() {
    return "login"; // resolve a templates/login.html
  }

  @PostMapping("/login")
  public String login(@RequestParam String correo, @RequestParam String clave, Model model, RedirectAttributes redirectAttributes) {
    try {
      Usuario usuario = usuarioService.findByEmail(correo);
      if (usuario != null) {
        model.addAttribute("usuario", usuario);
        if (usuario.getClave().equals(clave)) {
          return "index"; // Redirect to user profile page
        } else {
          model.addAttribute("error", "Clave incorrecta");
          return "login"; // Back to login page with error
        }
      } else {
        // redirectAttributes is not defined, so just return redirect with error param
        return "redirect:/?error=Usuario+no+encontrado";
      }
    } catch (Exception e) {
      return "redirect:/?error=Error+al+cargar+el+usuario";
    }
    // This line is unreachable, but kept for structure
    // return "redirect:/"; // Redirigir a la página principal después del login
  }
}
