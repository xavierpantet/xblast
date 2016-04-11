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
    
    /**
     * Constructeur principal
     * @param identité
     * @param états de vie
     * @param position dirigée
     * @param nombre maximal de bombes
     * @param portée des bombes
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange) throws NullPointerException, IllegalArgumentException{
        this.id=Objects.requireNonNull(id);
        this.lifeStates=Objects.requireNonNull(lifeStates);
        this.directedPositions=Objects.requireNonNull(directedPos);
        this.maxBombs=ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange=ArgumentChecker.requireNonNegative(bombRange);
    }
    
    /**
     * Constructeur secondaire
     * @param identité
     * @param nombre de vies
     * @param position
     * @param nombre max de bombes
     * @param portée des bombes
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) throws NullPointerException, IllegalArgumentException{
        this(id, createStateSequence(lives),
                Sq.constant(new DirectedPosition(SubCell.centralSubCellOf(position), Direction.S)),
                maxBombs,
                bombRange);
    }
    
    /**
     * @return l'identifiant
     */
    public PlayerID id(){
        return id;
    }
    
    /**
     * @return la séquence d'états de vie
     */
    public Sq<LifeState> lifeStates(){
        return lifeStates;
    }
    
    /**
     * @return l'état de vie actuel
     */
    public LifeState lifeState(){
        return lifeStates.head();
    }
    
    /**
     * @return l'état de vie suivant
     */
    public Sq<LifeState> statesForNextLife(){
        return Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), State.DYING)).concat(createStateSequence(lives()-1));   
    }
    
    /**
     * @return le nombre de vie restantes
     */
    public int lives(){
        return lifeStates.head().lives();
    }
    
    /**
     * @return true si le joueur est encore vivant
     */
    public boolean isAlive(){
        return (this.lives()>0)? true:false;
    }
    
    /**
     * @return la séquence des directions dirigées
     */
    public Sq<DirectedPosition> directedPositions(){
        return directedPositions;
    }
    
    /**
     * @return la position actuelle
     */
    public SubCell position(){
        return directedPositions.head().position();
    }
    
    /**
     * @return la direction actuelle du regard
     */
    public Direction direction(){
        return directedPositions.head().direction();
    }
    
    /**
     * @return le nombre de bombes maximal
     */
    public int maxBombs(){
        return maxBombs;
    }
    
    /**
     * Permet de dériver un joueur en modifiant le nombre de bombes qu'il peut posséder
     * @param newMaxBombs
     * @return un joueur identique ayant le nombre de bombes indiqué
     */
    public Player withMaxBombs(int newMaxBombs){
        return new Player(id, lifeStates, directedPositions, newMaxBombs, bombRange);
    }
    
    /**
     * @return la portée des bombes déposées par le joueur
     */
    public int bombRange(){
        return bombRange;
    }
    
    /**
     * Permet de dériver un joueur en modifiant la portée des bombes qu'il dépose
     * @param newBombRange
     * @return un joueur identique ayant la portée de bombes indiquée
     */
    public Player withBombRange(int newBombRange){
        return new Player(id, lifeStates, directedPositions, maxBombs, newBombRange);
    }
    
    /**
     * Permet de déposer une bombe
     * @return une bombe déposée par le joueur
     */
    public Bomb newBomb(){
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange);
    }
    
    /**
     * Permet de créer une séquence d'états de vie, utilisé par le constructeur
     * @param nombre de vies
     * @return la séquence d'états de vie du joueur à son initialisation
     * @throws IllegalArgumentException
     */
    private static Sq<LifeState> createStateSequence(int lives) throws IllegalArgumentException{
        if(lives==0){
            return Sq.constant(new LifeState(lives, State.DEAD));
        }
        else if(lives>0){
            // Le joueur est tout d'abord invulnérable (pendant un certain temps)
            Sq<LifeState> lifeState = Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, State.INVULNERABLE));
         
            // Puis il est vulnérable pour une durée indéfinie
            lifeState = lifeState.concat(Sq.constant(new LifeState(lives, State.VULNERABLE)));

            return lifeState;
        } else {throw new IllegalArgumentException("Le nombre de vie doit être non négatif");}
    }
    
    /**
     * Classe représentant une position dirigée du joueur (sa position et la direction dans laquelle il regarde)
     * @author Timothée Duran (258683)
     */
    public static final class DirectedPosition {

        private final SubCell position;
        private final Direction direction;
        
        /**
         * Construit une position dirigée avec la position et la direction données, 
         * ou lève l'exception NullPointerException si l'un ou l'autre de ces arguments est nul
         * @param position
         * @param direction
         */
        public DirectedPosition(SubCell position, Direction direction) throws NullPointerException{
            this.position = Objects.requireNonNull(position);
            this.direction = Objects.requireNonNull(direction);
        }
        
        
        /**
         * Retourne une séquence infinie composée uniquement de la position dirigée donnée et 
         * représentant un joueur arrêté dans cette position
         * @param position dirigée
         * @return une séquence infinie de la position dirigée donnée
         */
        
        public static Sq<DirectedPosition> stopped(DirectedPosition p){
            return Sq.constant(p);
        }
        
        /**
         * Retourne une séquence infinie de positions dirigées représentant un joueur se déplaçant dans la direction dans laquelle il regarde ; 
         * le premier élément de cette séquence est la position dirigée donnée, 
         * le second a pour position la sous-case voisine de celle du premier élément dans la direction de regard, 
         * et ainsi de suite
         * @param position dirigée actuelle
         * @return le déplacement du joueur dans la direction de son regard
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p){
            
            Sq<DirectedPosition> liste;

            liste = Sq.iterate(p, c -> c.withPosition(c.position.neighbor(c.direction)));
            
            return liste;
            
        }
        
        /**
         * Retourne la position
         * @return la position actuelle
         */
        public SubCell position(){
            
            return position;
        }
        
        /**
         * Retourne une position dirigée dont la position est celle donnée, et la direction est identique à celle du récepteur
         * @param newPosition
         * @return une position dirigée identique ayant la position donnée
         */
        public DirectedPosition withPosition(SubCell newPosition){
            return new DirectedPosition (newPosition, this.direction);
        }
        
        /**
         * Retourne la direction de la position dirigée
         * @return la direction
         */
        public Direction direction(){
            return direction;
        }
        
        /**
         * Retourne une position dirigée dont la direction est celle donnée, et la position est identique à celle du récepteur.
         * @param newDirection
         * @return une position dirigée identique ayant la direction indiquée
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
         * Enum qui decrit les 4 états possibles d'un joueur
         * @author Timothée Duran (258683)
         */
        public enum State {
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }
        
        /**
         * Constructeur de la classe
         * @param nombre de vies
         * @param état de vie
         * @throws IllegalArgumentException
         * @throws NullPointerException
         */
        public LifeState(int lives, State state) throws IllegalArgumentException, NullPointerException{
            this.lives = ArgumentChecker.requireNonNegative(lives);
            this.state = Objects.requireNonNull(state);
            
        }
        
        /**
         * Methode qui retorune le nombre de vie restant
         * @return le nombre de vies
         */
        public int lives (){
           return this.lives;
        }
        
        /**
         * Méthode qui retourne l'état actuel d'un joueur
         * @return l'état actuel
         */
        public State state(){
            return this.state;
        }
        
        /**
         * Methode qui retour true si je jour peut bouger et false sinon
         * @return si le joueur peut bouger ou non
         */
        public boolean canMove(){
            return (this.state==State.VULNERABLE)||(this.state==State.INVULNERABLE);
        }

    }


}