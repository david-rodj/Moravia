package com.moravia.demo.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estado;
    private float total;

    @OneToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @OneToMany(mappedBy = "cuenta")
    private List<ItemCuenta> items = new ArrayList<>();

    @OneToMany(mappedBy = "cuenta")
    private List<Pago> pagos = new ArrayList<>();

    public Cuenta(String estado, float total, Reserva reserva) {
        this.estado = estado;
        this.total = total;
        this.reserva = reserva;
    }
}
