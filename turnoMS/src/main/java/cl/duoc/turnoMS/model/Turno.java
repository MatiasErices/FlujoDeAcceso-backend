package cl.duoc.turnoMS.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del turno", example = "1")
    private Long id;

    // Id del usuario en usuarioMS: NO es clave foránea porque está en otra base de datos
    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "Id del empleado (usuarioMS)", example = "1")
    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Acceso acceso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private TipoTurno tipo;

    @Column(nullable = false, length = 10)
    @Schema(description = "Fecha AAAA-MM-DD", example = "2026-10-13")
    private String fecha;

    @Column(name = "hora_inicio_real", nullable = false, length = 5)
    private String horaInicioReal;

    @Column(name = "hora_fin_real", length = 5)
    @Schema(description = "null = turno abierto")
    private String horaFinReal;

    @Column(name = "foto_uri", length = 500)
    @Schema(description = "Dirección de la foto en el teléfono (la imagen no se sube)")
    private String fotoUri;

    @Column(nullable = false)
    private boolean exportado;

    @Column(nullable = false)
    @Schema(description = "true si un supervisor corrigió sus conteos")
    private boolean corregido;

    public boolean estaAbierto() {
        return horaFinReal == null;
    }
}
