package com.example.demo_spring.repository; // Repositorio para la entidad Empleado

import com.example.demo_spring.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// Importa dependencias necesarias
@Repository// Anotación de repositorio de Spring
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {// Interfaz de repositorio para Empleado
    boolean existsByCorreo(String correo);// Verifica si existe un empleado por correo
}
