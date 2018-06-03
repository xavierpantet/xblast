package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.NoSuchElementException;

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
        
        assertFalse(free.isBonus());
        assertFalse(desWall.isBonus());
        assertFalse(indesWall.isBonus());
        assertFalse(crumWall.isBonus());
    }
    
    @Test(expected=NoSuchElementException.class)
    public void nullBonusThrowsException(){
        Block free = Block.FREE;
        Block desWall = Block.DESTRUCTIBLE_WALL;
        Block indesWall = Block.INDESTRUCTIBLE_WALL;
        Block crumWall = Block.CRUMBLING_WALL;
        
        crumWall.associatedBonus();
    }
    
    @Test
    public void associatedBonusWorks(){
        Block b = Block.BONUS_BOMB;
        Block r = Block.BONUS_RANGE;
        
        assertEquals(b.associatedBonus(), Bonus.INC_BOMB);
        assertEquals(r.associatedBonus(), Bonus.INC_RANGE);
    }
    
    @Test
    public void BlocksAndBonusTest(){
        Block b = Block.BONUS_BOMB;
        Block r = Block.BONUS_RANGE;
        
        assertTrue(b.isBonus());
        assertTrue(r.isBonus());
        
        assertTrue(b.canHostPlayer());
        assertTrue(r.canHostPlayer());
    }

}
