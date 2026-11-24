package cl.duoc.turnoMS.exception;

/** usuarioMS no respondió: no se puede verificar el rol del supervisor. */
public class ServicioNoDisponibleException extends RuntimeException {

    public ServicioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
