package com.example;

import jakarta.ejb.Stateless;

@Stateless(name = "HelloBean")
public class HelloBean {
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}