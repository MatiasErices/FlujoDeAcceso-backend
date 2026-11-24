package cl.duoc.turnoMS.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.duoc.turnoMS.dto.UsuarioDTO;

/** Llama a usuarioMS por HTTP (comunicación entre microservicios). */
@FeignClient(name = "usuarioMS", url = "${usuario-ms.url}")
public interface UsuarioClient {

    @GetMapping("/api/v1/usuarios/dto/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable("id") Long id);
}