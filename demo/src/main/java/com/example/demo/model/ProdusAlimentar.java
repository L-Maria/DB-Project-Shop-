package com.example.demo.model;

import jakarta.persistence.*; // Pentru @Entity, @Id, etc. (Spring Boot 3)
import lombok.Data; // Pentru @Data
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore; // Pentru @JsonIgnore
import java.util.List;

@Entity
@Data
public class ProdusAlimentar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String denumire;
    private LocalDate dataProducere;
    private LocalDate dataExpirare;
        
    @OneToMany(mappedBy = "produs", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Achizitie> achizitii;

    @OneToMany(mappedBy = "produs", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Furnizare> furnizari;
}
