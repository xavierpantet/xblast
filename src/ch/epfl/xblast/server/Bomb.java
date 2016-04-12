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
 * Classe représentant une bombe dans le jeu.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class Bomb {
    private final PlayerID ownerId;
    private final Cell position;
    private final Sq<Integer> fuseLengths;
    private final int range;

    /**
     * Constructeur de bombe.
     * Créé une bombe avec les paramètres donnés en s'assurant qu'ils sont corrects.
     * @param ownerId (PlayerID) le propriétaire
     * @param position (Cell) la position
     * @param fuseLengths (Sq<Integer>) la seéquence des longueurs de mèche
     * @param range (int) la portée
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
     * Surcharge du premier constructeur quasi-identique au premier.
     * Il initialise une séquence de longueurs de mèches quand on lui en donne la valeur maximale.
     * @param ownerId (PlayerID) le propriétaire
     * @param position (Cell) la position
     * @param fuseLength (int) la longueur de mèche
     * @param range (int) la portée
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range) throws NullPointerException, IllegalArgumentException {
        this(ownerId, position, Sq.iterate(fuseLength,  u -> u-1).limit(fuseLength), range);
    }

    /**
     * Retourne l'identifiant du propriétaire de la bombe.
     * @return l'identifiant du propriétaire (PlayerID)
     */
    public PlayerID ownerId(){
        return ownerId;
    }

    /**
     * Retourne la position de la bombe.
     * @return la position (Cell)
     */
    public Cell position(){
        return position;
    }

    /**
     * Retourne la séquence des longueurs de mèche de la bombe.
     * @return la séquence des longueurs de mèche (Sq<Integer>)
     */
    public Sq<Integer> fuseLengths(){
        return fuseLengths;
    }

    /**
     * Retourne la longueur actuelle de la mèche.
     * @return la longueur actuelle de la mèche (int)
     */
    public int fuseLength(){
        return fuseLengths.head().intValue();
    }

    /**
     * Retourne la portée de l'explosion de la bombe.
     * @return la portée de l'explosion (int)
     */
    public int range(){
        return range;
    }

    /**
     * Retourne l'explosion de la bombe.
     * @return l'explosion de la bombe (List<Sq<Sq<Cell>>>)
     */
    public List<Sq<Sq<Cell>>> explosion(){
        List<Sq<Sq<Cell>>> toReturn = new ArrayList<Sq<Sq<Cell>>>();

        Direction[] dir=Direction.values();
        for(Direction d:dir){
            toReturn.add(explosionArmTowards(d));
        }
        return toReturn;
    }

    /**
     * Retourne un bras d'explosion dans la direction donnée
     * @param dir (Direction) la direction
     * @return un bras d'explosion dans la direction dir (Sq<Sq<Cell>>)
     */
    private Sq<Sq<Cell>> explosionArmTowards(Direction dir){
        Sq<Cell> singleParticle = Sq.iterate(position, c -> c.neighbor(dir)).limit(range);
        Sq<Sq<Cell>> explosionArm=Sq.constant(singleParticle).limit(Ticks.EXPLOSION_TICKS);
        return explosionArm;
    }
}
