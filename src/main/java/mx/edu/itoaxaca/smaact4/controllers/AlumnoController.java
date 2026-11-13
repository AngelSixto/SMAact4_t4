package mx.edu.itoaxaca.smaact4.controllers;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import mx.edu.itoaxaca.smaact4.dto.alumno.AlumnoRequest;
import mx.edu.itoaxaca.smaact4.dto.alumno.AlumnoResponse;
import mx.edu.itoaxaca.smaact4.dto.common.PageResponse;
import mx.edu.itoaxaca.smaact4.service.AlumnoService;

/**
 * CRUD de alumnos en formato API REST (solo JSON). Todos los endpoints requieren JWT.
 */
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    // a) GET /api/alumnos?page=0&size=10&sort=apellidos,asc[&carreraId=1] -> 200 (paginado)
    @GetMapping
    public ResponseEntity<PageResponse<AlumnoResponse>> listar(
            @RequestParam(required = false) Long carreraId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(alumnoService.listar(carreraId, pageable));
    }

    // a) GET /api/alumnos/{id} -> 200 / 404
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoService.obtener(id));
    }

    // b) POST /api/alumnos -> 201 Created + header Location / 400 / 409
    @PostMapping
    public ResponseEntity<AlumnoResponse> crear(@Valid @RequestBody AlumnoRequest request) {
        AlumnoResponse creado = alumnoService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    // c) PUT /api/alumnos/{id} -> 200 / 400 / 404 / 409
    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponse> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody AlumnoRequest request) {
        return ResponseEntity.ok(alumnoService.actualizar(id, request));
    }

    // d) DELETE /api/alumnos/{id} -> 204 No Content / 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alumnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
