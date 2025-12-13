package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Furnizare;
import java.util.Optional;

public interface FurnizareRepository extends JpaRepository<Furnizare, Long> {
    Optional<Furnizare> findByProducatorIdAndProdusId(Long producatorId, Long produsId);
}