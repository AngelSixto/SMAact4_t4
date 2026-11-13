package mx.edu.itoaxaca.smaact4.service;

import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.itoaxaca.smaact4.dto.auth.AuthResponse;
import mx.edu.itoaxaca.smaact4.dto.auth.LoginRequest;
import mx.edu.itoaxaca.smaact4.dto.auth.RegisterRequest;
import mx.edu.itoaxaca.smaact4.dto.auth.UsuarioResponse;
import mx.edu.itoaxaca.smaact4.exception.ConflictoException;
import mx.edu.itoaxaca.smaact4.exception.RecursoNoEncontradoException;
import mx.edu.itoaxaca.smaact4.models.entity.Rol;
import mx.edu.itoaxaca.smaact4.models.entity.Usuario;
import mx.edu.itoaxaca.smaact4.repository.UsuarioRepository;
import mx.edu.itoaxaca.smaact4.security.JwtService;

/**
 * Logica de registro y login.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        String email = normalizar(request.email());
        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictoException("Ya existe un usuario registrado con el email " + email);
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre().trim());
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(request.password())); // se guarda el HASH
        usuario.setRol(Rol.USER);
        usuarioRepository.save(usuario);

        return respuestaConToken(usuario);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizar(request.email());

        // Spring Security verifica email + password (BCrypt). Si fallan lanza BadCredentialsException -> 401
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password()));

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        return respuestaConToken(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse perfil(String email) {
        return usuarioRepository.findByEmail(email)
                .map(UsuarioResponse::from)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
    }

    private AuthResponse respuestaConToken(Usuario usuario) {
        String token = jwtService.generarToken(usuario);
        return AuthResponse.bearer(token, jwtService.getExpiracionSegundos(), UsuarioResponse.from(usuario));
    }

    private String normalizar(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
