package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.ProdusAlimentar;
import java.util.Optional;


public interface ProdusRepository extends JpaRepository<ProdusAlimentar, Long> {
    Optional<ProdusAlimentar> findByDenumire(String denumire);
}