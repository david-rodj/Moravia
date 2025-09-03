package com.moravia.demo.service;

import com.moravia.demo.entities.Room;
import java.util.List;

public interface RoomService {
    public List<Room> findAll();
    
    public Room findById(String id);
    
    public void add(Room room);

    public void update(Room room);
    
    public void deleteById(String id);
}
