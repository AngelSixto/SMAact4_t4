package mx.edu.itoaxaca.smaact4.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import mx.edu.itoaxaca.smaact4.dto.carrera.CarreraRequest;
import mx.edu.itoaxaca.smaact4.dto.carrera.CarreraResponse;
import mx.edu.itoaxaca.smaact4.service.CarreraService;

/**
 * Catalogo de carreras (necesario para asignar la carrera a cada alumno). Requiere JWT.
 */
@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public ResponseEntity<List<CarreraResponse>> listar() {
        return ResponseEntity.ok(carreraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarreraResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(carreraService.obtener(id));
    }

    @PostMapping
    public ResponseEntity<CarreraResponse> crear(@Valid @RequestBody CarreraRequest request) {
        CarreraResponse creada = carreraService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarreraResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody CarreraRequest request) {
        return ResponseEntity.ok(carreraService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        carreraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
