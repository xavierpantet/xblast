package ch.epfl.xblast.server.graphics;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExplosionPainterTest {

    @Test
    public void byteForBlastWorksCorrectly() {
        
        assertEquals(0b1110, ExplosionPainter.byteForBlast(true, true, true, false));
        assertEquals(0b1111, ExplosionPainter.byteForBlast(true, true, true, true));
        assertEquals(0b1001, ExplosionPainter.byteForBlast(true, false, false, true));
    }

}
