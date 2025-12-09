package com.example.demo_spring.service; // Servicio para la entidad Empleado


import com.example.demo_spring.model.Empleado;
import com.example.demo_spring.repository.EmpleadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repository;

    // Listar todos
    public List<Empleado> listarTodos() {
        return repository.findAll();
    }

    // Buscar por ID
    public Empleado findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
    }

    // Crear (validando correo)
    public Empleado guardar(Empleado empleado) {
        if (repository.existsByCorreo(empleado.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado: " + empleado.getCorreo());
        }
        return repository.save(empleado);
    }

    // Actualizar completo
    public Empleado actualizar(Long id, Empleado data) {

        Empleado empleado = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        if (!empleado.getCorreo().equals(data.getCorreo())
                && repository.existsByCorreo(data.getCorreo())) {
            throw new RuntimeException("El correo ya está en uso");
        }

        empleado.setNombre(data.getNombre());
        empleado.setCorreo(data.getCorreo());
        empleado.setSalario(data.getSalario());
        empleado.setFechaIngreso(data.getFechaIngreso());

        return repository.save(empleado);
    }

    // Eliminar
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }
}
