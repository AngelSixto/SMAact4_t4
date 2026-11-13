package mx.edu.itoaxaca.smaact4.dto.common;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Formato unico de error que regresa toda la API.
 * "errores" solo se llena cuando falla la validacion (campo -> mensaje).
 */
public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String mensaje,
        String ruta,
        Map<String, String> errores
) {
    public static ErrorResponse of(int status, String error, String mensaje, String ruta,
                                   Map<String, String> errores) {
        return new ErrorResponse(OffsetDateTime.now().toString(), status, error, mensaje, ruta, errores);
    }
}
