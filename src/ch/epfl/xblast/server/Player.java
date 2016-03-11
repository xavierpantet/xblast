package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * Classe représentant un joueur
 * @author Xavier Pantet (260473)
 */
public final class Player {
    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPositions;
    private final int maxBombs;
    private final int bombRange;
    
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) throws NullPointerException, IllegalArgumentException{
        this.id=Objects.requireNonNull(id);
        this.lifeStates=Objects.requireNonNull(lifeStates);
        this.directedPositions=Objects.requireNonNull(directedPos);
        this.maxBombs=ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange=ArgumentChecker.requireNonNegative(bombRange);
    }
    
    public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) throws NullPointerException, IllegalArgumentException{
        this(id, createStateSequence(lives),
                Sq.constant(new DirectedPosition(SubCell.centralSubCellOf(position), Direction.S)),
                maxBombs,
                bombRange);
    }
    
    public PlayerID id(){
        return id;
    }
    
    public Sq<LifeState> lifeStates(){
        return lifeStates;
    }
    
    public LifeState lifeState(){
        return lifeStates.head();
    }
    
    public Sq<LifeState> statesForNextLife(){
        int nbLives=lives();
        Sq<LifeState> toReturn = Sq.constant(new LifeState(nbLives, State.DYING)).limit(Ticks.PLAYER_DYING_TICKS);
        if(nbLives==0){
            toReturn.concat(Sq.constant(new LifeState(nbLives, State.DEAD)));
        }
        else{
            toReturn.concat(Sq.constant(new LifeState(nbLives, State.INVULNERABLE)).limit(Ticks.PLAYER_INVULNERABLE_TICKS));
            toReturn.concat(Sq.constant(new LifeState(nbLives-1, State.VULNERABLE)));
        }
        return toReturn;
        
    }
    
    public int lives(){
        return lifeStates.head().lives();
    }
    
    public boolean isAlive(){
        return (lives()>0)? true:false;
    }
    
    public Sq<DirectedPosition> directedPositions(){
        return directedPositions;
    }
    
    public SubCell position(){
        return directedPositions.head().position();
    }
    
    public Direction direction(){
        return directedPositions.head().direction();
    }
    
    public int maxBombs(){
        return maxBombs;
    }
    
    public Player withMaxBombs(int newMaxBombs){
        return new Player(id, lifeStates, directedPositions, newMaxBombs, bombRange);
    }
    
    public int bombRange(){
        return bombRange;
    }
    
    public Player withBombRange(int newBombRange){
        return new Player(id, lifeStates, directedPositions, maxBombs, newBombRange);
    }
    
    public Bomb newBomb(){
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange);
    }
    
    private static Sq<LifeState> createStateSequence(int lives) throws IllegalArgumentException{
        if(lives==0){
            return Sq.constant(new LifeState(lives, State.DEAD));
        }
        else if(lives>0){
            Sq<LifeState> lifeState = Sq.constant(new LifeState(lives, State.INVULNERABLE)).limit(Ticks.PLAYER_INVULNERABLE_TICKS);
            lifeState.concat(Sq.constant(new LifeState(lives, State.VULNERABLE)));
            return lifeState;
        }
        else{throw new IllegalArgumentException("Le nombre de vie doit être non négatif");}
    }
    
    /**
     * Classe représentant une position dirigée du joueur (sa position et la direction dans laquelle il regarde)
     * @author Timothée Duran (258683)
     */
    public static final class DirectedPosition {

        private final SubCell position;
        private final Direction direction;
        /**
         * construit une position dirigée avec la position et la direction donnés, 
         * ou lève l'exception NullPointerException si l'un ou l'autre de ces arguments est nul
         * @param position
         * @param direction
         */
        public DirectedPosition(SubCell position, Direction direction) throws NullPointerException{
           
           if ((position==null||direction==null)){
               throw new NullPointerException();
               
           } else {
               this.position = position;
               this.direction = direction;
           }
        }
        
        
        /**
         * retourne une séquence infinie composée uniquement de la position dirigée donnée et 
         * représentant un joueur arrêté dans cette position
         * @param p
         * @return
         */
        
        public Sq<DirectedPosition> stopped(DirectedPosition p){
     
            return Sq.constant(p);
        }
        
        /**
         * retourne une séquence infinie de positions dirigées représentant un joueur se déplaçant dans la direction dans laquelle il regarde ; 
         * le premier élément de cette séquence est la position dirigée donnée, 
         * le second a pour position la sous-case voisine de celle du premier élément dans la direction de regard, 
         * et ainsi de suite
         * @param p
         * @return
         */
        public Sq<DirectedPosition> moving(DirectedPosition p){
            
            Sq<DirectedPosition> liste;

            liste = Sq.iterate(p, c -> c.withPosition(position.neighbor(direction)));
            
            return liste;
            
        }
        
        /**
         * Retourne la position
         * @return
         */
        public SubCell position(){
            
            return position;
        }
        
        /**
         * Retourne une position dirigée dont la position est celle donnée, et la direction est identique à celle du récepteur
         * @param newPosition
         * @return
         */
        public DirectedPosition withPosition(SubCell newPosition){
            return new DirectedPosition (newPosition, this.direction);
        }
        
        /**
         * Retourne la direction de la position dirigée
         * @return
         */
        public Direction direction(){
            return direction;
        }
        
        /**
         * Retourne une position dirigée dont la direction est celle donnée, et la position est identique à celle du récepteur.
         * @param newDirection
         * @return
         */
        public DirectedPosition withDirection(Direction newDirection){
            return new DirectedPosition (this.position, newDirection);
        }
    }
    
    /**
     * Classe représentant l'état de vie d'un joueur (nombre de vies et état)
     * @author Timothée Duran (258683)
     */
    public static final class LifeState {
        
        private final int lives;
        private final State state;
        
        /**
         * Enum qui decrit les trois etats possibles d'un joueur
         * @author timotheedu
         *
         */
        public enum State {
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }
        
        /**
         * Constructeur de la classe
         * @param lives
         * @param state
         * @throws IllegalArgumentException
         * @throws NullPointerException
         */
        public LifeState(int lives, State state) throws IllegalArgumentException, NullPointerException{
            
            if(lives<0){
                throw new IllegalArgumentException();
            } else if (state==null){
                throw new NullPointerException();
            }
            
            this.lives = lives;
            this.state = state;
            
        }
        
        /**
         * Methode qui retorune le nombre de vie restant
         * @return this.lives
         */
        public int lives (){
           return this.lives;
        }
        
        /**
         * Méthode qui retourne l'état actuel d'un joueur
         * @return this.state
         */
        public State state(){
            return this.state;
        }
        
        /**
         * Methode qui retour true si je jour peut bouger et false sinon
         * @return
         */
        public boolean canMove(){
            if ((this.state==State.VULNERABLE)||(this.state==State.INVULNERABLE)){
                return true;
            } else {
                return false;
            }
        }

    }


}
