package ch.epfl.xblast.server;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;

public class PlayerTest {

    @Test
    public void returnErrorWhenConstruction (){
        Sq<LifeState> l = Sq.constant()
        
        Player p = new Player(PlayerID.PLAYER_1, );
        
        PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange
    }
}
