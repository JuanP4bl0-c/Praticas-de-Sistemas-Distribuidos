package com.example;

import jakarta.ejb.Stateless;

@Stateless(name = "HelloBean")
public class HelloService{
    public String sayHello(String name) {
        return "Hello, " + name + ", isso é um teste em API EJB!";
    }
}