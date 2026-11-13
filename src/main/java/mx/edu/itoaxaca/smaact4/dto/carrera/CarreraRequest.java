package mx.edu.itoaxaca.smaact4.dto.carrera;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de ENTRADA para crear o actualizar una carrera.
 */
public record CarreraRequest(

        @NotBlank(message = "La clave es obligatoria")
        @Size(max = 10, message = "La clave no puede tener mas de 10 caracteres")
        String clave,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede tener mas de 100 caracteres")
        String nombre
) {
}
