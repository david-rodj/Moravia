package com.moravia.demo.service;

import com.moravia.demo.entities.Room;
import java.util.List;

public interface RoomService {
    List<Room> findAll();
    Room findById(String id);
    Room save(Room room);
    Room delete(String id);
}
