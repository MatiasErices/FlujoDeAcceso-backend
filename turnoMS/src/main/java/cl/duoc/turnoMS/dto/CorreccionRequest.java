package cl.duoc.turnoMS.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorreccionRequest {

    @NotNull(message = "Falta el id del supervisor")
    private Long supervisorId;

    @NotNull @Min(value = 0, message = "No se permiten valores negativos")
    private Integer hombres;

    @NotNull @Min(value = 0, message = "No se permiten valores negativos")
    private Integer mujeres;

    @NotNull @Min(value = 0, message = "No se permiten valores negativos")
    private Integer menores;

    @NotNull @Min(value = 0, message = "No se permiten valores negativos")
    private Integer sinClasificar;

    @NotNull @Min(value = 0, message = "No se permiten valores negativos")
    private Integer transito;
}
