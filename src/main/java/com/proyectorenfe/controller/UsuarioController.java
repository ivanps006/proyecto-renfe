package com.proyectorenfe.controller;

import com.proyectorenfe.config.SecurityConfig;
import com.proyectorenfe.dto.LoginUsuarioDTO;
import com.proyectorenfe.dto.RegistroUsuarioDTO;
import com.proyectorenfe.model.Usuario;
import com.proyectorenfe.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Usuario> obtenerTodos(){
        return usuarioRepository.findAll();
    }

    @PostMapping("/registro")
    public String crearUsuario(@RequestBody RegistroUsuarioDTO registroUsuarioDTO){
        String passwordCifrada = passwordEncoder.encode(registroUsuarioDTO.getPassword());

        Usuario usuario = new Usuario(
                registroUsuarioDTO.getNombre(),
                registroUsuarioDTO.getEmail(),
                passwordCifrada
        );

        usuarioRepository.save(usuario);

        return "Usuario registrado correctamente";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginUsuarioDTO loginUsuarioDTO) {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(
                loginUsuarioDTO.getEmail()
        );

        if (usuario.isPresent()) {

            boolean passwordCorrecta = passwordEncoder.matches(
                    loginUsuarioDTO.getPassword(),
                    usuario.get().getPassword()
            );

            if (passwordCorrecta) {
                return "Login correcto";
            }
        }

        return "Login incorrecto";
    }
}
