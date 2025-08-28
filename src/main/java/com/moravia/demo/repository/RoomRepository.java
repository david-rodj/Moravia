package com.moravia.demo.repository;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.moravia.demo.entities.Room;

@Repository
public class RoomRepository {

    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // Configurable (por defecto ./data). En application.properties: app.data.dir=./data
    @Value("${app.data.dir:./data}")
    private String dataDir;

    // Estructura { "rooms": [ ... ] }
    public static class RoomStore {
        public List<Room> rooms = new ArrayList<>();
    }

    private Path dataPath() {
        return Path.of(dataDir, "room.json");
    }

    // ===== Helpers =====
    private RoomStore readStore() {
        Path path = dataPath();
        try {
            if (!Files.exists(path)) {
                try (InputStream is = getClass().getResourceAsStream("/room.json")) {
                    RoomStore seed;
                    if (is != null) {
                        seed = mapper.readValue(is, RoomStore.class);
                    } else {
                        seed = new RoomStore();
                    }
                    writeStore(seed);
                    return seed;
                }
            }
            return mapper.readValue(path.toFile(), RoomStore.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new RoomStore();
        }
    }

    private void writeStore(RoomStore store) {
        Path path = dataPath();
        try {
            Files.createDirectories(path.getParent());
            Path tmp = path.resolveSibling(path.getFileName().toString() + ".tmp");
            mapper.writeValue(tmp.toFile(), store);
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String nextId(List<Room> existentes) {
        Pattern p = Pattern.compile("^ROOM(\\d{3,})$");
        int max = 0;
        for (Room r : existentes) {
            String id = r.getId();
            if (id == null) continue;
            Matcher m = p.matcher(id);
            if (m.matches()) {
                try {
                    int n = Integer.parseInt(m.group(1));
                    if (n > max) max = n;
                } catch (NumberFormatException ignored) {}
            }
        }
        if (max > 0) return String.format("ROOM%03d", max + 1);
        return "ROOM001";
    }

    // ===== CRUD =====
    public List<Room> findAll() {
        return new ArrayList<>(readStore().rooms);
    }

    public Room findById(String id) {
        return readStore().rooms.stream()
                .filter(r -> Objects.equals(r.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public Room save(Room room) {
        RoomStore store = readStore();

        if (room.getId() == null || room.getId().isBlank()) {
            room.setId(nextId(store.rooms));
        }

        boolean updated = false;
        for (int i = 0; i < store.rooms.size(); i++) {
            if (Objects.equals(store.rooms.get(i).getId(), room.getId())) {
                store.rooms.set(i, room);
                updated = true;
                break;
            }
        }
        if (!updated) {
            store.rooms.add(room);
        }

        writeStore(store);
        return room;
    }

    public Room delete(String id) {
        RoomStore store = readStore();
        Room removed = null;

        Iterator<Room> it = store.rooms.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (Objects.equals(r.getId(), id)) {
                removed = r;
                it.remove();
                break;
            }
        }
        if (removed != null) writeStore(store);
        return removed;
    }
}
