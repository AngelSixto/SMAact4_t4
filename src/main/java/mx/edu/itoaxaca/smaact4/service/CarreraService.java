package mx.edu.itoaxaca.smaact4.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.itoaxaca.smaact4.dto.carrera.CarreraRequest;
import mx.edu.itoaxaca.smaact4.dto.carrera.CarreraResponse;
import mx.edu.itoaxaca.smaact4.exception.ConflictoException;
import mx.edu.itoaxaca.smaact4.exception.RecursoNoEncontradoException;
import mx.edu.itoaxaca.smaact4.models.entity.Carrera;
import mx.edu.itoaxaca.smaact4.repository.AlumnoRepository;
import mx.edu.itoaxaca.smaact4.repository.CarreraRepository;

@Service
public class CarreraService {

    private final CarreraRepository carreraRepository;
    private final AlumnoRepository alumnoRepository;

    public CarreraService(CarreraRepository carreraRepository, AlumnoRepository alumnoRepository) {
        this.carreraRepository = carreraRepository;
        this.alumnoRepository = alumnoRepository;
    }

    @Transactional(readOnly = true)
    public List<CarreraResponse> listar() {
        return carreraRepository.findAll().stream().map(CarreraResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CarreraResponse obtener(Long id) {
        return CarreraResponse.from(buscar(id));
    }

    @Transactional
    public CarreraResponse crear(CarreraRequest request) {
        String clave = request.clave().trim().toUpperCase();
        if (carreraRepository.existsByClaveIgnoreCase(clave)) {
            throw new ConflictoException("Ya existe una carrera con la clave " + clave);
        }
        Carrera carrera = new Carrera();
        carrera.setClave(clave);
        carrera.setNombre(request.nombre().trim());
        return CarreraResponse.from(carreraRepository.save(carrera));
    }

    @Transactional
    public CarreraResponse actualizar(Long id, CarreraRequest request) {
        Carrera carrera = buscar(id);
        String clave = request.clave().trim().toUpperCase();
        if (carreraRepository.existsByClaveIgnoreCaseAndIdNot(clave, id)) {
            throw new ConflictoException("Ya existe otra carrera con la clave " + clave);
        }
        carrera.setClave(clave);
        carrera.setNombre(request.nombre().trim());
        return CarreraResponse.from(carreraRepository.save(carrera));
    }

    @Transactional
    public void eliminar(Long id) {
        Carrera carrera = buscar(id);
        if (alumnoRepository.existsByCarreraId(id)) {
            throw new ConflictoException("No se puede eliminar la carrera porque tiene alumnos inscritos");
        }
        carreraRepository.delete(carrera);
    }

    private Carrera buscar(Long id) {
        return carreraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la carrera con id " + id));
    }
}
