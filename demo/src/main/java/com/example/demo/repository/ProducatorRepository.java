package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Producator;
import java.util.Optional;

public interface ProducatorRepository extends JpaRepository<Producator, Long> {
    // Login pentru producator
    Optional<Producator> findByDenumireAndPassword(String denumire, String password);
}