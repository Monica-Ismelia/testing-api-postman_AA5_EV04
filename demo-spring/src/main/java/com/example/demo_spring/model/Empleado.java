package com.example.demo_spring.model; // src/main/java/com/example/demo_spring/model/Empleado.java

import jakarta.persistence.*; // Importa las anotaciones de JPA
import io.swagger.v3.oas.annotations.media.Schema; // Importamos la anotación Schema
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "empleados")
@Schema(description = "Detalles completos de la entidad Empleado") // Documentación de la clase
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del empleado", example = "8") // Ejemplo para ID
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre completo del empleado", example = "Ana Pérez") // Ejemplo para Nombre
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    @Schema(description = "Correo electrónico (debe ser único)", example = "ana.perez@empresa.com") // Ejemplo para Correo
    private String correo;

    @Column(nullable = false, precision = 12, scale = 2)
    @Schema(description = "Salario anual del empleado", example = "48500.75") // Ejemplo para Salario
    private BigDecimal salario;

    @Column
    @Schema(description = "Fecha de ingreso (Formato YYYY-MM-DD)", example = "2024-03-01") // Ejemplo para Fecha
    private LocalDate fechaIngreso;

    // Constructor por defecto
    public Empleado() {}

    // Constructor con parámetros
    public Empleado(String nombre, String correo, BigDecimal salario, LocalDate fechaIngreso) {
        this.nombre = nombre;
        this.correo = correo;
        this.salario = salario;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters y Setters (Se mantienen sin cambios)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
}