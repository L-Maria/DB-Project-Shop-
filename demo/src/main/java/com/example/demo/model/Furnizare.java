package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate; // Nu uita importul pentru dată!

@Entity
@Data
@NoArgsConstructor
public class Furnizare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Data la care s-a făcut furnizarea (opțional, dar util)
    private LocalDate dataFurnizarii;
    
    // Prețul cu care producătorul vinde acest produs (opțional)
    private Double pretAchizitie;
    private int stoc;

    // RELAȚIILE (Cheile străine)

    // Legătura cu Producatorul
    @ManyToOne
    @JoinColumn(name = "producator_id")
    private Producator producator;

    // Legătura cu Produsul
    @ManyToOne
    @JoinColumn(name = "produs_id")
    private ProdusAlimentar produs;
}