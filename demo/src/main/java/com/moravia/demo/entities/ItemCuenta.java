package com.moravia.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ItemCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;
    private float precioItem;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    public ItemCuenta(int cantidad, float precioItem, Cuenta cuenta, Servicio servicio) {
        this.cantidad = cantidad;
        this.precioItem = precioItem;
        this.cuenta = cuenta;
        this.servicio = servicio;
    }
}
