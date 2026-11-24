package cl.duoc.turnoMS.dto;

import cl.duoc.turnoMS.model.Acceso;
import cl.duoc.turnoMS.model.TipoTurno;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoDTO {

    private Long id;
    private Long usuarioId;
    private Acceso acceso;
    private TipoTurno tipo;
    private String fecha;
    private String horaInicioReal;
    private String horaFinReal;
    private String fotoUri;
    private boolean exportado;
    private boolean corregido;
}