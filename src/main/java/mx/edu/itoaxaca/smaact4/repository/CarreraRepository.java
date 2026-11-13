package mx.edu.itoaxaca.smaact4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.itoaxaca.smaact4.models.entity.Carrera;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    boolean existsByClaveIgnoreCase(String clave);

    boolean existsByClaveIgnoreCaseAndIdNot(String clave, Long id);
}
