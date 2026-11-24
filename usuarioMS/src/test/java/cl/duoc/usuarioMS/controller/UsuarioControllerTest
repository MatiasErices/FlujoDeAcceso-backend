package cl.duoc.usuarioMS.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import cl.duoc.usuarioMS.dto.LoginRequest;
import cl.duoc.usuarioMS.dto.UsuarioDTO;
import cl.duoc.usuarioMS.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioService service; // service simulado

    @InjectMocks
    private UsuarioController controller; // controller real

    @Test
    void login_correcto_devuelve200ConElRol() {
        LoginRequest request = new LoginRequest("supervisor@duoc.cl", "super1234");
        UsuarioDTO supervisora = new UsuarioDTO(3L, "Supervisora Demo", "supervisor@duoc.cl", "SUPERVISOR");
        when(service.login(request)).thenReturn(supervisora);

        ResponseEntity<UsuarioDTO> respuesta = controller.login(request);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("SUPERVISOR", respuesta.getBody().getRol());
    }

    @Test
    void login_incorrecto_dejaPasarLaExcepcionAlManejador() {
        LoginRequest request = new LoginRequest("camila.r@duoc.cl", "malaclave");
        when(service.login(request)).thenThrow(new SecurityException(UsuarioService.MENSAJE_LOGIN_INVALIDO));

        assertThrows(SecurityException.class, () -> controller.login(request));
    }

    @Test
    void listar_sinUsuarios_devuelve200ConListaVacia() {
        when(service.listar()).thenReturn(List.of());

        ResponseEntity<List<UsuarioDTO>> respuesta = controller.listar();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().isEmpty());
    }
}