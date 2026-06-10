package com.saberpro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SaberProApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaberProApplication.class, args);
        System.out.println("=== SISTEMA SABER PRO INICIADO ===");
        System.out.println("http://localhost:8080");
    }
}