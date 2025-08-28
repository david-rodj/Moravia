package com.moravia.demo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.moravia.demo.entities.Servicio;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
public class ServicioRepository {

    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // Ruta donde estará el archivo (fuera de resources, editable)
    private final Path dataPath = Path.of("data/servicios.json");

    // Clase wrapper para mantener la estructura { "servicios": [ ... ] }
    public static class ServicioStore {
        public List<Servicio> servicios = new ArrayList<>();
    }

    // ========== Helpers ==========

    private ServicioStore readStore() {
        try {
            if (!Files.exists(dataPath)) {
                // si no existe el archivo, intenta inicializar desde resources
                try (InputStream is = getClass().getResourceAsStream("/servicios.json")) {
                    ServicioStore seed = mapper.readValue(is, ServicioStore.class);
                    writeStore(seed); // guarda copia inicial
                    return seed;
                }
            }
            return mapper.readValue(dataPath.toFile(), ServicioStore.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ServicioStore();
        }
    }

    private void writeStore(ServicioStore store) {
        try {
            Files.createDirectories(dataPath.getParent());
            mapper.writeValue(dataPath.toFile(), store);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== CRUD ==========

    public List<Servicio> findAll() {
        return new ArrayList<>(readStore().servicios);
    }

    public Servicio findById(String idServicio) {
        return readStore().servicios.stream()
                .filter(s -> Objects.equals(s.getIdServicio(), idServicio))
                .findFirst()
                .orElse(null);
    }

    // /servicios/nuevo  -> FORM para CREAR
    @GetMapping("/nuevo")
    public String nuevoServicioForm(Model model) {
        model.addAttribute("servicio", new Servicio()); // id vacío => insert
        model.addAttribute("modo", "crear");
        return "form_servicio"; // tu plantilla
    }

    

    public Servicio save(Servicio servicio) {
        ServicioStore store = readStore();

        if (servicio.getIdServicio() == null || servicio.getIdServicio().isBlank()) {
            servicio.setIdServicio(UUID.randomUUID().toString());
        }

        // buscar si existe
        boolean updated = false;
        for (int i = 0; i < store.servicios.size(); i++) {
            if (Objects.equals(store.servicios.get(i).getIdServicio(), servicio.getIdServicio())) {
                store.servicios.set(i, servicio);
                updated = true;
                break;
            }
        }
        if (!updated) {
            store.servicios.add(servicio);
        }

        writeStore(store);
        return servicio;
    }

    public Servicio delete(String idServicio) {
        ServicioStore store = readStore();
        Servicio removed = null;

        Iterator<Servicio> it = store.servicios.iterator();
        while (it.hasNext()) {
            Servicio s = it.next();
            if (Objects.equals(s.getIdServicio(), idServicio)) {
                removed = s;
                it.remove();
                break;
            }
        }

        if (removed != null) {
            writeStore(store);
        }
        return removed;
    }


}
