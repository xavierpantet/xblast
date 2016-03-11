package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;


import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

public class PlayerTest {

    @Test
    public void constrctorOK(){
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(3, State.INVULNERABLE)), Sq.constant(new Player.DirectedPosition(new SubCell(3,4), Direction.N)), 3, 3);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void consrtuctionWithNegativeBombNumberReturnError(){
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(0, State.INVULNERABLE)), Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.N)), -1, 2);
    }

    @Test (expected=IllegalArgumentException.class)
    public void consrtuctionWithNegativeBombSpreadReturnError(){
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(0, State.INVULNERABLE)), Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.N)), 1, -2);
    }
    @Test (expected=NullPointerException.class)
    public void consrtuctionWithnullDirectedPosReturnError(){
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(0, State.INVULNERABLE)), null, 1, 2);
    }
    @Test (expected=IllegalArgumentException.class)
    public void consrtuctionWithNegativePositionReturnError(){
        Player p = new Player(PlayerID.PLAYER_1, Sq.constant(new Player.LifeState(0, State.INVULNERABLE)), Sq.constant(new Player.DirectedPosition(new SubCell(-1,-4), Direction.N)), -1, 2);
    }
    @Test (expected=NullPointerException.class)
    public void consrtuctionWithNullIDReturnError(){
        Player p = new Player(null, Sq.constant(new Player.LifeState(0, State.INVULNERABLE)), Sq.constant(new Player.DirectedPosition(new SubCell(-1,-4), Direction.N)), -1, 2);
    }
    
   
}
