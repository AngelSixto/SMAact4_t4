package mx.edu.itoaxaca.smaact4.dto.auth;

/**
 * DTO de SALIDA del registro y del login: el token JWT y los datos publicos del usuario.
 */
public record AuthResponse(
        String token,
        String tipo,
        long expiraEnSegundos,
        UsuarioResponse usuario
) {
    public static AuthResponse bearer(String token, long expiraEnSegundos, UsuarioResponse usuario) {
        return new AuthResponse(token, "Bearer", expiraEnSegundos, usuario);
    }
}
