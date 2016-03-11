package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.PlayerID;

/**
 * Classe représentant un joueur
 * @author Xavier Pantet (260473)
 */
public final class Player {
    private final PlayerID id;
    private final Sq<LifeState> lifeState;
    private final Sq<DirectedPosition> directedPositions;
    private final int maxBombs;
    private final int bombRange;
    
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) throws NullPointerException, IllegalArgumentException{
        this.id=Objects.requireNonNull(id);
        this.lifeState=Objects.requireNonNull(lifeStates);
        this.directedPositions=Objects.requireNonNull(directedPos);
        this.maxBombs=ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange=ArgumentChecker.requireNonNegative(bombRange);
    }
    
    public Player
}
