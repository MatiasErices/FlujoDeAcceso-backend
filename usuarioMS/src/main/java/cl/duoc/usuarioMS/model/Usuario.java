package cl.duoc.usuarioMS.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador del usuario", example = "1")
    private Long id;

    @Column(nullable = false, length = 80)
    @Schema(description = "Nombre visible", example = "Camila R.")
    private String nombre;

    @Column(nullable = false, unique = true, length = 120)
    @Schema(description = "Correo con el que inicia sesión", example = "camila.r@duoc.cl")
    private String correo;

    @Column(name = "clave_hash", nullable = false, length = 64)
    @Schema(description = "SHA-256 de la contraseña (nunca la contraseña real)")
    private String claveHash;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_usuario_id", nullable = false)
    @Schema(description = "Tipo de usuario: EMPLEADO o SUPERVISOR")
    private TipoUsuario tipoUsuario;
}