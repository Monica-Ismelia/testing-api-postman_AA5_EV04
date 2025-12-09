package com.example.demo_spring; // Clase principal de la aplicación Spring Boot

import org.springframework.boot.SpringApplication;// Importa SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication;// Importa la anotación SpringBootApplication
import org.springframework.context.annotation.Bean;// Importa la anotación Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry;// Importa CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;// Importa WebMvcConfigurer
// Importa dependencias necesarias

@SpringBootApplication
public class DemoSpringApplication {// Clase principal de la aplicación

    public static void main(String[] args) {// Método principal
        SpringApplication.run(DemoSpringApplication.class, args);// Inicia la aplicación Spring Boot
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {// Configuración CORS para permitir solicitudes desde el frontend
        return new WebMvcConfigurer() {// Implementa WebMvcConfigurer
            @Override
            public void addCorsMappings(CorsRegistry registry) {// Configura los mapeos CORS
                registry.addMapping("/api/**")// Permite CORS para rutas que comienzan con /api/
                        .allowedOrigins("http://localhost:3000")//puerto para el  fronted
                        .allowedMethods("GET", "POST", "PUT", "DELETE");//Crud
            }
        };
    }
}