package com.moravia.demo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.moravia.demo.entities.Habitacion;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
public class HabitacionRepository {

    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // Ruta editable fuera de resources
    private final Path dataPath = Path.of("data/habitaciones.json");

    // Wrapper para mantener la estructura { "habitaciones": [ ... ] }
    public static class HabitacionStore {
        public List<Habitacion> habitaciones = new ArrayList<>();
    }

    // ========== Helpers ==========

    private HabitacionStore readStore() {
        try {
            if (!Files.exists(dataPath)) {
                // Si no existe el archivo, inicializa desde resources (/habitaciones.json)
                try (InputStream is = getClass().getResourceAsStream("/habitaciones.json")) {
                    HabitacionStore seed = mapper.readValue(is, HabitacionStore.class);
                    writeStore(seed); // guarda copia inicial editable
                    return seed;
                }
            }
            return mapper.readValue(dataPath.toFile(), HabitacionStore.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new HabitacionStore();
        }
    }

    private void writeStore(HabitacionStore store) {
        try {
            Files.createDirectories(dataPath.getParent());
            mapper.writeValue(dataPath.toFile(), store);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== CRUD ==========

    public List<Habitacion> findAll() {
        return new ArrayList<>(readStore().habitaciones);
    }

    public Habitacion findById(String idHabitacion) {
        return readStore().habitaciones.stream()
                .filter(h -> Objects.equals(h.getIdHabitacion(), idHabitacion))
                .findFirst()
                .orElse(null);
    }

    public Habitacion save(Habitacion habitacion) {
        HabitacionStore store = readStore();

        if (habitacion.getIdHabitacion() == null || habitacion.getIdHabitacion().isBlank()) {
            int newId = store.habitaciones.size() + 1;
            habitacion.setIdHabitacion(String.valueOf(newId));
        }

        boolean updated = false;
        for (int i = 0; i < store.habitaciones.size(); i++) {
            if (Objects.equals(store.habitaciones.get(i).getIdHabitacion(), habitacion.getIdHabitacion())) {
                store.habitaciones.set(i, habitacion);
                updated = true;
                break;
            }
        }
        if (!updated) {
            store.habitaciones.add(habitacion);
        }

        writeStore(store);
        return habitacion;
    }

    public Habitacion delete(String idHabitacion) {
        HabitacionStore store = readStore();
        Habitacion removed = null;

        Iterator<Habitacion> it = store.habitaciones.iterator();
        while (it.hasNext()) {
            Habitacion h = it.next();
            if (Objects.equals(h.getIdHabitacion(), idHabitacion)) {
                removed = h;
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
