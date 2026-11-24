package cl.duoc.turnoMS.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class RepartoTest {

    @Test
    void repartir_noPierdePersonasAlRedondear() {
        assertArrayEquals(new int[] { 3, 3, 4 }, Reparto.repartir(10, new int[] { 1, 1, 1 }));
        assertEquals(540, Arrays.stream(Reparto.repartir(540, new int[] { 16, 20, 24, 22, 18 })).sum());
    }
}
