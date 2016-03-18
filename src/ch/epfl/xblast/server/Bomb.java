package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * Classe représentant une bombe dans le jeu
 * @author Xavier Pantet (260473)
 */
public final class Bomb {
    private final PlayerID ownerId;
    private final Cell position;
    private final Sq<Integer> fuseLengths;
    private final int range;
    
    /**
     * Créé une bombe avec les données en paramètre
     * @param ownerId
     * @param position
     * @param fuseLengths
     * @param range
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range) throws NullPointerException, IllegalArgumentException {
        this.ownerId=Objects.requireNonNull(ownerId);
        this.position=Objects.requireNonNull(position);
        
        if(!fuseLengths.isEmpty()){
            this.fuseLengths=Objects.requireNonNull(fuseLengths);
        }else{throw new IllegalArgumentException("La séquence des longueurs de la mèche ne peut pas être vide");}
        
        this.range=ArgumentChecker.requireNonNegative(range);
    }
    
    /**
     * Surcharge du premier constructeurs quasi-identique au premier.
     * Il initialise une séquence quand on lui en donne l'élément maximal
     * @param ownerId
     * @param position
     * @param fuseLengths
     * @param range
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range) throws NullPointerException, IllegalArgumentException {
        this(ownerId, position, Sq.iterate(fuseLength,  u -> u-1).limit(fuseLength), range);
    }
    
    /**
     * Retourne l'identifiant du propriétaire de la bombe
     * @return l'identifiant du propriétaire
     */
    public PlayerID ownerId(){
        return ownerId;
    }
    /**
     * Retourne la position de la bombe
     * @return la position
     */
    public Cell position(){
        return position;
    }
    
    /**
     * Retourne la séquence des longueurs de mèche de la bombe
     * @return la séquence des longueurs de mèche
     */
    public Sq<Integer> fuseLengths(){
        return fuseLengths;
    }
    
    /**
     * Retourne la longueur actuelle de la mèche
     * @return la longueur actuelle de la mèche
     */
    public int fuseLength(){
        return fuseLengths.head().intValue();
    }
    
    /**
     * Retourne la portée de l'explosion de la bombe
     * @return la portée de l'explosion
     */
    public int range(){
        return range;
    }
    
    /**
     * Retourne l'explosion de la bombe
     * @return l'explosion de la bombe
     */
    public List<Sq<Sq<Cell>>> explosion(){
        List<Sq<Sq<Cell>>> toReturn = new ArrayList<Sq<Sq<Cell>>>();
        Direction[] dir=Direction.values();
        for(int i=0; i<4; i++){
            toReturn.add(explosionArmTowards(dir[i]));
        }
        return toReturn;
    }
    
    private Sq<Sq<Cell>> explosionArmTowards(Direction dir){
        Sq<Cell> singleParticle = Sq.iterate(position, c -> c.neighbor(dir)).limit(range);
        Sq<Sq<Cell>> explosionArm=Sq.constant(singleParticle).limit(Ticks.EXPLOSION_TICKS);
        return explosionArm;
    }
}
