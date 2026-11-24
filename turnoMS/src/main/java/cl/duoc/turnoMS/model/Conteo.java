package cl.duoc.turnoMS.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conteo", uniqueConstraints = @UniqueConstraint(columnNames = { "turno_id", "hora" }))
public class Conteo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "turno_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // al borrar el turno, MySQL borra sus conteos
    private Turno turno;

    @Column(nullable = false)
    private int hora;

    private int hombres;
    private int mujeres;
    private int menores;

    @Column(name = "sin_clasificar")
    private int sinClasificar;

    private int transito;

    /** El tránsito NO se suma: ya está incluido en los ingresos. */
    public int totalIngresos() {
        return hombres + mujeres + menores + sinClasificar;
    }
}