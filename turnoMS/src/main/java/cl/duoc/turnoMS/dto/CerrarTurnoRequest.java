package cl.duoc.turnoMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CerrarTurnoRequest {

    @NotBlank(message = "La hora de cierre es obligatoria")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "La hora debe tener formato HH:mm")
    private String horaFin;
}
