package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.example.demo.utils.SecurityUtil;

// Importuri JAVA standard (fără ele ai erori la List și Optional)
import java.util.List;
import java.util.Optional;

// Importuri din proiectul tău (trebuie să existe fișierele în pachetele respective)
import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;

@RestController
@RequestMapping("/api/clienti")
@CrossOrigin(origins = "*") 
public class ClientController {

    @Autowired
    private ClientRepository clientRepo;

    // // 1. Inregistrare (Sign Up)
    // @PostMapping("/signup")
    // public Client adaugaClient(@RequestBody Client client) {
    //     return clientRepo.save(client);
    // }

    // // 2. Login (Verificare simpla)
    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody Client loginRequest) {
    //     // Aceasta metodă trebuie să existe în ClientRepository (vezi Pasul 1)
    //     Optional<Client> client = clientRepo.findByNumeAndPassword(
    //         loginRequest.getNume(), loginRequest.getPassword()
    //     );
        
    //     if(client.isPresent()) {
    //         return ResponseEntity.ok(client.get());
    //     } else {
    //         return ResponseEntity.status(401).body("Nume sau parola gresita");
    //     }
    // }
    // 1. Inregistrare (Cu Hashing)
    @PostMapping("/signup")
    public Client adaugaClient(@RequestBody Client client) {
        // Criptam parola inainte de salvare!
        String parolaCriptata = SecurityUtil.hashPassword(client.getPassword());
        client.setPassword(parolaCriptata);
        
        return clientRepo.save(client);
    }

    // 2. Login (Cu Hashing)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Client loginRequest) {
        // Criptam parola primita ca sa o putem compara cu cea din baza de date
        String parolaHashCautata = SecurityUtil.hashPassword(loginRequest.getPassword());

        Optional<Client> client = clientRepo.findByNumeAndPassword(
            loginRequest.getNume(), 
            parolaHashCautata // Cautam dupa hash, nu dupa text simplu
        );
        
        if(client.isPresent()) {
            return ResponseEntity.ok(client.get());
        } else {
            return ResponseEntity.status(401).body("Nume sau parola gresita");
        }
    }

    @DeleteMapping("/{id}")
    public void stergeClient(@PathVariable Long id) {
        clientRepo.deleteById(id);
    }

    // 3. Vizualizare toti clientii
    @GetMapping
    public List<Client> getClienti() {
        return clientRepo.findAll();
    }
}