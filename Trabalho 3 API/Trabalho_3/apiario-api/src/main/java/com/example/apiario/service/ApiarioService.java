package com.example.apiario.service;

import com.example.apiario.model.Apicultor;
import com.example.apiario.model.Colmeia;

import com.example.apiario.repository.ApicultorRepository;
import com.example.apiario.repository.ColmeiaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiarioService {

    @Autowired
    private ApicultorRepository apicultorRepo;
    
    @Autowired
    private ColmeiaRepository colmeiaRepo;

    public Colmeia criarColmeia(String nomeApicultor, int capacidadeAbelhas, int capacidadeMel) {
        
        Apicultor apicultor = apicultorRepo.findByNome(nomeApicultor);

        if (apicultor == null) {
            apicultor = new Apicultor();
            apicultor.setNome(nomeApicultor);
            apicultor = apicultorRepo.save(apicultor);
        }
        Colmeia colmeia = new Colmeia();
        colmeia.setCapacidadeAbelhas(capacidadeAbelhas);
        colmeia.setCapacidadeMel(capacidadeMel);
        colmeia.setApicultor(apicultor);
        return colmeiaRepo.save(colmeia);
    }

    public List<Colmeia> listarColmeias(String nomeApicultor) {
        Apicultor apicultor = apicultorRepo.findByNome(nomeApicultor);
        if (apicultor == null) return List.of();
        return colmeiaRepo.findByApicultor(apicultor);
    }

    public Colmeia AdicionarAbelhas(Long idColmeia, int quantidade, boolean rainhaPresente) {
        
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade de abelhas deve ser maior que zero");
        }
        if (idColmeia == null) {
            throw new IllegalArgumentException("ID da colmeia não pode ser nulo");
        }

        Colmeia colmeia = colmeiaRepo.findById(idColmeia).orElseThrow(() -> new RuntimeException("Colmeia não encontrada"));
        
        colmeia.adicionarAbelhas(quantidade, rainhaPresente);
        return colmeiaRepo.save(colmeia);
        
    }

    public void deletarColmeia(Long id) {
        colmeiaRepo.deleteById(id);
    }



}