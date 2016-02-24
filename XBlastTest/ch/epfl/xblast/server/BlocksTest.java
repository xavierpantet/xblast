package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlocksTest {

    @Test
    public void generalTest() {
        Block free = Block.FREE;
        Block desWall = Block.DESTRUCTIBLE_WALL;
        Block indesWall = Block.INDESTRUCTIBLE_WALL;
        Block crumWall = Block.CRUMBLING_WALL;
        
        assertTrue(free.isFree());
        assertFalse(desWall.isFree());
        assertFalse(indesWall.isFree());
        assertFalse(crumWall.isFree());
        
        assertTrue(free.canHostPlayer());
        assertFalse(desWall.canHostPlayer());
        assertFalse(indesWall.canHostPlayer());
        assertFalse(crumWall.canHostPlayer());
        
        assertFalse(free.castsShadow());
        assertTrue(desWall.castsShadow());
        assertTrue(indesWall.castsShadow());
        assertTrue(crumWall.castsShadow());
    }

}
