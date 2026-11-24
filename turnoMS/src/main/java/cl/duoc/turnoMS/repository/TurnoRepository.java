package cl.duoc.turnoMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.turnoMS.model.Turno;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByFechaOrderByTipoAscAccesoAsc(String fecha);
}