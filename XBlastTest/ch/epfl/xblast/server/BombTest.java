package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

public class BombTest {

    @Test
    public void firstConstructorIsCorrect(){
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(3,5), Sq.iterate(4,  u -> u-1).limit(4), 5);
        //System.out.println(b.fuseLengths());
        assertTrue(true);
    }
    
    @Test
    public void secondConstructorIsCorrect(){
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(3,5), 4, 5);
        //System.out.println(b.range());
        assertTrue(true);
    }
    
    @Test(expected=NullPointerException.class)
    public void firstConstructorThrowsNullPointerException(){
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(3,5), null, 5);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void firstCconstructorThrowsIllegalArgumentException(){
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(3,5), Sq.iterate(4,  u -> u-1).limit(4), -3);
    }
    
    @Test(expected=NullPointerException.class)
    public void secondConstructorThrowsNullPointerException(){
        Bomb b = new Bomb(PlayerID.PLAYER_1, null, 4, 5);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void secondCconstructorThrowsIllegalArgumentException(){
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(3,5), 1, -2);
    }
    
    @Test
    public void explosionMethodIsCorrect(){
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(2,3), 10, 3);
        Sq<Sq<Cell>> sqFinal=b.explosionArmTowards(Direction.S);
        for (int i = 0; i < Ticks.EXPLOSION_TICKS; i++) {
            Sq<Cell> p = sqFinal.head();
            for(int j=0; j<b.range(); j++){
                System.out.print(p.head()+ ", ");
                p = p.tail();
            }
            System.out.println();
            sqFinal=sqFinal.tail();
        }
    }

}
