package com.moravia.demo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moravia.demo.entities.Usuario;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UsuarioRepository {
    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    private final Path dataPath = Path.of("data/usuarios.json");

    public List<Usuario> findAll() {
        try {
            return new ArrayList<>(readStore().usuarios);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Usuario findById(String idUsuario) {
        List<Usuario> usuarios = findAll();
        return usuarios.stream()
                .filter(usuario -> usuario.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElse(null);
    }

    public Usuario findByEmail(String email) {
        List<Usuario> usuarios = findAll();
        return usuarios.stream()
                .filter(usuario -> usuario.getCorreo().equals(email))
                .findFirst()
                .orElse(null);
    }

    public Usuario findByCedula(String cedula) {
        List<Usuario> usuarios = findAll();
        return usuarios.stream()
                .filter(usuario -> usuario.getCedula().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    public Usuario save(Usuario usuario) {
        try {
            UsuarioStore store = readStore();
            
            // Verificar si el usuario ya existe por email o cédula
            boolean emailExiste = store.usuarios.stream()
                    .anyMatch(u -> u.getCorreo().equals(usuario.getCorreo()));
            boolean cedulaExiste = store.usuarios.stream()
                    .anyMatch(u -> u.getCedula().equals(usuario.getCedula()));
            
            if (emailExiste) {
                throw new RuntimeException("El email ya está registrado");
            }
            if (cedulaExiste) {
                throw new RuntimeException("La cédula ya está registrada");
            }
            
            store.usuarios.add(usuario);
            writeStore(store);
            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage());
        }
    }

    public Usuario update(Usuario usuario) {
        try {
            UsuarioStore store = readStore();
            
            // Encontrar el índice del usuario a actualizar
            int index = -1;
            for (int i = 0; i < store.usuarios.size(); i++) {
                if (store.usuarios.get(i).getIdUsuario().equals(usuario.getIdUsuario())) {
                    index = i;
                    break;
                }
            }
            
            if (index == -1) {
                throw new RuntimeException("Usuario no encontrado");
            }
            
            // Verificar email único (excluyendo el usuario actual)
            boolean emailExiste = store.usuarios.stream()
                    .anyMatch(u -> u.getCorreo().equals(usuario.getCorreo()) 
                            && !u.getIdUsuario().equals(usuario.getIdUsuario()));
            
            // Verificar cédula única (excluyendo el usuario actual)
            boolean cedulaExiste = store.usuarios.stream()
                    .anyMatch(u -> u.getCedula().equals(usuario.getCedula()) 
                            && !u.getIdUsuario().equals(usuario.getIdUsuario()));
            
            if (emailExiste) {
                throw new RuntimeException("El email ya está registrado por otro usuario");
            }
            if (cedulaExiste) {
                throw new RuntimeException("La cédula ya está registrada por otro usuario");
            }
            
            // Actualizar el usuario
            store.usuarios.set(index, usuario);
            writeStore(store);
            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    public boolean deleteById(String idUsuario) {
        try {
            UsuarioStore store = readStore();
            boolean removed = store.usuarios.removeIf(usuario -> usuario.getIdUsuario().equals(idUsuario));
            
            if (removed) {
                writeStore(store);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private UsuarioStore readStore() {
        try {
            if (!Files.exists(dataPath)) {
                // Crear directorio si no existe
                Files.createDirectories(dataPath.getParent());
                
                // Intentar inicializar desde resources
                try (InputStream is = getClass().getResourceAsStream("/usuarios.json")) {
                    if (is != null) {
                        UsuarioStore seed = mapper.readValue(is, UsuarioStore.class);
                        writeStore(seed);
                        return seed;
                    }
                }
            }
            return mapper.readValue(dataPath.toFile(), UsuarioStore.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new UsuarioStore();
        }
    }

    private void writeStore(UsuarioStore store) {
        try {
            // Crear directorio si no existe
            Files.createDirectories(dataPath.getParent());
            mapper.writeValue(dataPath.toFile(), store);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class UsuarioStore {
        public List<Usuario> usuarios = new ArrayList<>();
    }
}