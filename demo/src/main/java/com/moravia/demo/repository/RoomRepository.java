package com.moravia.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moravia.demo.entities.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
