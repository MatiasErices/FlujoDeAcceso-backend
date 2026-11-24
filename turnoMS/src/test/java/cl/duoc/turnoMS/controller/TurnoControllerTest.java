package cl.duoc.turnoMS.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import cl.duoc.turnoMS.dto.TurnoDTO;
import cl.duoc.turnoMS.dto.TurnoRequest;
import cl.duoc.turnoMS.model.Acceso;
import cl.duoc.turnoMS.model.TipoTurno;
import cl.duoc.turnoMS.service.TurnoService;

@ExtendWith(MockitoExtension.class)
public class TurnoControllerTest {

    @Mock
    private TurnoService service;

    @InjectMocks
    private TurnoController controller;

    @Test
    void crear_devuelve201ConElTurnoCreado() {
        TurnoRequest request = new TurnoRequest(1L, Acceso.A1, TipoTurno.AM, "2026-11-24", "10:05", "");
        TurnoDTO creado = new TurnoDTO(9L, 1L, Acceso.A1, TipoTurno.AM, "2026-11-24", "10:05", null, "", false, false);
        when(service.crear(request)).thenReturn(creado);

        ResponseEntity<TurnoDTO> respuesta = controller.crear(request);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(9L, respuesta.getBody().getId());
    }

    @Test
    void eliminar_devuelve204() {
        doNothing().when(service).eliminar(1L);

        ResponseEntity<Void> respuesta = controller.eliminar(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(service, times(1)).eliminar(1L);
    }
}