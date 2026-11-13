package mx.edu.itoaxaca.smaact4.exception;

/** Se traduce a 400 Bad Request (datos validos en formato pero incorrectos para el negocio). */
public class SolicitudInvalidaException extends RuntimeException {
    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }
}
