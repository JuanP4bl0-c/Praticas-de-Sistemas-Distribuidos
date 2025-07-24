package com.example.apiario.repository;

import com.example.apiario.model.Apicultor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApicultorRepository extends JpaRepository<Apicultor, Long> {
    
    Apicultor findByNome(String nome);

}