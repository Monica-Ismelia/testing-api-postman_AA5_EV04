package com.example.demo_spring.config;

import com.example.demo_spring.auth.JwtAuthenticationFilter;
import com.example.demo_spring.auth.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Importa dependencias necesarias

@Configuration // Anotación de configuración de Spring
@EnableWebSecurity // Habilita la seguridad web de Spring
public class SecurityConfig { // Clase de configuración de seguridad

    private final JwtAuthenticationFilter jwtFilter; // Filtro de autenticación JWT

    // uso de @Lazy para inyectar el filtro de forma perezosa.
    // Esto retrasa la creación del JwtAuthenticationFilter, rompiendo el ciclo.
    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtFilter) { // Constructor con inyección de dependencias
        this.jwtFilter = jwtFilter; // Inicializa el filtro JWT
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // Bean para codificador de contraseñas
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para codificar las contraseñas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception { // Lanza excepción si hay error
        return config.getAuthenticationManager(); // Retorna el gestor de autenticación
    }

    @Bean
    public AuthenticationProvider authenticationProvider(AuthService authService) { // Bean para el proveedor de autenticación
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // Proveedor de autenticación DAO
        provider.setUserDetailsService(authService); // Establece el servicio de detalles de usuario
        provider.setPasswordEncoder(passwordEncoder()); // Establece el codificador de contraseñas
        return provider; // Retorna el proveedor de autenticación
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Bean para la cadena de filtros de seguridad

        http.csrf(csrf -> csrf.disable()) // Deshabilita CSRF
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura la gestión de sesiones como sin estado
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**", 
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"  
                     ).permitAll() // Permite acceso público a Auth y Swagger
                    .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Agrega el filtro JWT antes del filtro de autenticación de nombre de usuario y contraseña

        return http.build(); // Construye y retorna la cadena de filtros de seguridad
    }
} // Fin de la clase SecurityConfig