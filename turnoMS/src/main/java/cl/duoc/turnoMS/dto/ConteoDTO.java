package cl.duoc.turnoMS.dto;

import cl.duoc.turnoMS.model.Acceso;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConteoDTO {

    private Long turnoId;
    private Acceso acceso;
    private String fecha;
    private int hora;
    private int hombres;
    private int mujeres;
    private int menores;
    private int sinClasificar;
    private int transito;
}