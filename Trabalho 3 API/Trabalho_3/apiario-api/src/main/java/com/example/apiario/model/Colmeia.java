package com.example.apiario.model;

import javax.persistence.*;

@Entity
public class Colmeia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    
    private int capacidadeAbelhas;
    private int abelhas = 0;
    private boolean rainha_presente = false;

    private int capacidadeMel;

    @ManyToOne
    @JoinColumn(name = "apicultor_id")
    private Apicultor apicultor;

    public Long getId() {
        return id;
    }

    public int getCapacidadeAbelhas() {
        return capacidadeAbelhas;
    }

    public int getCapacidadeMel() {
        return capacidadeMel;
    }

    public Apicultor getApicultor() {
        return apicultor;
    }

    public int getAbelhas() {
        return abelhas;
    }

    public boolean isRainhaPresente() {
        return rainha_presente;
    }

    public void setCapacidadeAbelhas(int capacidadeAbelhas) {
        this.capacidadeAbelhas = capacidadeAbelhas;
    }

    public void setCapacidadeMel(int capacidadeMel) {
        this.capacidadeMel = capacidadeMel;
    }

    public void setApicultor(Apicultor apicultor) {
        this.apicultor = apicultor;
    }

    public void adicionarAbelhas(int quantidade,boolean rainhaPresente) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade de abelhas não pode ser negativa");

        }if (this.abelhas + quantidade > this.capacidadeMel) {
            throw new IllegalArgumentException("Capacidade de abelhas excedida");
            
        }

        this.abelhas += quantidade;
        this.rainha_presente = rainhaPresente;
    }
    
}