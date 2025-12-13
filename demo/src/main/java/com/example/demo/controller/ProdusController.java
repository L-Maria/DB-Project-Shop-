package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.model.ProdusAlimentar;
import com.example.demo.repository.ProdusRepository;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produse")
@CrossOrigin(origins = "*")
public class ProdusController {

    @Autowired
    private ProdusRepository produsRepo;

    // 1. Vizualizare toate produsele
    @GetMapping
    public List<ProdusAlimentar> getAllProduse() {
        return produsRepo.findAll();
    }

    @PostMapping
        public ProdusAlimentar adaugaProdus(@RequestBody ProdusAlimentar produsNou) {
            // 1. Verificam daca produsul exista deja in catalog (dupa nume)
            Optional<ProdusAlimentar> produsExistent = produsRepo.findByDenumire(produsNou.getDenumire());

            if (produsExistent.isPresent()) {
                // Daca exista, il returnam pe cel vechi (NU cream altul)
                // Putem actualiza data expirarii daca vrem
                ProdusAlimentar p = produsExistent.get();
                p.setDataProducere(produsNou.getDataProducere()); 
                p.setDataExpirare(produsNou.getDataExpirare());
                return produsRepo.save(p);
            } else {
                // Daca nu exista, il cream
                return produsRepo.save(produsNou);
            }
        }

    // 3. Modificare produs (Update)
    @PutMapping("/{id}")
    public ProdusAlimentar modificaProdus(@PathVariable Long id, @RequestBody ProdusAlimentar produsNou) {
        return produsRepo.findById(id)
            .map(produs -> {
                produs.setDenumire(produsNou.getDenumire());
                produs.setDataProducere(produsNou.getDataProducere());
                produs.setDataExpirare(produsNou.getDataExpirare());
                return produsRepo.save(produs);
            })
            .orElseGet(() -> {
                return produsRepo.save(produsNou);
            });
    }

    // 4. Stergere produs
    @DeleteMapping("/{id}")
    public void stergeProdus(@PathVariable Long id) {
        produsRepo.deleteById(id);
    }
}