// src/main/java/com/example/demo_spring/auth/AuthService.java
package com.example.demo_spring.auth;// Servicio de autenticación y registro

import com.example.demo_spring.model.Usuario;
import com.example.demo_spring.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
// Importa dependencias necesarias
@Service // Anotación de servicio de Spring
public class AuthService implements UserDetailsService { // Implementa UserDetailsService para cargar usuarios

    private final UsuarioRepository usuarioRepository;// Repositorio de usuarios
    private final PasswordEncoder passwordEncoder;// Codificador de contraseñas

    // INYECCIÓN POR CONSTRUCTOR (ROMPE EL CICLO)
    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) { // Constructor con inyección de dependencias
        this.usuarioRepository = usuarioRepository;// Inicializa el repositorio de usuarios
        this.passwordEncoder = passwordEncoder;// Inicializa el codificador de contraseñas
    }

    // Registro de nuevo usuario
    public Usuario registrarUsuario(Usuario usuario) {// Registra un nuevo usuario
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) { // Verifica si el correo ya está registrado
            throw new RuntimeException("El correo ya está registrado: " + usuario.getCorreo());// Lanza excepción si el correo ya existe
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));// Codifica la contraseña antes de guardar
        return usuarioRepository.save(usuario);// Guarda el nuevo usuario en el repositorio
    }

    // Carga el usuario por correo (para Spring Security)
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException { // Carga el usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo) // Busca el usuario por correo
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo)); // Lanza excepción si no se encuentra el usuario

        return User.builder()// Construye el UserDetails para Spring Security
            .username(usuario.getCorreo()) // Establece el nombre de usuario como el correo
            .password(usuario.getContrasena()) // Establece la contraseña codificada
            .authorities(new ArrayList<>()) // Establece las autoridades (roles) vacías
            .build();// Construye y retorna el UserDetails
    }
}// Fin de la clase AuthService
