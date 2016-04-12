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
 * Classe représentant un joueur.
 * @author Xavier Pantet (260473)
 */
public final class Player {
    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPositions;
    private final int maxBombs;
    private final int bombRange;
    
    /**
     * Constructeur principal.
     * @param id    l'identité
     * @param lifeStates    la séquence d'états de vie
     * @param directedPos   la position dirigée
     * @param maxBombs  le nombre maximal de bombes
     * @param bombRange la portée des bombes
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
     * Constructeur secondaire.
     * Permet de créer un joueur en spécifiant uniquement son nombre de vies, plutôt que sa séquence d'états de vies.
     * @param id    l'identité
     * @param lives le nombre de vies
     * @param position  la position
     * @param maxBombs  le nombre max de bombes
     * @param bombRange la portée des bombes
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
     * Retourne l'identifiant.
     * @return l'identifiant
     */
    public PlayerID id(){
        return id;
    }
    
    /**
     * retourne la séquence des états de vie.
     * @return la séquence d'états de vie
     */
    public Sq<LifeState> lifeStates(){
        return lifeStates;
    }
    
    /**
     * Retourne l'état de vie actuel.
     * @return l'état de vie actuel
     */
    public LifeState lifeState(){
        return lifeStates.head();
    }
    
    /**
     * Retourne la séquence des états de vie suivants, une fois qu'elle évolue.
     * @return l'état de vie suivant
     */
    public Sq<LifeState> statesForNextLife(){
        return Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), State.DYING)).concat(createStateSequence(lives()-1));   
    }
    
    /**
     * Retourne le nombre de vies restantes.
     * @return le nombre de vie restantes
     */
    public int lives(){
        return lifeStates.head().lives();
    }
    
    /**
     * Retourne vrai <=> le joueur est encore vivant (son nombre de vies est supérieur à 0)
     * @return vrai <=> le joueur est encore vivant
     */
    public boolean isAlive(){
        return (this.lives()>0)? true:false;
    }
    
    /**
     * Retourne la séquence des positions dirigées.
     * @return la séquence des positions dirigées
     */
    public Sq<DirectedPosition> directedPositions(){
        return directedPositions;
    }
    
    /**
     * Retourne la position actuelle.
     * @return la position actuelle
     */
    public SubCell position(){
        return directedPositions.head().position();
    }
    
    /**
     * Retourne la direction actuelle du regard.
     * @return la direction actuelle du regard
     */
    public Direction direction(){
        return directedPositions.head().direction();
    }
    
    /**
     * Retourne le nombre maximal que le joueur peut déposer simultanément sur le plateau.
     * @return le nombre de bombes maximal
     */
    public int maxBombs(){
        return maxBombs;
    }
    
    /**
     * Permet de dériver un joueur en modifiant le nombre de bombes qu'il peut posséder.
     * @param newMaxBombs   le nouveau nombre de bombes maximal
     * @return un joueur identique ayant newMaxBombs bombes à disposition simultanément
     */
    public Player withMaxBombs(int newMaxBombs){
        return new Player(id, lifeStates, directedPositions, newMaxBombs, bombRange);
    }
    
    /**
     * Retourne la portée des bombes déposées.
     * @return la portée des bombes déposées par le joueur
     */
    public int bombRange(){
        return bombRange;
    }
    
    /**
     * Permet de dériver un joueur en modifiant la portée des bombes qu'il dépose.
     * @param newBombRange  la nouvelle portée des bombes
     * @return un joueur identique ayant newBombRange de portée
     */
    public Player withBombRange(int newBombRange){
        return new Player(id, lifeStates, directedPositions, maxBombs, newBombRange);
    }
    
    /**
     * Permet au joueur de déposer une bombe.
     * @return une bombe déposée par le joueur
     */
    public Bomb newBomb(){
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange);
    }
    
    /**
     * Permet de créer une séquence d'états de vie en connaissant le nombre de vies initial.
     * Très utile au constructeur.
     * @param lives le nombre de vies
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
     * Classe représentant une position dirigée du joueur (sa position et la direction dans laquelle il regarde).
     * @author Timothée Duran (258683)
     */
    public static final class DirectedPosition {

        private final SubCell position;
        private final Direction direction;
        
        /**
         * Construit une position dirigée avec la position et la direction données, 
         * ou lève l'exception NullPointerException si l'un ou l'autre de ces arguments est nul.
         * @param position  la position du joueur
         * @param direction la direction du regard du joueur
         */
        public DirectedPosition(SubCell position, Direction direction) throws NullPointerException{
            this.position = Objects.requireNonNull(position);
            this.direction = Objects.requireNonNull(direction);
        }
        
        
        /**
         * Retourne une séquence infinie composée uniquement de la position dirigée donnée et représentant un joueur arrêté dans cette position.
         * @param p la position dirigée
         * @return une séquence infinie de la position dirigée donnée (joueur à l'arrêt)
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition p){
            return Sq.constant(p);
        }
        
        /**
         * Retourne une séquence infinie de positions dirigées représentant un joueur se déplaçant dans la direction dans laquelle il regarde ; 
         * le premier élément de cette séquence est la position dirigée, 
         * le second a pour position la sous-case voisine de celle du premier élément dans la direction de regard, 
         * et ainsi de suite.
         * @param p la position dirigée initiale
         * @return le déplacement du joueur dans la direction de son regard
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p){
            return Sq.iterate(p, c -> c.withPosition(c.position.neighbor(c.direction)));            
        }
        
        /**
         * Retourne la position actuelle.
         * @return la position actuelle
         */
        public SubCell position(){
            
            return position;
        }
        
        /**
         * Retourne une position dirigée dont la position est celle donnée, et la direction est identique à celle du récepteur.
         * @param newPosition   la nouvelle position
         * @return une position dirigée identique ayant la position donnée
         */
        public DirectedPosition withPosition(SubCell newPosition){
            return new DirectedPosition (newPosition, direction);
        }
        
        /**
         * Retourne la direction de la position dirigée.
         * @return la direction
         */
        public Direction direction(){
            return direction;
        }
        
        /**
         * Retourne une position dirigée dont la direction est celle donnée, et la position est identique à celle du récepteur.
         * @param newDirection  la nouvelle direction
         * @return une position dirigée identique ayant la direction indiquée
         */
        public DirectedPosition withDirection(Direction newDirection){
            return new DirectedPosition (position, newDirection);
        }
    }
    
    /**
     * Classe représentant l'état de vie d'un joueur (nombre de vies et état).
     * @author Timothée Duran (258683)
     */
    public static final class LifeState {
        
        private final int lives;
        private final State state;
        
        /**
         * Enum qui decrit les 4 états possibles d'un joueur.
         * @author Timothée Duran (258683)
         */
        public enum State {
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }
        
        /**
         * Constructeur d'état de vie
         * @param lives le nombre de vies
         * @param state l'état de vie
         * @throws IllegalArgumentException
         * @throws NullPointerException
         */
        public LifeState(int lives, State state) throws IllegalArgumentException, NullPointerException{
            this.lives = ArgumentChecker.requireNonNegative(lives);
            this.state = Objects.requireNonNull(state);
            
        }
        
        /**
         * Methode qui retourne le nombre de vie restantes.
         * @return le nombre de vies restantes
         */
        public int lives (){
           return this.lives;
        }
        
        /**
         * Méthode qui retourne l'état actuel d'un joueur.
         * @return l'état actuel
         */
        public State state(){
            return this.state;
        }
        
        /**
         * Methode qui retourne vrai si le joueur peut bouger (son état est VULNERABLE OU INVULNERABLE) et faux sinon.
         * @return vrai <=> le joueur peut bouger ou non
         */
        public boolean canMove(){
            return (this.state==State.VULNERABLE)||(this.state==State.INVULNERABLE);
        }

    }


}