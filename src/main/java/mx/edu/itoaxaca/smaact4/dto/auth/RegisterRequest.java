package mx.edu.itoaxaca.smaact4.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de ENTRADA para POST /api/auth/register.
 */
public record RegisterRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede tener mas de 100 caracteres")
        String nombre,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        @Size(max = 120, message = "El email no puede tener mas de 120 caracteres")
        String email,

        @NotBlank(message = "El password es obligatorio")
        @Size(min = 8, max = 64, message = "El password debe tener entre 8 y 64 caracteres")
        String password
) {
}
