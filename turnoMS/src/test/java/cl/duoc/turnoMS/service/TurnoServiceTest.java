package cl.duoc.turnoMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.turnoMS.client.UsuarioClient;
import cl.duoc.turnoMS.dto.ConteoDTO;
import cl.duoc.turnoMS.dto.ConteoRequest;
import cl.duoc.turnoMS.dto.CorreccionRequest;
import cl.duoc.turnoMS.dto.TurnoDTO;
import cl.duoc.turnoMS.dto.TurnoRequest;
import cl.duoc.turnoMS.dto.UsuarioDTO;
import cl.duoc.turnoMS.model.Acceso;
import cl.duoc.turnoMS.model.Conteo;
import cl.duoc.turnoMS.model.TipoTurno;
import cl.duoc.turnoMS.model.Turno;
import cl.duoc.turnoMS.repository.ConteoRepository;
import cl.duoc.turnoMS.repository.TurnoRepository;

@ExtendWith(MockitoExtension.class)
public class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;   // simulado: no toca MySQL

    @Mock
    private ConteoRepository conteoRepository; // simulado

    @Mock
    private UsuarioClient usuarioClient;       // simulado: no llama a usuarioMS

    @InjectMocks
    private TurnoService service;              // real

    private Turno turnoAbierto;

    @BeforeEach
    void setUp() {
        turnoAbierto = new Turno(1L, 1L, Acceso.A1, TipoTurno.AM, "2026-10-13", "10:00", null, "", false, false);
    }

    @Test
    void crear_guardaElTurnoAbiertoYSinExportar() {
        when(turnoRepository.save(any(Turno.class))).thenAnswer(inv -> {
            Turno t = inv.getArgument(0);
            t.setId(10L); // simula el id que asigna MySQL
            return t;
        });

        TurnoDTO r = service.crear(new TurnoRequest(1L, Acceso.A1, TipoTurno.AM, "2026-11-24", "10:05", "content://foto"));

        assertEquals(10L, r.getId());
        assertNull(r.getHoraFinReal());
        assertFalse(r.isExportado());
    }

    @Test
    void guardarConteo_valido_creaLaFilaDeEsaHora() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turnoAbierto));
        when(conteoRepository.findByTurnoIdAndHora(1L, 10)).thenReturn(Optional.empty());
        when(conteoRepository.save(any(Conteo.class))).thenAnswer(inv -> inv.getArgument(0));

        ConteoDTO r = service.guardarConteo(1L, 10, new ConteoRequest(30, 36, 10, 2, 14));

        assertEquals(30, r.getHombres());
        assertEquals(14, r.getTransito());
        assertEquals(Acceso.A1, r.getAcceso());
    }

    @Test
    void guardarConteo_transitoMayorQueIngresos_lanzaIllegalArgument() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turnoAbierto));

        assertThrows(IllegalArgumentException.class,
                () -> service.guardarConteo(1L, 10, new ConteoRequest(2, 1, 0, 0, 5)));

        verify(conteoRepository, never()).save(any()); // no se guardó nada
    }

    @Test
    void guardarConteo_horaFueraDelTurno_lanzaIllegalArgument() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turnoAbierto));

        assertThrows(IllegalArgumentException.class,
                () -> service.guardarConteo(1L, 16, new ConteoRequest(1, 0, 0, 0, 0))); // 16:00 no es AM
    }

    @Test
    void guardarConteo_enTurnoCerrado_lanzaIllegalState() {
        turnoAbierto.setHoraFinReal("15:00");
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turnoAbierto));

        assertThrows(IllegalStateException.class,
                () -> service.guardarConteo(1L, 10, new ConteoRequest(1, 0, 0, 0, 0)));
    }

    @Test
    void corregirConteo_porUnEmpleado_lanzaSecurityException() {
        when(usuarioClient.obtenerUsuario(1L))
                .thenReturn(new UsuarioDTO(1L, "Camila R.", "camila.r@duoc.cl", "EMPLEADO"));

        assertThrows(SecurityException.class,
                () -> service.corregirConteo(1L, 10, new CorreccionRequest(1L, 30, 36, 10, 2, 14)));

        verify(turnoRepository, never()).save(any());
    }

    @Test
    void corregirConteo_porSupervisor_marcaCorregidoYPendienteDeExportar() {
        turnoAbierto.setHoraFinReal("15:00");
        turnoAbierto.setExportado(true);
        Conteo existente = new Conteo(5L, turnoAbierto, 10, 31, 36, 10, 2, 14);

        when(usuarioClient.obtenerUsuario(3L))
                .thenReturn(new UsuarioDTO(3L, "Supervisora Demo", "supervisor@duoc.cl", "SUPERVISOR"));
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turnoAbierto));
        when(conteoRepository.findByTurnoIdAndHora(1L, 10)).thenReturn(Optional.of(existente));
        when(conteoRepository.save(any(Conteo.class))).thenAnswer(inv -> inv.getArgument(0));

        ConteoDTO r = service.corregirConteo(1L, 10, new CorreccionRequest(3L, 30, 36, 10, 2, 14));

        assertEquals(30, r.getHombres());       // se corrigió de 31 a 30
        assertTrue(turnoAbierto.isCorregido());
        assertFalse(turnoAbierto.isExportado()); // hay que volver a exportar
        verify(turnoRepository).save(turnoAbierto);
    }

    @Test
    void eliminar_turnoInexistente_lanzaNoSuchElement() {
        when(turnoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.eliminar(99L));
    }
}
