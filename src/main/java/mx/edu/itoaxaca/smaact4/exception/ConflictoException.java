package mx.edu.itoaxaca.smaact4.exception;

/** Se traduce a 409 Conflict (dato duplicado o regla que impide la operacion). */
public class ConflictoException extends RuntimeException {
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
