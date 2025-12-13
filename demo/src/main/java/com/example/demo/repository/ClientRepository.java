package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Client; // Importă entitatea Client
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // Aici definim metoda custom pe care o folosești în controller
    // Spring o va implementa automat bazat pe nume
    Optional<Client> findByNumeAndPassword(String nume, String password);
}