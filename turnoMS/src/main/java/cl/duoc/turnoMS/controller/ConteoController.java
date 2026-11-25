package cl.duoc.turnoMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.turnoMS.dto.ConteoDTO;
import cl.duoc.turnoMS.service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/conteos")
public class ConteoController {

    @Autowired
    private TurnoService service;

    @GetMapping
    @Operation(summary = "Conteos de un día", description = "Para el panel del supervisor. Ej.: ?fecha=2026-10-13")
    public ResponseEntity<List<ConteoDTO>> porFecha(@RequestParam("fecha") String fecha) {
        return ResponseEntity.ok(service.conteosPorFecha(fecha));
    }
}
