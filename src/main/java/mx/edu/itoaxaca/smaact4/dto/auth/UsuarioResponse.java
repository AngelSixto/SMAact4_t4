package mx.edu.itoaxaca.smaact4.dto.auth;

import java.time.LocalDateTime;

import mx.edu.itoaxaca.smaact4.models.entity.Usuario;

/**
 * DTO de SALIDA de un usuario. NO incluye el password.
 */
public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        String rol,
        LocalDateTime fechaRegistro
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.getFechaRegistro());
    }
}
