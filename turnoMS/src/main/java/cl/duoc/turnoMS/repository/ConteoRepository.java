package cl.duoc.turnoMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.turnoMS.model.Conteo;

@Repository
public interface ConteoRepository extends JpaRepository<Conteo, Long> {

    List<Conteo> findByTurnoIdOrderByHoraAsc(Long turnoId);

    Optional<Conteo> findByTurnoIdAndHora(Long turnoId, int hora);

    // "TurnoFecha" navega la relación: conteo.turno.fecha
    List<Conteo> findByTurnoFechaOrderByHoraAsc(String fecha);
}
