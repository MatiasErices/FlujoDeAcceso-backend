package cl.duoc.turnoMS.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.turnoMS.client.UsuarioClient;
import cl.duoc.turnoMS.dto.ConteoDTO;
import cl.duoc.turnoMS.dto.ConteoRequest;
import cl.duoc.turnoMS.dto.CorreccionRequest;
import cl.duoc.turnoMS.dto.TurnoDTO;
import cl.duoc.turnoMS.dto.TurnoRequest;
import cl.duoc.turnoMS.dto.UsuarioDTO;
import cl.duoc.turnoMS.exception.ServicioNoDisponibleException;
import cl.duoc.turnoMS.model.Conteo;
import cl.duoc.turnoMS.model.Turno;
import cl.duoc.turnoMS.repository.ConteoRepository;
import cl.duoc.turnoMS.repository.TurnoRepository;
import feign.FeignException;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private ConteoRepository conteoRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    // ---------- Turnos ----------

    public TurnoDTO crear(TurnoRequest request) {
        Turno turno = new Turno(null, request.getUsuarioId(), request.getAcceso(), request.getTipo(),
                request.getFecha(), request.getHoraInicioReal(), null, request.getFotoUri(), false, false);
        return aDTO(turnoRepository.save(turno));
    }

    public List<TurnoDTO> listarPorFecha(String fecha) {
        return turnoRepository.findByFechaOrderByTipoAscAccesoAsc(fecha).stream().map(this::aDTO).toList();
    }

    public TurnoDTO buscar(Long id) {
        return aDTO(obtenerTurno(id));
    }

    public TurnoDTO cerrar(Long id, String horaFin) {
        Turno turno = obtenerTurno(id);
        turno.setHoraFinReal(horaFin);
        return aDTO(turnoRepository.save(turno));
    }

    public TurnoDTO marcarExportado(Long id) {
        Turno turno = obtenerTurno(id);
        turno.setExportado(true);
        return aDTO(turnoRepository.save(turno));
    }

    public void eliminar(Long id) {
        turnoRepository.delete(obtenerTurno(id)); // MySQL borra sus conteos (ON DELETE CASCADE)
    }

    // ---------- Conteos ----------

    public List<ConteoDTO> conteosDeTurno(Long turnoId) {
        obtenerTurno(turnoId); // 404 si el turno no existe
        return conteoRepository.findByTurnoIdOrderByHoraAsc(turnoId).stream().map(this::aDTO).toList();
    }

    public List<ConteoDTO> conteosPorFecha(String fecha) {
        return conteoRepository.findByTurnoFechaOrderByHoraAsc(fecha).stream().map(this::aDTO).toList();
    }

    /** El EMPLEADO guarda una hora (la crea o la actualiza). Solo mientras el turno está abierto. */
    public ConteoDTO guardarConteo(Long turnoId, int hora, ConteoRequest r) {
        Turno turno = obtenerTurno(turnoId);
        if (!turno.estaAbierto()) {
            throw new IllegalStateException("El turno está cerrado: solo un supervisor puede corregirlo");
        }
        Conteo conteo = aplicarConteo(turno, hora,
                r.getHombres(), r.getMujeres(), r.getMenores(), r.getSinClasificar(), r.getTransito());
        return aDTO(conteo);
    }

    /** El SUPERVISOR corrige una hora de un turno ya guardado. */
    public ConteoDTO corregirConteo(Long turnoId, int hora, CorreccionRequest r) {
        verificarSupervisor(r.getSupervisorId());
        Turno turno = obtenerTurno(turnoId);

        Conteo conteo = aplicarConteo(turno, hora,
                r.getHombres(), r.getMujeres(), r.getMenores(), r.getSinClasificar(), r.getTransito());

        turno.setCorregido(true);
        turno.setExportado(false); // el CSV exportado antes ya no coincide con los datos
        turnoRepository.save(turno);
        return aDTO(conteo);
    }

    // ---------- Reglas ----------

    /** Mismas reglas que validarConteo de la app: sin negativos y tránsito <= ingresos. */
    public static void validarConteo(int hombres, int mujeres, int menores, int sinClasificar, int transito) {
        if (hombres < 0 || mujeres < 0 || menores < 0 || sinClasificar < 0 || transito < 0) {
            throw new IllegalArgumentException("No se permiten valores negativos");
        }
        if (transito > hombres + mujeres + menores + sinClasificar) {
            throw new IllegalArgumentException("Las personas en tránsito no pueden superar a las que ingresaron");
        }
    }

    private Conteo aplicarConteo(Turno turno, int hora, int hombres, int mujeres, int menores,
            int sinClasificar, int transito) {
        if (!turno.getTipo().incluyeHora(hora)) {
            throw new IllegalArgumentException("La hora " + hora + " no pertenece al turno " + turno.getTipo());
        }
        validarConteo(hombres, mujeres, menores, sinClasificar, transito);

        Conteo conteo = conteoRepository.findByTurnoIdAndHora(turno.getId(), hora).orElseGet(() -> {
            Conteo nuevo = new Conteo();
            nuevo.setTurno(turno);
            nuevo.setHora(hora);
            return nuevo;
        });
        conteo.setHombres(hombres);
        conteo.setMujeres(mujeres);
        conteo.setMenores(menores);
        conteo.setSinClasificar(sinClasificar);
        conteo.setTransito(transito);
        return conteoRepository.save(conteo);
    }

    /** Pregunta a usuarioMS (Feign) si el usuario es SUPERVISOR. */
    private void verificarSupervisor(Long usuarioId) {
        UsuarioDTO usuario;
        try {
            usuario = usuarioClient.obtenerUsuario(usuarioId);
        } catch (FeignException.NotFound e) {
            throw new SecurityException("El usuario " + usuarioId + " no existe");
        } catch (FeignException e) {
            throw new ServicioNoDisponibleException("No se pudo verificar el usuario: usuarioMS no responde");
        }
        if (!"SUPERVISOR".equals(usuario.getRol())) {
            throw new SecurityException("Solo un supervisor puede corregir turnos guardados");
        }
    }

    private Turno obtenerTurno(Long id) {
        return turnoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Turno no encontrado"));
    }

    private TurnoDTO aDTO(Turno t) {
        return new TurnoDTO(t.getId(), t.getUsuarioId(), t.getAcceso(), t.getTipo(), t.getFecha(),
                t.getHoraInicioReal(), t.getHoraFinReal(), t.getFotoUri(), t.isExportado(), t.isCorregido());
    }

    private ConteoDTO aDTO(Conteo c) {
        return new ConteoDTO(c.getTurno().getId(), c.getTurno().getAcceso(), c.getTurno().getFecha(),
                c.getHora(), c.getHombres(), c.getMujeres(), c.getMenores(), c.getSinClasificar(), c.getTransito());
    }
}