package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.Furnizare;
import com.example.demo.repository.FurnizareRepository;

import com.example.demo.model.Achizitie;
import com.example.demo.model.Client;
import com.example.demo.model.ProdusAlimentar;
import com.example.demo.repository.AchizitieRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ProdusRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/achizitii")
@CrossOrigin(origins = "*")
public class AchizitieController {

    @Autowired private AchizitieRepository achizitieRepo;
    @Autowired private ClientRepository clientRepo;
    @Autowired private ProdusRepository produsRepo;
    @Autowired private FurnizareRepository furnizareRepo;

    @PostMapping("/cumpara")
    public ResponseEntity<?> cumparaProdus(@RequestBody RequestAchizitie request) {
        
        // 1. Validari de baza: Verificam daca avem toate ID-urile necesare
        if (request.clientId == null || request.produsId == null || request.furnizareId == null) {
            return ResponseEntity.badRequest().body("Eroare: Lipsesc date (clientId, produsId sau furnizareId)!");
        }

        // 2. Cautam Entitatile
        Optional<Client> clientOpt = clientRepo.findById(request.clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Eroare: Clientul nu exista!");
        }

        Optional<ProdusAlimentar> produsOpt = produsRepo.findById(request.produsId);
        if (produsOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Eroare: Produsul nu exista!");
        }

        // 3. Cautam Furnizarea (Oferta) pentru a verifica STOCUL
        Optional<Furnizare> furnizareOpt = furnizareRepo.findById(request.furnizareId);
        if (furnizareOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Eroare: Oferta/Stocul nu mai exista!");
        }

        Furnizare furnizare = furnizareOpt.get();

        // 4. Verificam daca e destul stoc
        if (furnizare.getStoc() < request.cantitate) {
            return ResponseEntity.badRequest().body("Stoc insuficient! Mai sunt doar " + furnizare.getStoc() + " bucati.");
        }

        // 5. SCADEM STOCUL SI SALVAM
        furnizare.setStoc(furnizare.getStoc() - request.cantitate);
        furnizareRepo.save(furnizare); // Aici se face update in baza de date la Furnizare

        // 6. Salvam Achizitia (Chitanta pentru client)
        Achizitie achizitie = new Achizitie();
        achizitie.setClient(clientOpt.get());
        achizitie.setProdus(produsOpt.get());
        achizitie.setCantitate(request.cantitate);
        achizitie.setDataAchizitie(LocalDate.now());

        achizitieRepo.save(achizitie);
        
        return ResponseEntity.ok("Achizitie reusita! Stocul a fost actualizat.");
    }

    @GetMapping
    public List<Achizitie> getAll() {
        return achizitieRepo.findAll();
    }
    
    // DTO actualizat cu furnizareId
    public static class RequestAchizitie {
        public Long clientId;
        public Long produsId;
        public Long furnizareId; // Camp obligatoriu pentru stoc
        public int cantitate;
    }
}