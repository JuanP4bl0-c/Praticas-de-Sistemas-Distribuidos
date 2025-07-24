package com.example.apiario.repository;

import com.example.apiario.model.Colmeia;
import com.example.apiario.model.Apicultor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ColmeiaRepository extends JpaRepository<Colmeia, Long> {
    List<Colmeia> findByApicultor(Apicultor apicultor);
}