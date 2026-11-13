package mx.edu.itoaxaca.smaact4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.itoaxaca.smaact4.models.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
