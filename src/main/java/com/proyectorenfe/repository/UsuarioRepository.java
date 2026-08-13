package com.proyectorenfe.repository;

import com.proyectorenfe.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario>findByEmail(String email);
}
