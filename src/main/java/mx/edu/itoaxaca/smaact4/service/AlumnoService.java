package mx.edu.itoaxaca.smaact4.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.itoaxaca.smaact4.dto.alumno.AlumnoRequest;
import mx.edu.itoaxaca.smaact4.dto.alumno.AlumnoResponse;
import mx.edu.itoaxaca.smaact4.dto.common.PageResponse;
import mx.edu.itoaxaca.smaact4.exception.ConflictoException;
import mx.edu.itoaxaca.smaact4.exception.RecursoNoEncontradoException;
import mx.edu.itoaxaca.smaact4.exception.SolicitudInvalidaException;
import mx.edu.itoaxaca.smaact4.models.entity.Alumno;
import mx.edu.itoaxaca.smaact4.models.entity.Carrera;
import mx.edu.itoaxaca.smaact4.repository.AlumnoRepository;
import mx.edu.itoaxaca.smaact4.repository.CarreraRepository;

/**
 * Logica de negocio del CRUD de alumnos. Recibe y regresa DTOs, nunca entidades.
 */
@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final CarreraRepository carreraRepository;

    public AlumnoService(AlumnoRepository alumnoRepository, CarreraRepository carreraRepository) {
        this.alumnoRepository = alumnoRepository;
        this.carreraRepository = carreraRepository;
    }

    // ---------- LEER (paginado) ----------
    @Transactional(readOnly = true)
    public PageResponse<AlumnoResponse> listar(Long carreraId, Pageable pageable) {
        Page<Alumno> pagina = (carreraId == null)
                ? alumnoRepository.findAll(pageable)
                : alumnoRepository.findByCarreraId(carreraId, pageable);
        return PageResponse.from(pagina.map(AlumnoResponse::from));
    }

    // ---------- LEER (uno) ----------
    @Transactional(readOnly = true)
    public AlumnoResponse obtener(Long id) {
        return AlumnoResponse.from(buscar(id));
    }

    // ---------- CREAR ----------
    @Transactional
    public AlumnoResponse crear(AlumnoRequest request) {
        if (alumnoRepository.existsByNumeroControl(request.numeroControl())) {
            throw new ConflictoException("Ya existe un alumno con el numero de control " + request.numeroControl());
        }
        Alumno alumno = new Alumno();
        copiarDatos(request, alumno);
        return AlumnoResponse.from(alumnoRepository.save(alumno));
    }

    // ---------- ACTUALIZAR ----------
    @Transactional
    public AlumnoResponse actualizar(Long id, AlumnoRequest request) {
        Alumno alumno = buscar(id);
        if (alumnoRepository.existsByNumeroControlAndIdNot(request.numeroControl(), id)) {
            throw new ConflictoException("Ya existe otro alumno con el numero de control " + request.numeroControl());
        }
        copiarDatos(request, alumno);
        return AlumnoResponse.from(alumnoRepository.save(alumno));
    }

    // ---------- ELIMINAR ----------
    @Transactional
    public void eliminar(Long id) {
        alumnoRepository.delete(buscar(id));
    }

    private Alumno buscar(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el alumno con id " + id));
    }

    // Pasa los datos del DTO a la entidad y asigna la carrera real (relacion)
    private void copiarDatos(AlumnoRequest request, Alumno alumno) {
        Carrera carrera = carreraRepository.findById(request.carreraId())
                .orElseThrow(() -> new SolicitudInvalidaException(
                        "La carrera con id " + request.carreraId() + " no existe"));
        alumno.setNumeroControl(request.numeroControl());
        alumno.setNombre(request.nombre().trim());
        alumno.setApellidos(request.apellidos().trim());
        alumno.setEmail(request.email());
        alumno.setSemestre(request.semestre());
        alumno.setCarrera(carrera);
    }
}
