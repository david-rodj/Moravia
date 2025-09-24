package com.moravia.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.moravia.demo.entities.Usuario;
import com.moravia.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar perfil de usuario por ID
    // Alternativa: Cambiar el parámetro a String y convertir manualmente
    @GetMapping("/{idUsuario}")
    public String perfilUsuario(@PathVariable String idUsuario, Model model,
            RedirectAttributes redirectAttributes, HttpSession session) {
        // Verificar autenticación
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        if (authenticated == null || !authenticated || usuarioSesion == null) {
            return "redirect:/login?error=Debe+iniciar+sesion+para+acceder";
        }

        try {

            System.out.println("idUsuario recibido: " + idUsuario);
            // Convertir String a Long
            Long id = Long.parseLong(idUsuario);

            System.out.println("ID de usuario solicitado: " + id);

            // Verificar que el usuario solo pueda ver su propio perfil
            if (!usuarioSesion.getIdUsuario().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "No tiene permisos para ver este perfil");
                return "redirect:/usuarios/" + usuarioSesion.getIdUsuario();
            }

            Usuario usuario = usuarioService.findById(id);
            if (usuario != null) {
                model.addAttribute("usuario", usuario);
                return "perfil_usuario";
            } else {
                System.out.println("error Usuario no encontrado");
                return "redirect:/";
            }
        } catch (NumberFormatException e) {
            System.out.println("error ID de usuario inválido");
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("error Error al cargar el usuario");
            return "redirect:/";
        }
    }

    // Aplicar el mismo cambio a los otros métodos
    @PostMapping("/{idUsuario}/actualizar")
    public String actualizarUsuario(@PathVariable String idUsuario,
            @Valid @ModelAttribute Usuario usuario,
            BindingResult result,
            @RequestParam(value = "nuevaClave", required = false) String nuevaClave,
            @RequestParam(value = "confirmarClave", required = false) String confirmarClave,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        Long id = null;

        try {
            id = Long.parseLong(idUsuario);
            // usar 'id' para operaciones que requieren Long
        } catch (NumberFormatException e) {
            // manejar error de conversión
        }

        // Verificar autenticación
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        if (authenticated == null || !authenticated || usuarioSesion == null) {
            return "redirect:/login?error=Debe+iniciar+sesion+para+acceder";
        }

        // Verificar que el usuario solo pueda actualizar su propio perfil
        if (!usuarioSesion.getIdUsuario().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "No tiene permisos para modificar este perfil");
            return "redirect:/usuarios/" + usuarioSesion.getIdUsuario();
        }

        // DEBUG: Imprimir los datos recibidos
        System.out.println("=== DATOS RECIBIDOS ===");
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Apellido: " + usuario.getApellido());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Cédula: " + usuario.getCedula());
        System.out.println("Teléfono: " + usuario.getTelefono());
        System.out.println("Foto: " + usuario.getFotoPerfil());
        System.out.println("Nueva clave: " + (nuevaClave != null ? "[PROPORCIONADA]" : "[NO PROPORCIONADA]"));

        // Validar contraseñas si se proporcionaron
        if (nuevaClave != null && !nuevaClave.isEmpty()) {
            if (confirmarClave == null || !nuevaClave.equals(confirmarClave)) {
                result.rejectValue("clave", "error.usuario", "Las contraseñas no coinciden");
            } else if (nuevaClave.length() < 6) {
                result.rejectValue("clave", "error.usuario", "La contraseña debe tener al menos 6 caracteres");
            } else {
                usuario.setClave(nuevaClave);
                System.out.println("Nueva contraseña establecida");
            }
        } else {
            // Mantener la contraseña actual si no se proporciona una nueva
            Usuario usuarioActual = usuarioService.findById(id);
            if (usuarioActual != null) {
                usuario.setClave(usuarioActual.getClave());
                System.out.println("Contraseña actual mantenida");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "perfil_usuario";
        }

        try {
            usuario.setIdUsuario(id); // Asegurar que el ID se mantenga

            System.out.println("=== DATOS A ACTUALIZAR ===");
            System.out.println("ID: " + usuario.getIdUsuario());
            System.out.println("Nombre: " + usuario.getNombre());
            System.out.println("Apellido: " + usuario.getApellido());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Cédula: " + usuario.getCedula());
            System.out.println("Teléfono: " + usuario.getTelefono());
            System.out.println("Foto: " + usuario.getFotoPerfil());

            usuarioService.update(usuario);
            System.out.println("Usuario actualizado exitosamente");

            // Actualizar la sesión con los nuevos datos del usuario
            session.setAttribute("usuario", usuario);

            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            return "redirect:/usuarios/" + id;
        } catch (Exception e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
            model.addAttribute("usuario", usuario);
            return "perfil_usuario";
        }

    }

    @GetMapping("/{idUsuario}/confirmar-eliminar")
    public String confirmarEliminar(@PathVariable Long idUsuario, Model model,
            RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "confirmar_eliminar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/";
        }
    }

    @PostMapping("/{idUsuario}/eliminar")
    public String eliminarUsuario(@PathVariable Long idUsuario, RedirectAttributes ra) {
        try {
            usuarioService.deleteById(idUsuario); // ajusta según tu repo
            ra.addFlashAttribute("exito", "Usuario eliminado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se pudo eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/logout";
    }
}