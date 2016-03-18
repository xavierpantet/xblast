package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

public final class GameState {
    
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;
    
    
    /**
     * construit l'état du jeu pour le coup d'horloge, le plateau de jeu, les joueurs, les bombes, les explosions et les particules d'explosion (blasts) donnés ; 
     * lève l'exception IllegalArgumentException si le coup d'horloge est (strictement) négatif ou 
     * si la liste des joueurs ne contient pas exactement 4 éléments, 
     * ou l'exception NullPointerException si l'un des cinq derniers arguments est nul
     * @param ticks
     * @param board
     * @param players
     * @param bombs
     * @param explosions
     * @param blasts
     */
    public GameState(int ticks, Board board, List<Player> players, List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions, List<Sq<Cell>> blasts) throws IllegalArgumentException, NullPointerException{
        
        int playerSize = players.size();
        if ((ticks<0)||(playerSize != 4)){
            throw new IllegalArgumentException();
        }
        if ((players.get(0)==null)||(players.get(1)==null)||(players.get(2)==null)||(players.get(3)==null)){
            throw new NullPointerException();
        }
        
        this.ticks = ticks;
        this.board = board;
        this.players = players;
        this.bombs = bombs;
        this.explosions = explosions;
        this.blasts = blasts;
        
    }
    
    /**
     * construit l'état du jeu pour le plateau et les joueurs donnés, pour le coup d'horloge 0 et aucune bombe, explosion ou particule d'explosion.
     * @param board
     * @param players
     */
    public GameState(Board board, List<Player> players){

       this(0, board, players, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
       
    }
    
    /**
     * retourne le coup d'horloge correspondant à l'état
     * @return
     */
    public int ticks(){
        return this.ticks;
    }
    
    /**
     * retourne vrai si et seulement si l'état correspond à une partie terminée, 
     * c-à-d si le nombre de coups d'horloge d'une partie (Ticks.TOTAL_TICKS) est écoulé, 
     * ou s'il n'y a pas plus d'un joueur vivant
     * @return
     */
    public boolean isGameOver(){
        if (this.ticks==Ticks.TOTAL_TICKS){
            return true;
        }
        
        int nbOfAlivePlayers = 0;
    
        for (int i=0; i<4; i++){
            if (players.get(i).isAlive()){
              
                nbOfAlivePlayers++;
           
            } else {
             
            };
        }
        
        if (nbOfAlivePlayers<=3){
            return true;
        } else {
            return false;
        }
    }

    /**
     * retourne le temps restant dans la partie, en secondes
     * @return
     */
    public double remainingTime(){
        return (this.ticks-Ticks.TOTAL_TICKS)/Ticks.TICKS_PER_SECOND;
    }
    
    /**
     * Retourne un Optional sur le vainqueur de la partie
     * Si un vainqueur est défini, on le retourne,
     * sinon on retourne un Optional vide
     * @author Xavier Pantet (260473)
     * @return le vainqueur
     */
    public Optional<PlayerID> winner(){
        Optional<PlayerID> winner = Optional.empty();
        if(alivePlayers().size()==1){
            winner = Optional.of(alivePlayers().get(0).id());
        }
        return winner;
    }
    
    /**
     * Retourne le plateau de jeu
     * @return
     */
    public Board board(){
        return this.board;
    }
    
    /**
     * retourne les joueurs, sous la forme d'une liste contenant toujours 4 éléments, car même les joueurs morts en font partie
     * @return
     */
    public List<Player> players(){
        return this.players;
    }
    
    /**
     * retourne les joueurs vivants, c-à-d ceux ayant au moins une vie
     * @return
     */
    public List<Player> alivePlayers(){
        
        List<Player> alivePlayers = new ArrayList<Player>();
        for (int i=0; i<4; i++){
            Player testedPlayer = this.players().get(i);
            if (testedPlayer.isAlive()){
                alivePlayers.add(testedPlayer);
            }
        }
        
        return alivePlayers;
    }
    
    /**
     * calcule les particules d'explosion pour l'état suivant étant données celles de l'état courant, le plateau de jeu courant et les explosions courantes.
     * @param blasts0
     * @param board0
     * @param explosions0
     * @return
     */
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> blasts1 = new ArrayList<>();
        for(Sq<Cell> s:blasts0){
            if(board0.blockAt(s.head()).isFree()){
                if(!s.tail().isEmpty()){ blasts1.add(s.tail());}
            }
        }
        
        for(Sq<Sq<Cell>> s:explosions0){
           blasts1.add(s.head());
        }
        return blasts1;
    }
}
