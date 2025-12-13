package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Furnizare;
import com.example.demo.model.Producator;
import com.example.demo.model.ProdusAlimentar;
import com.example.demo.repository.FurnizareRepository;
import com.example.demo.repository.ProducatorRepository;
import com.example.demo.repository.ProdusRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/furnizari")
@CrossOrigin(origins = "*")
public class FurnizareController {

    @Autowired private FurnizareRepository furnizareRepo;
    @Autowired private ProducatorRepository producatorRepo;
    @Autowired private ProdusRepository produsRepo;

    // 1. Vizualizare toate furnizarile (Tabela de legatura)
    @GetMapping
    public List<Furnizare> getAllFurnizari() {
        return furnizareRepo.findAll();
    }

    // 2. Adaugare Furnizare (Aprovizionare)
    @PostMapping("/adauga")
    public String adaugaFurnizare(@RequestBody RequestFurnizare request) {
        Optional<Furnizare> furnizareExistenta = furnizareRepo.findByProducatorIdAndProdusId(
            request.producatorId, 
            request.produsId
        );

        if (furnizareExistenta.isPresent()) {
            // --- CAZUL 1: PRODUSUL EXISTA -> ACTUALIZAM STOCUL ---
            Furnizare f = furnizareExistenta.get();
            
            // a) Cumulam stocul (Vechi + Nou)
            int stocNou = f.getStoc() + request.stoc;
            f.setStoc(stocNou);
            
            // b) Actualizam pretul (poate s-a scumpit intre timp)
            f.setPretAchizitie(request.pret);
            
            // c) Actualizam data (Compromisul: consideram tot stocul ca fiind "proaspat")
            f.setDataFurnizarii(LocalDate.now());

            furnizareRepo.save(f);
            return "Stoc actualizat! Total acum: " + stocNou + " bucati.";

        } else {
            // --- CAZUL 2: PRODUS NOU -> CREAM DE LA ZERO ---
            Producator producator = producatorRepo.findById(request.producatorId).orElseThrow();
            ProdusAlimentar produs = produsRepo.findById(request.produsId).orElseThrow();

            Furnizare furnizare = new Furnizare();
            furnizare.setProducator(producator);
            furnizare.setProdus(produs);
            furnizare.setPretAchizitie(request.pret);
            furnizare.setStoc(request.stoc);
            furnizare.setDataFurnizarii(LocalDate.now());

            furnizareRepo.save(furnizare);
            return "Produs nou adaugat in oferta!";
        }
    }

    // 3. Stergere o furnizare (daca a fost gresita)
    @DeleteMapping("/{id}")
    public void stergeFurnizare(@PathVariable Long id) {
        furnizareRepo.deleteById(id);
    }

    // Clasa interna (DTO) pentru a primi datele simplificat din Frontend/Postman
    static class RequestFurnizare {
        public Long producatorId;
        public Long produsId;
        public Double pret;
        public int stoc;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Furnizare> getFurnizareById(@PathVariable Long id) {
        return furnizareRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Modificare pret furnizare
    @PutMapping("/{id}")
    public Furnizare modificaFurnizare(@PathVariable Long id, @RequestBody RequestFurnizare req) {
        return furnizareRepo.findById(id).map(f -> {
            if(req.pret != null) f.setPretAchizitie(req.pret);
            return furnizareRepo.save(f);
        }).orElseThrow(() -> new RuntimeException("Furnizarea nu exista"));
    }
}