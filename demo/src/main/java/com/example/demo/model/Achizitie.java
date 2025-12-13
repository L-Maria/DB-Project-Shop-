package com.example.demo.model;

import jakarta.persistence.*; // Pentru @Entity, @Id, etc. (Spring Boot 3)
import lombok.Data; // Pentru @Data
import java.time.LocalDate;

@Entity
@Data
public class Achizitie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate dataAchizitie;
    private int cantitate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "produs_id")
    private ProdusAlimentar produs;
}
