package com.calculadora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Main {

    @GetMapping("/somar/{a}/{b}")
    public String somar(@PathVariable int a, @PathVariable int b) {
        return "Resultado da soma: " + (a + b);
    }

    @GetMapping("/subtrair/{a}/{b}")
    public String subtrair(@PathVariable int a, @PathVariable int b) {
        return "Resultado da soma: " + (a - b);
    }

    @GetMapping("/mul/{a}/{b}")
    public String multiplicar(@PathVariable int a, @PathVariable int b) {
        return "Resultado da soma: " + (a * b);
    }

    @GetMapping("/div/{a}/{b}")
    public String dividir(@PathVariable int a, @PathVariable int b) {
        if (b == 0) {
            return "Erro: Divisão por zero não é permitida.";
        }
        return "Resultado da soma: " + (a / b);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}