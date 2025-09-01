package com.moravia.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.moravia.demo.entities.Usuario;
import com.moravia.demo.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar lista de usuarios
    /*
     * @GetMapping("/lista")
     * public String listaUsuarios(Model model) {
     * List<Usuario> usuarios = usuarioService.findAll();
     * model.addAttribute("usuarios", usuarios);
     * return "lista_usuarios";
     * }
     */

    // Mostrar perfil de usuario por ID
    @GetMapping("/{idUsuario}")
    public String perfilUsuario(@PathVariable String idUsuario, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.findById(idUsuario);
            if (usuario != null) {
                model.addAttribute("usuario", usuario);
                return "perfil_usuario";
            } else {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el usuario");
            return "redirect:/";
        }
    }

    /*
     * // Mostrar formulario de edición
     * 
     * @GetMapping("/{idUsuario}/editar")
     * public String editarUsuario(@PathVariable String idUsuario, Model model,
     * RedirectAttributes redirectAttributes) {
     * try {
     * Usuario usuario = usuarioService.findById(idUsuario);
     * if (usuario != null) {
     * model.addAttribute("usuario", usuario);
     * return "editar_usuario";
     * } else {
     * redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
     * return "redirect:/";
     * }
     * } catch (Exception e) {
     * redirectAttributes.addFlashAttribute("error", "Error al cargar el usuario");
     * return "redirect:/";
     * }
     * }
     */
    // Procesar actualización de usuario
    // Procesar actualización de usuario
    @PostMapping("/{idUsuario}/actualizar")
    public String actualizarUsuario(@PathVariable String idUsuario,
            @Valid @ModelAttribute Usuario usuario,
            BindingResult result,
            @RequestParam(value = "nuevaClave", required = false) String nuevaClave,
            @RequestParam(value = "confirmarClave", required = false) String confirmarClave,
            Model model,
            RedirectAttributes redirectAttributes) {

        // DEBUG: Imprimir los datos recibidos
        System.out.println("=== DATOS RECIBIDOS ===");
        System.out.println("ID: " + idUsuario);
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Apellido: " + usuario.getApellido());
        System.out.println("Email: " + usuario.getCorreo());
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
            Usuario usuarioActual = usuarioService.findById(idUsuario);
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
            usuario.setIdUsuario(idUsuario); // Asegurar que el ID se mantenga

            System.out.println("=== DATOS A ACTUALIZAR ===");
            System.out.println("ID: " + usuario.getIdUsuario());
            System.out.println("Nombre: " + usuario.getNombre());
            System.out.println("Apellido: " + usuario.getApellido());
            System.out.println("Email: " + usuario.getCorreo());
            System.out.println("Cédula: " + usuario.getCedula());
            System.out.println("Teléfono: " + usuario.getTelefono());
            System.out.println("Foto: " + usuario.getFotoPerfil());

            Usuario usuarioActualizado = usuarioService.update(usuario);
            System.out.println("Usuario actualizado exitosamente");

            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            return "redirect:/usuarios/" + idUsuario;
        } catch (Exception e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
            model.addAttribute("usuario", usuario);
            return "perfil_usuario";
        }
    }

    // Mostrar formulario de nuevo usuario
    /*
     * @GetMapping("/nuevo")
     * public String nuevoUsuario(Model model) {
     * model.addAttribute("usuario", new Usuario());
     * return "crear_usuario";
     * }
     * 
     * // Crear nuevo usuario
     * 
     * @PostMapping("/crear")
     * public String crearUsuario(@Valid @ModelAttribute Usuario usuario,
     * BindingResult result,
     * 
     * @RequestParam("confirmarClave") String confirmarClave,
     * Model model,
     * RedirectAttributes redirectAttributes) {
     * 
     * // Validar contraseñas
     * if (!usuario.getClave().equals(confirmarClave)) {
     * result.rejectValue("clave", "error.usuario", "Las contraseñas no coinciden");
     * }
     * 
     * // Validar email único
     * if (usuarioService.existsByEmail(usuario.getCorreo())) {
     * result.rejectValue("correo", "error.usuario", "El email ya está registrado");
     * }
     * 
     * // Validar cédula única
     * if (usuarioService.existsByCedula(usuario.getCedula())) {
     * result.rejectValue("cedula", "error.usuario",
     * "La cédula ya está registrada");
     * }
     * 
     * if (result.hasErrors()) {
     * model.addAttribute("usuario", usuario);
     * return "crear_usuario";
     * }
     * 
     * try {
     * Usuario nuevoUsuario = usuarioService.save(usuario);
     * redirectAttributes.addFlashAttribute("mensaje",
     * "Usuario creado exitosamente");
     * return "redirect:/usuarios/" + nuevoUsuario.getIdUsuario();
     * } catch (Exception e) {
     * redirectAttributes.addFlashAttribute("error", "Error al crear el usuario: " +
     * e.getMessage());
     * model.addAttribute("usuario", usuario);
     * return "crear_usuario";
     * }
     * }
     * 
     * // Eliminar usuario
     * 
     * @PostMapping("/{idUsuario}/eliminar")
     * public String eliminarUsuario(@PathVariable String idUsuario,
     * RedirectAttributes redirectAttributes) {
     * try {
     * boolean eliminado = usuarioService.deleteById(idUsuario);
     * if (eliminado) {
     * redirectAttributes.addFlashAttribute("mensaje",
     * "Usuario eliminado exitosamente");
     * } else {
     * redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
     * }
     * } catch (Exception e) {
     * redirectAttributes.addFlashAttribute("error",
     * "Error al eliminar el usuario: " + e.getMessage());
     * }
     * 
     * return "redirect:/usuarios/lista";
     * }
     * 
     * // Método para confirmar eliminación
     * 
     */
    @GetMapping("/{idUsuario}/confirmar-eliminar")
    public String confirmarEliminar(@PathVariable String idUsuario, Model model,
            RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "confirmar_eliminar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/usuarios/lista";
        }
    }

    @PostMapping("/{idUsuario}/eliminar")
    public String eliminarUsuario(@PathVariable String idUsuario, RedirectAttributes ra) {
        try {
            usuarioService.deleteById(idUsuario); // ajusta según tu repo
            ra.addFlashAttribute("exito", "Usuario eliminado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se pudo eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/";
    }

}