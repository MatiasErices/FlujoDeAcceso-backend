package cl.duoc.usuarioMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.usuarioMS.model.TipoUsuario;
import cl.duoc.usuarioMS.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/tipos-usuario")
public class TipoUsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    @Operation(summary = "Listar tipos de usuario", description = "Catálogo: EMPLEADO y SUPERVISOR.")
    public ResponseEntity<List<TipoUsuario>> listar() {
        return ResponseEntity.ok(service.listarTipos());
    }
}