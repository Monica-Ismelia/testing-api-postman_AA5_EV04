// src/main/java/com/example/demo_spring/model/Usuario.java
package com.example.demo_spring.model; // Modelo de entidad Usuario

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// Importa anotaciones de JPA y validación
@Entity //Define la  entidad usuario de la  base de datos
@Table(name = "usuarios", uniqueConstraints = { // Define la tabla y la restricción única en el correo
    @UniqueConstraint(columnNames = "correo") // El correo debe ser único
})
public class Usuario {// Clase modelo para Usuario

    @Id// Define la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Id autoincremental
    private Long id;// ID del usuario

    @NotBlank// El nombre no puede estar en blanco
    @Size(max = 50) // Máximo 50 caracteres para el nombre
    private String nombre; // Nombre del usuario

    @NotBlank// El correo no puede estar en blanco
    @Size(max = 100)// Máximo 100 caracteres para el correo
    @Email // Debe ser un formato de correo válido
    private String correo; // Correo electrónico del usuario

    @NotBlank// La contraseña no puede estar en blanco
    @Size(max = 120)// Máximo 120 caracteres para la contraseña
    private String contrasena; // Contraseña del usuario

    // Constructores
    public Usuario() {} // Constructor por defecto

    public Usuario(String nombre, String correo, String contrasena) { // Constructor con parámetros
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Getters y Setters 
    public Long getId() { return id; }// Obtiene el ID
    public void setId(Long id) { this.id = id; } // Establece el ID

    public String getNombre() { return nombre; }// Obtiene el nombre
    public void setNombre(String nombre) { this.nombre = nombre; } // Establece el nombre

    public String getCorreo() { return correo; }// Obtiene el correo
    public void setCorreo(String correo) { this.correo = correo; } // Establece el correo
    public String getContrasena() { return contrasena; }// Obtiene la contraseña
    public void setContrasena(String contrasena) { this.contrasena = contrasena; } // Establece la contraseña
}