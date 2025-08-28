package com.moravia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private String id;                 // p.ej. ROOM001
    private String numeroHabitacion;   // p.ej. "101"
    private String tipo;               // "Suite" | "Doble" | "Sencilla"
    private boolean disponible;        // true/false
}
