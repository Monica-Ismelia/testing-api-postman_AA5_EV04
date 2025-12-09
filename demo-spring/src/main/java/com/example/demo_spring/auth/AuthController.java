// src/main/java/com/example/demo_spring/auth/AuthController.javapackage com.example.demo_spring.auth;
package com.example.demo_spring.auth;


import com.example.demo_spring.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro y login con JWT")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    // Constantes para documentación de errores 400
    private final String ERROR_400_REGISTER_JSON = "{\"status\":400,\"error\":\"Bad Request\",\"message\":\"El correo ya está registrado o faltan datos requeridos.\"}";
    private final String ERROR_400_LOGIN_JSON = "{\"status\":400,\"error\":\"Bad Request\",\"message\":\"Credenciales inválidas\"}";


    // ------------------------------------------------------------
    // Registro
    // ------------------------------------------------------------
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un usuario en la base de datos y devuelve un mensaje de confirmación.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de usuario",
                                    value = "{\n" +
                                            " \"nombre\": \"Laura Gómez\",\n" +
                                            " \"correo\": \"laura@test.com\",\n" +
                                            " \"contrasena\": \"123456\"\n" +
                                            "}"
                            )
                    )
            )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario registrado exitosamente",
            content = @Content(mediaType = "text/plain", // La respuesta es un texto (String)
                examples = @ExampleObject(
                    name = "Éxito",
                    value = "Usuario registrado exitosamente: laura@test.com"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error en los datos enviados",
            content = @Content(mediaType = "application/json", // Documenta el JSON de error
                examples = @ExampleObject(
                    name = "Error 400 Ejemplo",
                    value = ERROR_400_REGISTER_JSON
                )
            )
        )
    })
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = authService.registrarUsuario(usuario);
            return ResponseEntity.ok("Usuario registrado exitosamente: " + nuevo.getCorreo());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // Login
    // ------------------------------------------------------------
    @Operation(
            summary = "Iniciar sesión",
            description = "Valida credenciales y devuelve un token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de login",
                                    value = "{\n" +
                                            " \"correo\": \"laura@test.com\",\n" +
                                            " \"contrasena\": \"123456\"\n" +
                                            "}"
                            )
                    )
            )
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Login exitoso",
                content = @Content(
                        schema = @Schema(implementation = JwtResponse.class),
                        examples = @ExampleObject(
                                name = "Token JWT",
                                value = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\" }"
                        )
                )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Credenciales inválidas",
            content = @Content(mediaType = "application/json", // Documenta el JSON de error
                examples = @ExampleObject(
                    name = "Error 400 Ejemplo",
                    value = ERROR_400_LOGIN_JSON
                )
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCorreo(),
                            loginRequest.getContrasena()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(jwt));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Credenciales inválidas");
        }
    }

    // ------------------------------------------------------------
    // Request y Response Documentados
    // ------------------------------------------------------------
    
    // (LoginRequest y JwtResponse se mantienen sin cambios, ya tienen documentación de Schema y Example)
    
    @Schema(description = "Objeto para iniciar sesión")
    public static class LoginRequest {

        @Schema(description = "Correo del usuario", example = "usuario@test.com")
        private String correo;

        @Schema(description = "Contraseña del usuario", example = "123456")
        private String contrasena;

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    }

    @Schema(description = "Respuesta con token JWT")
    public static class JwtResponse {
        @Schema(description = "Token JWT generado")
        private String token;

        public JwtResponse(String token) { this.token = token; }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}