package com.example.demo_spring.controller; // src/main/java/com/example/demo_spring/controller/EmpleadoController.java


import com.example.demo_spring.model.Empleado;
import com.example.demo_spring.service.EmpleadoService;
import com.example.demo_spring.repository.EmpleadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoRepository repository;

    // Mensajes JSON de ejemplo reutilizables (puedes cambiarlos)
    private final String ERROR_403_JSON = "{\"status\":403,\"error\":\"No autorizado\",\"message\":\"No tiene permisos para acceder a este recurso\",\"timestamp\":\"2024-01-01T10:20:30\"}";
    private final String ERROR_404_JSON = "{\"status\":404,\"error\":\"NOT_FOUND\",\"message\":\"Empleado no encontrado\",\"timestamp\":\"2024-01-01T10:20:30\"}";
    private final String ERROR_400_JSON = "{\"status\":400,\"error\":\"BAD_REQUEST\",\"message\":\"Datos de entrada inválidos (ej. correo duplicado)\",\"timestamp\":\"2024-01-01T10:20:30\"}";

    // ---------------------------------------------------------
    // GET → Todos
    // ---------------------------------------------------------
    @Operation(summary = "Obtener todos los empleados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de empleados recuperada",
            content = @Content(schema = @Schema(implementation = Empleado.class))
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_403_JSON)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<Empleado>> getAll() {
        return ResponseEntity.ok(empleadoService.listarTodos());
    }

    // ---------------------------------------------------------
    // GET → Por ID
    // ---------------------------------------------------------
    @Operation(summary = "Obtener un empleado por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado encontrado",
            content = @Content(schema = @Schema(implementation = Empleado.class))
        ),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_404_JSON)
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_403_JSON)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Empleado empleado = empleadoService.findById(id);
            return ResponseEntity.ok(empleado);
        } catch (RuntimeException e) {
            // Dejar que el GlobalExceptionHandler forme la respuesta o devolver explícitamente:
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
        }
    }

    // ---------------------------------------------------------
    // POST → Crear
    // ---------------------------------------------------------
    @Operation(summary = "Registrar un nuevo empleado")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Empleado registrado con éxito",
            content = @Content(schema = @Schema(implementation = Empleado.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_400_JSON)
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_403_JSON)
            )
        )
    })
    @PostMapping
    public ResponseEntity<?> crear(@org.springframework.web.bind.annotation.RequestBody Empleado empleado) {
        if (empleado.getCorreo() == null || empleado.getCorreo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no puede ser nulo");
        }
        try {
            Empleado nuevo = empleadoService.guardar(empleado);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // PUT → Actualización completa
    // ---------------------------------------------------------
    @Operation(
        summary = "Actualizar completamente un empleado (PUT)",
        // usar la versión fully-qualified de RequestBody de Swagger para evitar colisión de nombres
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto Empleado completo requerido para la actualización total.",
            required = true,
            content = @Content(
                schema = @Schema(implementation = Empleado.class),
                examples = @ExampleObject(name = "Ejemplo completo",
                    value = "{\"id\":5,\"nombre\":\"Elena Garcías\",\"correo\":\"elena.g@ejemplo.com\",\"salario\":55000.00,\"fechaIngreso\":\"2023-08-15\"}")
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado actualizado",
            content = @Content(schema = @Schema(implementation = Empleado.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_400_JSON)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_404_JSON)
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @org.springframework.web.bind.annotation.RequestBody Empleado data) {

        if (data.getCorreo() == null || data.getCorreo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El campo correo es obligatorio para la actualización (PUT).");
        }

        try {
            Empleado actualizado = empleadoService.actualizar(id, data);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().toLowerCase().contains("no encontrado")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // PATCH → Parcial
    // ---------------------------------------------------------
    @Operation(
        summary = "Actualizar parcialmente un empleado (PATCH)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Campos a modificar. El correo debe ser único.",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(name = "Ejemplo PATCH",
                    value = "{\"nombre\":\"Maria\",\"salario\":45000.00}")
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado actualizado parcialmente",
            content = @Content(schema = @Schema(implementation = Empleado.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_400_JSON)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_404_JSON)
            )
        )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcial(@PathVariable Long id,
                                               @org.springframework.web.bind.annotation.RequestBody Map<String, Object> cambios) {

        Empleado empleado = empleadoService.findById(id);

        for (String campo : cambios.keySet()) {
            Object v = cambios.get(campo);
            switch (campo) {
                case "nombre" -> empleado.setNombre(v.toString());
                case "correo" -> {
                    if (repository.existsByCorreo(v.toString()) &&
                            !v.toString().equals(empleado.getCorreo())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
                    }
                    empleado.setCorreo(v.toString());
                }
                case "salario" -> empleado.setSalario(new BigDecimal(v.toString()));
                case "fechaIngreso" -> empleado.setFechaIngreso(LocalDate.parse(v.toString()));
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo no permitido: " + campo);
            }
        }

        return ResponseEntity.ok(repository.save(empleado));
    }

    // ---------------------------------------------------------
    // DELETE → Eliminar
    // ---------------------------------------------------------
    @Operation(summary = "Eliminar un empleado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado eliminado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"mensaje\":\"Empleado eliminado\"}")
            )
        ),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_404_JSON)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            empleadoService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Empleado eliminado"));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
