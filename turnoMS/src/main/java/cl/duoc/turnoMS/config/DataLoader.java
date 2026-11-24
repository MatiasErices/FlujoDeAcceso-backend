package cl.duoc.turnoMS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.duoc.turnoMS.model.Acceso;
import cl.duoc.turnoMS.model.Conteo;
import cl.duoc.turnoMS.model.TipoTurno;
import cl.duoc.turnoMS.model.Turno;
import cl.duoc.turnoMS.repository.ConteoRepository;
import cl.duoc.turnoMS.repository.TurnoRepository;
import cl.duoc.turnoMS.util.Reparto;

@Component
public class DataLoader implements CommandLineRunner {

    private static final String FECHA_DEMO = "2026-10-13";
    private static final int[] PESOS_POR_HORA = { 16, 20, 24, 22, 18 }; // % de cada hora del turno

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private ConteoRepository conteoRepository;

    @Override
    public void run(String... args) throws Exception {

        if (turnoRepository.count() == 0) {
            // usuarioId, acceso, turno, {hombres, mujeres, menores, sinClasificar, tránsito}, exportado
            crearTurnoDemo(1L, Acceso.A1, TipoTurno.AM, new int[] { 210, 246, 71, 13, 128 }, true);
            crearTurnoDemo(4L, Acceso.A2, TipoTurno.AM, new int[] { 148, 173, 50, 9, 54 }, true);
            crearTurnoDemo(5L, Acceso.A3, TipoTurno.AM, new int[] { 117, 136, 40, 7, 28 }, true);
            crearTurnoDemo(6L, Acceso.A4, TipoTurno.AM, new int[] { 307, 360, 104, 19, 283 }, true);
            crearTurnoDemo(2L, Acceso.A1, TipoTurno.PM, new int[] { 272, 319, 92, 17, 165 }, true);
            crearTurnoDemo(7L, Acceso.A2, TipoTurno.PM, new int[] { 187, 219, 63, 11, 68 }, true);
            crearTurnoDemo(8L, Acceso.A3, TipoTurno.PM, new int[] { 121, 141, 41, 7, 29 }, false);
            crearTurnoDemo(9L, Acceso.A4, TipoTurno.PM, new int[] { 288, 336, 99, 17, 265 }, false);

            System.out.println("✅ turnoMS — 8 turnos y 40 conteos del 13-oct cargados");
        } else {
            System.out.println("ℹ️ turnoMS — los datos ya existían, no se insertaron");
        }
    }

    private void crearTurnoDemo(Long usuarioId, Acceso acceso, TipoTurno tipo, int[] totales, boolean exportado) {
        Turno turno = turnoRepository.save(new Turno(null, usuarioId, acceso, tipo, FECHA_DEMO,
                tipo.getHoraInicio() + ":00", tipo.getHoraFin() + ":00", "", exportado, false));

        // Reparte cada categoría entre las 5 horas del turno
        int[][] porCategoria = new int[5][];
        for (int c = 0; c < 5; c++) {
            porCategoria[c] = Reparto.repartir(totales[c], PESOS_POR_HORA);
        }

        for (int i = 0; i < PESOS_POR_HORA.length; i++) {
            conteoRepository.save(new Conteo(null, turno, tipo.getHoraInicio() + i,
                    porCategoria[0][i], porCategoria[1][i], porCategoria[2][i],
                    porCategoria[3][i], porCategoria[4][i]));
        }
    }
}
