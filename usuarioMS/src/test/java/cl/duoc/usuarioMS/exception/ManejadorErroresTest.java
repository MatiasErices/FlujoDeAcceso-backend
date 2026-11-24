package cl.duoc.usuarioMS.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ManejadorErroresTest {

    private final ManejadorErrores manejador = new ManejadorErrores();

    @Test
    void securityException_seConvierteEn401() {
        ResponseEntity<Map<String, String>> r = manejador.credencialesInvalidas(new SecurityException("Correo o contraseña incorrectos"));

        assertEquals(HttpStatus.UNAUTHORIZED, r.getStatusCode());
        assertEquals("Correo o contraseña incorrectos", r.getBody().get("mensaje"));
    }

    @Test
    void noSuchElement_seConvierteEn404() {
        ResponseEntity<Map<String, String>> r = manejador.noEncontrado(new NoSuchElementException("Usuario no encontrado"));

        assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
    }
}