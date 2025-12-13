package com.example.demo.controller;

import com.example.demo.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.demo.model.Producator;
import com.example.demo.repository.ProducatorRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/producatori")
@CrossOrigin(origins = "*")
public class ProducatorController {

    @Autowired
    private ProducatorRepository producatorRepo;

    // // 1. Inregistrare Producator (Sign Up)
    // @PostMapping("/signup")
    // public Producator adaugaProducator(@RequestBody Producator producator) {
    //     return producatorRepo.save(producator);
    // }

    // // 2. Login Producator
    // // Atentie: In ProducatorRepository trebuie sa ai metoda findByDenumireAndPassword
    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody Producator loginRequest) {
    //     Optional<Producator> producator = producatorRepo.findByDenumireAndPassword(
    //         loginRequest.getDenumire(), loginRequest.getPassword()
    //     );

    //     if (producator.isPresent()) {
    //         return ResponseEntity.ok(producator.get());
    //     } else {
    //         return ResponseEntity.status(401).body("Denumire sau parola gresita");
    //     }
    // }
    // 1. Inregistrare
    @PostMapping("/signup")
    public Producator adaugaProducator(@RequestBody Producator producator) {
        // Hashing parola
        String hash = SecurityUtil.hashPassword(producator.getPassword());
        producator.setPassword(hash);
        
        return producatorRepo.save(producator);
    }

    // 2. Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Producator loginRequest) {
        // Hash parola primita
        String hash = SecurityUtil.hashPassword(loginRequest.getPassword());

        Optional<Producator> producator = producatorRepo.findByDenumireAndPassword(
            loginRequest.getDenumire(), 
            hash // Cautam hash-ul
        );

        if (producator.isPresent()) {
            return ResponseEntity.ok(producator.get());
        } else {
            return ResponseEntity.status(401).body("Denumire sau parola gresita");
        }
    }
 
    // 3. Vizualizare toti producatorii
    @GetMapping
    public List<Producator> getAllProducatori() {
        return producatorRepo.findAll();
    }

    // 4. Modificare date producator
    @PutMapping("/{id}")
    public Producator modificaProducator(@PathVariable Long id, @RequestBody Producator producatorNou) {
        return producatorRepo.findById(id)
            .map(prod -> {
                prod.setDenumire(producatorNou.getDenumire());
                prod.setAdresa(producatorNou.getAdresa());
                prod.setTaraOrigine(producatorNou.getTaraOrigine());
                // Nu actualizam parola aici decat daca e nevoie
                return producatorRepo.save(prod);
            })
            .orElseGet(() -> producatorRepo.save(producatorNou));
    }


    // 5. Stergere producator
    @DeleteMapping("/{id}")
    public void stergeProducator(@PathVariable Long id) {
        producatorRepo.deleteById(id);
    }
}