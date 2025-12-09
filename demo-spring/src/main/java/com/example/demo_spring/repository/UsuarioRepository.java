// src/main/java/com/example/demo_spring/repository/UsuarioRepository.java
package com.example.demo_spring.repository; // Repositorio para la entidad Usuario

import com.example.demo_spring.model.Usuario;// Importa la clase modelo Usuario
import org.springframework.data.jpa.repository.JpaRepository;// Importa JpaRepository
import org.springframework.stereotype.Repository;// Importa la anotación Repository

import java.util.Optional; // Importa Optional para manejo de valores que pueden estar ausentes
// Importa dependencias necesarias

@Repository// Anotación de repositorio de Spring
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {// Interfaz de repositorio para Usuario
    Optional<Usuario> findByCorreo(String correo);// Busca un usuario por correo
    boolean existsByCorreo(String correo);// Verifica si existe un usuario por correo
}