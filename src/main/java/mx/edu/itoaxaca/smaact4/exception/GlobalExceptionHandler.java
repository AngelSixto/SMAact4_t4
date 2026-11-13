package mx.edu.itoaxaca.smaact4.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import mx.edu.itoaxaca.smaact4.dto.common.ErrorResponse;

/**
 * Convierte las excepciones en respuestas JSON con el codigo HTTP correcto.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400: fallo la validacion de @Valid (@NotBlank, @Email, @Min...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException ex,
                                                    HttpServletRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return construir(HttpStatus.BAD_REQUEST, "La validacion de los datos fallo", request, errores);
    }

    // 400: JSON mal escrito o con tipos incorrectos (ej. "semestre": "siete")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException ex,
                                                      HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo de la peticion no es un JSON valido o tiene tipos de dato incorrectos",
                request, null);
    }

    // 400: parametro de la URL con tipo incorrecto (ej. /api/alumnos/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tipoIncorrecto(MethodArgumentTypeMismatchException ex,
                                                        HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST,
                "El parametro '" + ex.getName() + "' tiene un valor invalido", request, null);
    }

    // 400: regla de negocio (ej. la carrera indicada no existe)
    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<ErrorResponse> solicitudInvalida(SolicitudInvalidaException ex,
                                                           HttpServletRequest request) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    // 401: email o password incorrectos en el login
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> credenciales(AuthenticationException ex,
                                                      HttpServletRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, "Email o password incorrectos", request, null);
    }

    // 404: el registro no existe
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(RecursoNoEncontradoException ex,
                                                      HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    // 404: la ruta no existe
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> rutaNoExiste(NoResourceFoundException ex,
                                                      HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, "La ruta solicitada no existe", request, null);
    }

    // 405: verbo HTTP no permitido en esa ruta
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> metodoNoPermitido(HttpRequestMethodNotSupportedException ex,
                                                           HttpServletRequest request) {
        return construir(HttpStatus.METHOD_NOT_ALLOWED,
                "El metodo " + ex.getMethod() + " no esta permitido en esta ruta", request, null);
    }

    // 409: duplicados o reglas que impiden la operacion
    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorResponse> conflicto(ConflictoException ex, HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> integridad(DataIntegrityViolationException ex,
                                                    HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT,
                "La operacion viola una restriccion de la base de datos (dato duplicado o relacionado)",
                request, null);
    }

    // 500: cualquier otro error no previsto
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> general(Exception ex, HttpServletRequest request) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error interno en el servidor",
                request, null);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje,
                                                    HttpServletRequest request,
                                                    Map<String, String> errores) {
        ErrorResponse body = ErrorResponse.of(status.value(), status.getReasonPhrase(), mensaje,
                request.getRequestURI(), errores);
        return ResponseEntity.status(status).body(body);
    }
}
