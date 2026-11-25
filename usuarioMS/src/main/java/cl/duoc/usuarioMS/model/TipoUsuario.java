package cl.duoc.usuarioMS.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del tipo de usuario", example = "1")
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    @Schema(description = "Nombre del tipo de usuario", example = "EMPLEADO")
    private String nombre;
}