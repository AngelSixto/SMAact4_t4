package mx.edu.itoaxaca.smaact4.dto.alumno;

import mx.edu.itoaxaca.smaact4.dto.carrera.CarreraResponse;
import mx.edu.itoaxaca.smaact4.models.entity.Alumno;

/**
 * DTO de SALIDA de un alumno. Incluye su carrera (la relacion) como objeto.
 */
public record AlumnoResponse(
        Long id,
        String numeroControl,
        String nombre,
        String apellidos,
        String email,
        Integer semestre,
        CarreraResponse carrera
) {
    public static AlumnoResponse from(Alumno alumno) {
        return new AlumnoResponse(
                alumno.getId(),
                alumno.getNumeroControl(),
                alumno.getNombre(),
                alumno.getApellidos(),
                alumno.getEmail(),
                alumno.getSemestre(),
                CarreraResponse.from(alumno.getCarrera()));
    }
}
