package com.example.demo_spring.auth; // Filtro de autenticación JWT

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
// Importa dependencias necesarias

@Component // Anotación de componente de Spring
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Extiende OncePerRequestFilter para filtrar cada solicitud una vez

    private final JwtUtil jwtUtil; // Utilidad para manejo de JWT
    private final AuthService authService;// Servicio de autenticación y registro

    // 👇 INYECCIÓN POR CONSTRUCTOR (ROMPE EL CICLO)
    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthService authService) {// Constructor con inyección de dependencias
        this.jwtUtil = jwtUtil;// Inicializa la utilidad JWT
        this.authService = authService;// Inicializa el servicio de autenticación
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,// Método para filtrar cada solicitud HTTP
                                    HttpServletResponse response,// Respuesta HTTP
                                    FilterChain filterChain)//  Cadena de filtros
            throws ServletException, IOException {
// Extrae el encabezado Authorization
        final String authHeader = request.getHeader("Authorization");// Encabezado de autorización

        String jwt = null;// Token JWT
        String username = null;// Nombre de usuario extraído del token

        if (authHeader != null && authHeader.startsWith("Bearer ")) { // Verifica si el encabezado comienza con "Bearer "
            jwt = authHeader.substring(7);// Extrae el token JWT del encabezado
            username = jwtUtil.extractUsername(jwt);// Extrae el nombre de usuario del token
        } // Si se extrajo un nombre de usuario y no hay autenticación en el contexto de seguridad

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // Verifica si el nombre de usuario no es nulo y no hay autenticación en el contexto de seguridad
            UserDetails userDetails = authService.loadUserByUsername(username);// Carga los detalles del usuario

            if (jwtUtil.isTokenValid(jwt, userDetails)) { // Verifica si el token JWT es válido
                UsernamePasswordAuthenticationToken authToken = // Crea el token de autenticación
                        new UsernamePasswordAuthenticationToken(// Token de autenticación
                                userDetails,// Detalles del usuario
                                null,// Credenciales (no se utilizan en este caso)
                                userDetails.getAuthorities()// Autoridades del usuario
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));// Establece los detalles de la solicitud

                SecurityContextHolder.getContext().setAuthentication(authToken);// Establece la autenticación en el contexto de seguridad
            }
        }

        filterChain.doFilter(request, response);// Continúa con la cadena de filtros
    }
}// Fin de la clase JwtAuthenticationFilter
