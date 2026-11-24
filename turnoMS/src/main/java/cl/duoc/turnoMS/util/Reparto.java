package cl.duoc.turnoMS.util;

import java.util.Arrays;

/** Reparte un total en partes proporcionales; la última recibe lo que sobra (suma exacta). */
public final class Reparto {

    private Reparto() {
    }

    public static int[] repartir(int total, int[] pesos) {
        int sumaPesos = Arrays.stream(pesos).sum();
        int[] partes = new int[pesos.length];
        int asignado = 0;
        for (int i = 0; i < pesos.length - 1; i++) {
            partes[i] = total * pesos[i] / sumaPesos;
            asignado += partes[i];
        }
        partes[pesos.length - 1] = total - asignado;
        return partes;
    }
}