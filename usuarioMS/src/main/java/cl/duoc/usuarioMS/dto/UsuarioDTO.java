package cl.duoc.usuarioMS.dto;

import lombok.*;

/** Lo que se envía a la app y a turnoMS: nunca incluye el hash de la contraseña. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String correo;
    private String rol;   // "EMPLEADO" o "SUPERVISOR"
}