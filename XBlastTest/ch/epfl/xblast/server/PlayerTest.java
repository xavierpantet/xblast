package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

public class PlayerTest {

    @Test
    public void returnErrorWhenConstruction (){
        Sq<LifeState> l = Sq.constant()
        
        Player p = new Player(PlayerID.PLAYER_1, );
        
        PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange
    }
    
    @Test
    public void statesForNextLifeWorksWithZeroLives(){
        Player p = new Player(PlayerID.PLAYER_1, 0, new Cell(2,3), 5, 3);
        Sq<LifeState> sq = p.statesForNextLife();
        for (int i = 0; i < 100; ++i) {
            //System.out.println(sq.head().state());
            //System.out.println(sq.head().lives());
            //System.out.println();
            sq = sq.tail();
        }
    }
    
    @Test
    public void statesForNextLifeWorksWithNonZeroLives(){
        Player p = new Player(PlayerID.PLAYER_1, 10, new Cell(2,3), 5, 3);
        Sq<LifeState> sq = p.statesForNextLife();
        for (int i = 0; i < 100; ++i) {
            //System.out.println(sq.head().state());
            //System.out.println(sq.head().lives());
            //System.out.println();
            sq = sq.tail();
        }
    }
    
    @Test
    public void isAliveMethodIsCorrect(){
        Player p = new Player(PlayerID.PLAYER_1, 10, new Cell(2,3), 5, 3);
        assertTrue(p.isAlive());
        Player p2 = new Player(PlayerID.PLAYER_1, 0, new Cell(2,3), 5, 3);
        assertFalse(p2.isAlive());
    }
    
    @Test
    public void isWithMaxBombsCorrect(){
        Player p = new Player(PlayerID.PLAYER_1, 10, new Cell(2,3), 5, 3);
        Player p2=p.withMaxBombs(20);
        Player p3 = new Player(PlayerID.PLAYER_1, 0, new Cell(2,3), 20, 3);
        assertEquals(p2.maxBombs(), p3.maxBombs());
    }
    
    @Test
    public void isWithBombRangeCorrect(){
        Player p = new Player(PlayerID.PLAYER_1, 10, new Cell(2,3), 5, 3);
        Player p2=p.withBombRange(30);
        Player p3 = new Player(PlayerID.PLAYER_1, 10, new Cell(2,3), 5, 30);
        assertEquals(p2.maxBombs(), p3.maxBombs());
    }
    
    @Test(expected=NullPointerException.class)
    public void directedPositionThrowsExceptionOnNullPosition(){
        DirectedPosition t = new Player.DirectedPosition(null, Direction.S);
    }
    
    @Test(expected=NullPointerException.class)
    public void directedPositionThrowsExceptionOnNullDirection(){
        DirectedPosition t = new Player.DirectedPosition(new SubCell(2, 3), null);
    }
    
    @Test
    public void stoppedStaticMethodWorks(){
        DirectedPosition dp = new Player.DirectedPosition(new SubCell(2, 3), Direction.S);
        Sq<DirectedPosition> sq = Player.DirectedPosition.stopped(dp);
        for (int i = 0; i < 100; ++i) {
            //System.out.println(sq.head().direction());
            //System.out.println(sq.head().position());
            //System.out.println();
            sq = sq.tail();
        }
    }
    
    @Test
    public void movingStaticMethodWorks(){
        DirectedPosition dp = new Player.DirectedPosition(new SubCell(2, 3), Direction.E);
        Sq<DirectedPosition> sq = Player.DirectedPosition.moving(dp);
        for (int i = 0; i < 100; ++i) {
            System.out.println(sq.head().direction());
            System.out.println(sq.head().position());
            System.out.println();
            sq = sq.tail();
        }
    }
}
