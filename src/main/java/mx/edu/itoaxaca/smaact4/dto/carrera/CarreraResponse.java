package mx.edu.itoaxaca.smaact4.dto.carrera;

import mx.edu.itoaxaca.smaact4.models.entity.Carrera;

/**
 * DTO de SALIDA de una carrera.
 */
public record CarreraResponse(
        Long id,
        String clave,
        String nombre
) {
    public static CarreraResponse from(Carrera carrera) {
        return new CarreraResponse(carrera.getId(), carrera.getClave(), carrera.getNombre());
    }
}
