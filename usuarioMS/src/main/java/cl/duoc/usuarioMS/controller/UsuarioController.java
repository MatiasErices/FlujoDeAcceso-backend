package cl.duoc.usuarioMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.usuarioMS.dto.LoginRequest;
import cl.duoc.usuarioMS.dto.UsuarioDTO;
import cl.duoc.usuarioMS.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios (sin contraseñas).")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        // Siempre 200 con una lista (aunque esté vacía): la app espera una lista, no un 204
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/dto/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Lo usa turnoMS (Feign) para verificar el rol.")
    public ResponseEntity<UsuarioDTO> obtenerDTO(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "200 con el usuario y su rol, o 401 si las credenciales no coinciden.")
    public ResponseEntity<UsuarioDTO> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}