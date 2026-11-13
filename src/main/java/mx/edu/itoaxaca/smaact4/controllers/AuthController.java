package mx.edu.itoaxaca.smaact4.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mx.edu.itoaxaca.smaact4.dto.auth.AuthResponse;
import mx.edu.itoaxaca.smaact4.dto.auth.LoginRequest;
import mx.edu.itoaxaca.smaact4.dto.auth.RegisterRequest;
import mx.edu.itoaxaca.smaact4.dto.auth.UsuarioResponse;
import mx.edu.itoaxaca.smaact4.service.AuthService;

/**
 * Endpoints de autenticacion.
 * register y login son publicos; perfil requiere token.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/register -> 201 Created + token
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

    // POST /api/auth/login -> 200 OK + token  (401 si las credenciales son incorrectas)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // GET /api/auth/perfil -> datos del usuario dueno del token (protegido)
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponse> perfil(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(authService.perfil(jwt.getSubject()));
    }
}
