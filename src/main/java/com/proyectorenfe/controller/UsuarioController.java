package com.proyectorenfe.controller;

import com.proyectorenfe.config.SecurityConfig;
import com.proyectorenfe.dto.LoginRespuestaDTO;
import com.proyectorenfe.dto.LoginUsuarioDTO;
import com.proyectorenfe.dto.RegistroUsuarioDTO;
import com.proyectorenfe.dto.UsuarioDTO;
import com.proyectorenfe.model.Usuario;
import com.proyectorenfe.repository.UsuarioRepository;
import com.proyectorenfe.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<UsuarioDTO> obtenerTodos() {

        return usuarioRepository.findAll()
                .stream()
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail()
                ))
                .toList();
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
    public LoginRespuestaDTO login(@RequestBody LoginUsuarioDTO loginUsuarioDTO) {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(
                loginUsuarioDTO.getEmail()
        );

        if (usuario.isPresent()) {

            boolean passwordCorrecta = passwordEncoder.matches(
                    loginUsuarioDTO.getPassword(),
                    usuario.get().getPassword()
            );

            if (passwordCorrecta) {
                String token = jwtService.generarToken(usuario.get().getEmail());

                return new LoginRespuestaDTO("Login correcto", token);

            }
        }

        return new LoginRespuestaDTO("Login incorrecto", null);
    }

    @GetMapping("/perfil")
    public UsuarioDTO obtenerPerfil(Authentication authentication) {

        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow();

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }
}
