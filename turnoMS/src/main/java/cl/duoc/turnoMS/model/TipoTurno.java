package cl.duoc.turnoMS.model;

/** Turnos con su horario: inicio incluido y fin excluido (igual que en la app). */
public enum TipoTurno {

    AM(10, 15),
    PM(15, 20);

    private final int horaInicio;
    private final int horaFin;

    TipoTurno(int horaInicio, int horaFin) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public boolean incluyeHora(int hora) {
        return hora >= horaInicio && hora < horaFin;
    }
}