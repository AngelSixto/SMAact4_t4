package mx.edu.itoaxaca.smaact4.dto.alumno;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO de ENTRADA para crear (POST) o actualizar (PUT) un alumno.
 * De la carrera solo se recibe su id.
 */
public record AlumnoRequest(

        @NotBlank(message = "El numero de control es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "El numero de control debe tener exactamente 8 digitos")
        String numeroControl,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 60, message = "El nombre no puede tener mas de 60 caracteres")
        String nombre,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 80, message = "Los apellidos no pueden tener mas de 80 caracteres")
        String apellidos,

        @Email(message = "El email no tiene un formato valido")
        @Size(max = 120, message = "El email no puede tener mas de 120 caracteres")
        String email,

        @NotNull(message = "El semestre es obligatorio")
        @Min(value = 1, message = "El semestre minimo es 1")
        @Max(value = 14, message = "El semestre maximo es 14")
        Integer semestre,

        @NotNull(message = "El id de la carrera es obligatorio")
        @Positive(message = "El id de la carrera debe ser positivo")
        Long carreraId
) {
}
