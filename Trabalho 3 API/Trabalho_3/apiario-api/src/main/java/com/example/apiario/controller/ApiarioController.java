package com.example.apiario.controller;

import com.example.apiario.model.Colmeia;
import com.example.apiario.service.ApiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiarioController {
    @Autowired
    private ApiarioService apiarioService;

    @PostMapping("/colmeia_new")
    public Colmeia criarColmeia(@RequestParam String nomeApicultor,
                                @RequestParam int capacidadeAbelhas,
                                @RequestParam int capacidadeMel) {
        return apiarioService.criarColmeia(nomeApicultor, capacidadeAbelhas, capacidadeMel);
    }

    @GetMapping("/colmeia_list")
    public List<Colmeia> listarColmeias(@RequestParam String nomeApicultor) {
        return apiarioService.listarColmeias(nomeApicultor);
    }

    @DeleteMapping("/colmeia_del/{id}")
    public void deletarColmeia(@PathVariable Long id) {
        apiarioService.deletarColmeia(id);
    }

    @PostMapping("/colmeia_add_abelhas")
    public Colmeia adicionarAbelhas(@RequestParam Long idColmeia,@RequestParam int quantidade,@RequestParam boolean rainhaPresente) {
        
        return apiarioService.AdicionarAbelhas(idColmeia, quantidade, rainhaPresente); 
    }
}