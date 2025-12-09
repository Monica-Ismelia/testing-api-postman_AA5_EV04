// src/main/java/com/example/demo_spring/auth/JwtUtil.java
package com.example.demo_spring.auth; // Utilidad para manejo de JWT

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
// Importa dependencias necesarias

@Service// Anotación de servicio de Spring
public class JwtUtil { // Clase para manejo de JWT

    @Value("${jwt.secret}")// Clave secreta para firmar los tokens
    private String jwtSecret;// Clave secreta

    @Value("${jwt.expiration}")// Tiempo de expiración del token en milisegundos
    private Long jwtExpirationMs;// Tiempo de expiración

    public String generateToken(UserDetails userDetails) {// Genera un token JWT para el usuario
        return generateToken(new HashMap<>(), userDetails);//   Genera el token con reclamos adicionales vacíos
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {// Genera un token JWT con reclamos adicionales
        return Jwts.builder()
                .setClaims(extraClaims) // Establece los reclamos adicionales
                .setSubject(userDetails.getUsername()) // Establece el sujeto (nombre de usuario)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Establece la fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Establece la fecha de expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma el token con la clave secreta y algoritmo HS256
                .compact();// Compila y retorna el token JWT
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);// Extrae el nombre de usuario del token
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // Extrae un reclamo específico del token
        final Claims claims = extractAllClaims(token);// Extrae todos los reclamos del token
        return claimsResolver.apply(claims);// Aplica la función para obtener el reclamo específico
    }

    private Claims extractAllClaims(String token) { // Extrae todos los reclamos del token JWT
        return Jwts.parserBuilder() // Construye el parser de JWT
                .setSigningKey(getSigningKey()) // Establece la clave de firma
                .build() 
                .parseClaimsJws(token)// Parsea el token JWT
                .getBody();// Retorna el cuerpo (reclamos) del token
    }

    private Key getSigningKey() { // Obtiene la clave de firma a partir de la clave secreta
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);// Decodifica la clave secreta en bytes
        return Keys.hmacShaKeyFor(keyBytes);// Crea la clave HMAC SHA a partir de los bytes
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extrae el nombre de usuario del token
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }// Verifica si el token es válido (nombre de usuario coincide y no está expirado)

    private boolean isTokenExpired(String token) { // Verifica si el token ha expirado
        return extractExpiration(token).before(new Date());// Compara la fecha de expiración con la fecha actual
    }

    private Date extractExpiration(String token) { // Extrae la fecha de expiración del token
        return extractClaim(token, Claims::getExpiration); // Utiliza la función para obtener la fecha de expiración
    }
}