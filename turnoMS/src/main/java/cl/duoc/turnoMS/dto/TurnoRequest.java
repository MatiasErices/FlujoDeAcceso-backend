package cl.duoc.turnoMS.dto;

import cl.duoc.turnoMS.model.Acceso;
import cl.duoc.turnoMS.model.TipoTurno;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequest {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "Selecciona el acceso asignado")
    private Acceso acceso;

    @NotNull(message = "Selecciona el turno")
    private TipoTurno tipo;

    @NotBlank(message = "La fecha es obligatoria")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato AAAA-MM-DD")
    private String fecha;

    @NotBlank(message = "La hora de inicio es obligatoria")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "La hora debe tener formato HH:mm")
    private String horaInicioReal;

    private String fotoUri;
}