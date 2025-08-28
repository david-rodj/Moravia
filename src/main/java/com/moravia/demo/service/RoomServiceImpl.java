package com.moravia.demo.service;

import com.moravia.demo.entities.Room;
import com.moravia.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository repo;

    @Override
    public List<Room> findAll() {
        return repo.findAll();
    }

    @Override
    public Room findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Room save(Room room) {
        return repo.save(room);
    }

    @Override
    public Room delete(String id) {
        return repo.delete(id);
    }
}
