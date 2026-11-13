package mx.edu.itoaxaca.smaact4.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.itoaxaca.smaact4.models.entity.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    // Paginado: SELECT ... WHERE carrera_id = ? LIMIT ? OFFSET ?
    Page<Alumno> findByCarreraId(Long carreraId, Pageable pageable);

    boolean existsByCarreraId(Long carreraId);

    boolean existsByNumeroControl(String numeroControl);

    boolean existsByNumeroControlAndIdNot(String numeroControl, Long id);
}
