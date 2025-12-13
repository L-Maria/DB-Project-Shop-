package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Producator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denumire;
    private String taraOrigine;
    private String adresa;
    
    // Parola cerută în cerință
    private String password;

    // Legătura cu entitatea Furnizare
    // Un producător poate avea mai multe "furnizări" (livrări de produse)
    @OneToMany(mappedBy = "producator", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Furnizare> furnizari;
}
