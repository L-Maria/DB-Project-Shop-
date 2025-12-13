package com.example.demo.model;

import jakarta.persistence.*; // Pentru @Entity, @Id, etc. (Spring Boot 3)
import lombok.Data; // Pentru @Data
import lombok.NoArgsConstructor; // Pentru @NoArgsConstructor
import com.fasterxml.jackson.annotation.JsonIgnore; // Pentru @JsonIgnore
import java.util.List;

@Entity
@Data // Lombok pentru Getters/Setters
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nume;
    private String prenume;
    private String adresa;
    private String password; // Simplu, text (pentru proiect scoala)
    
    // Un client poate avea mai multe achizitii
    // JsonIgnore oprește bucla infinită în JSON
    // @OneToMany(mappedBy = "client")
    // @JsonIgnore 
    // private List<Achizitie> achizitii;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore 
    private List<Achizitie> achizitii;
}