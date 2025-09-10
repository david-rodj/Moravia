package com.moravia.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Room {

    @Id
    private String id;                 // p.ej. ROOM001
    
    private String numeroHabitacion;   // p.ej. "101"
    
    private boolean disponible;         // true/false

    @ManyToOne
    private Habitacion tipo; // Relación con la entidad Habitación 

    public Room(String id, String numeroHabitacion, boolean disponible) {
        this.id = id;
        this.numeroHabitacion = numeroHabitacion;
        this.disponible = disponible;
    }
}
