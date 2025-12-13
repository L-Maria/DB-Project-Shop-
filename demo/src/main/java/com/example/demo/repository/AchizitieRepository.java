package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Achizitie;
import java.util.List;

public interface AchizitieRepository extends JpaRepository<Achizitie, Long> {
    // Metoda utila: Sa vezi istoricul de cumparaturi al unui client
    List<Achizitie> findByClientId(Long clientId);
}