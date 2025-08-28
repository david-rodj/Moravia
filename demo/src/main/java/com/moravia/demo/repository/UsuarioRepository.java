package com.moravia.demo.repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moravia.demo.entities.Usuario;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
        for (Usuario usuario : usuarios) {
            if (usuario.getIdUsuario().equals(idUsuario)) {
                return usuario;
            }
        }
        return null;
    }

    private UsuarioStore readStore() {
        try {
            if (!Files.exists(dataPath)) {
                // si no existe el archivo, intenta inicializar desde resources
                try (InputStream is = getClass().getResourceAsStream("/usuarios.json")) {
                    UsuarioStore seed = mapper.readValue(is, UsuarioStore.class);
                    writeStore(seed); // guarda copia inicial
                    return seed;
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
            mapper.writeValue(dataPath.toFile(), store);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class UsuarioStore {
        public List<Usuario> usuarios = new ArrayList<>();
    }
}
