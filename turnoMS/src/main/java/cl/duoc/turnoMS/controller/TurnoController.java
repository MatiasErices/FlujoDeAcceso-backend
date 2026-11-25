package cl.duoc.turnoMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.turnoMS.dto.CerrarTurnoRequest;
import cl.duoc.turnoMS.dto.ConteoDTO;
import cl.duoc.turnoMS.dto.ConteoRequest;
import cl.duoc.turnoMS.dto.CorreccionRequest;
import cl.duoc.turnoMS.dto.TurnoDTO;
import cl.duoc.turnoMS.dto.TurnoRequest;
import cl.duoc.turnoMS.service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/turnos")
public class TurnoController {

    @Autowired
    private TurnoService service;

    @PostMapping
    @Operation(summary = "Crear turno", description = "El empleado inicia su turno. Responde 201.")
    public ResponseEntity<TurnoDTO> crear(@Valid @RequestBody TurnoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping
    @Operation(summary = "Turnos de un día", description = "Ej.: ?fecha=2026-10-13")
    public ResponseEntity<List<TurnoDTO>> listarPorFecha(@RequestParam("fecha") String fecha) {
        return ResponseEntity.ok(service.listarPorFecha(fecha));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar turno por ID")
    public ResponseEntity<TurnoDTO> buscar(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PutMapping("/{id}/cerrar")
    @Operation(summary = "Finalizar turno")
    public ResponseEntity<TurnoDTO> cerrar(@PathVariable("id") Long id, @Valid @RequestBody CerrarTurnoRequest request) {
        return ResponseEntity.ok(service.cerrar(id, request.getHoraFin()));
    }

    @PutMapping("/{id}/exportado")
    @Operation(summary = "Marcar turno como exportado")
    public ResponseEntity<TurnoDTO> marcarExportado(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.marcarExportado(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar turno", description = "También elimina sus conteos (cascada).")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/conteos")
    @Operation(summary = "Conteos de un turno")
    public ResponseEntity<List<ConteoDTO>> conteosDeTurno(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.conteosDeTurno(id));
    }

    @PutMapping("/{id}/conteos/{hora}")
    @Operation(summary = "Guardar conteo de una hora (empleado)", description = "Crea o actualiza. Solo con el turno abierto.")
    public ResponseEntity<ConteoDTO> guardarConteo(@PathVariable("id") Long id, @PathVariable("hora") int hora,
            @Valid @RequestBody ConteoRequest request) {
        return ResponseEntity.ok(service.guardarConteo(id, hora, request));
    }

    @PutMapping("/{id}/conteos/{hora}/correccion")
    @Operation(summary = "Corregir conteo (solo supervisor)", description = "Verifica el rol en usuarioMS y marca el turno como corregido.")
    public ResponseEntity<ConteoDTO> corregirConteo(@PathVariable("id") Long id, @PathVariable("hora") int hora,
            @Valid @RequestBody CorreccionRequest request) {
        return ResponseEntity.ok(service.corregirConteo(id, hora, request));
    }
}