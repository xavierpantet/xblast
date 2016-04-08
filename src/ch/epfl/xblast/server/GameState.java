package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class GameState {
    
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;
    
    private static final List<List<PlayerID>> PERMUTATIONS = Collections.unmodifiableList(Lists.permutations(Arrays.asList(PlayerID.values())));
    private static final Random RANDOM = new Random(2016);
    
    
    /**
     * Construit l'état du jeu pour le coup d'horloge, le plateau de jeu, les joueurs, les bombes, les explosions et les particules d'explosion (blasts) donnés ; 
     * lève l'exception IllegalArgumentException si le coup d'horloge est (strictement) négatif ou 
     * si la liste des joueurs ne contient pas exactement 4 éléments, 
     * ou l'exception NullPointerException si l'un des cinq derniers arguments est nul
     * @param coup d'horloge
     * @param plateau
     * @param joueurs
     * @param bombes
     * @param explosions
     * @param particules
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
        this.players = Collections.unmodifiableList(new LinkedList<Player>(players));
        this.bombs = Collections.unmodifiableList(new LinkedList<Bomb>(bombs));
        this.explosions = Collections.unmodifiableList(new LinkedList<Sq<Sq<Cell>>>(explosions));
        this.blasts = Collections.unmodifiableList(new LinkedList<Sq<Cell>>(blasts));
        
    }
    
    /**
     * Construit l'état du jeu pour le plateau et les joueurs donnés, pour le coup d'horloge 0 et aucune bombe, explosion ou particule d'explosion.
     * @param plateau
     * @param joueurs
     */
    public GameState(Board board, List<Player> players){

       this(0, board, players, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
       //(Arrays.asList(new Bomb(PlayerID.PLAYER_1, new Cell(7, 7), 10, 3),new Bomb(PlayerID.PLAYER_1, new Cell(6, 7), 20, 3),new Bomb(PlayerID.PLAYER_1, new Cell(5, 7), 30, 3))
    }
    
    /**
     * Retourne le coup d'horloge correspondant à l'état
     * @return le coup d'horloge
     */
    public int ticks(){
        return this.ticks;
    }
    
    /**
     * Retourne vrai si et seulement si l'état correspond à une partie terminée, 
     * c-à-d si le nombre de coups d'horloge d'une partie (Ticks.TOTAL_TICKS) est écoulé, 
     * ou s'il n'y a pas plus d'un joueur vivant
     * @return vrai <=> partie terminée
     */
    public boolean isGameOver(){
        if (this.ticks==Ticks.TOTAL_TICKS){
            return true;
        }
        
        int nbOfAlivePlayers = 0;
    
        for (int i=0; i<4; i++){
            if (players.get(i).isAlive()){
              
                nbOfAlivePlayers++;
           
            }
        }
        
        if (nbOfAlivePlayers<=1){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retourne le temps restant dans la partie, en secondes
     * @return le temps restant
     */
    public double remainingTime(){
        return ((double) Ticks.TOTAL_TICKS-this.ticks)/(double) Ticks.TICKS_PER_SECOND;
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
     * @return le plateau de jeu
     */
    public Board board(){
        return this.board;
    }

    /**
     * Retourne les joueurs, sous la forme d'une liste contenant toujours 4 éléments, car même les joueurs morts en font partie
     * @return les joueurs
     */
    public List<Player> players(){
        return this.players;
    }
    
    /**
     * Retourne les joueurs vivants, c-à-d ceux ayant au moins une vie
     * @return les joueurs vivants
     */
    public List<Player> alivePlayers(){
        
        List<Player> alivePlayers = new ArrayList<Player>();
        
        for(Player p:this.players()){
            if(p.isAlive()){
                alivePlayers.add(p); 
            }
        }
        return alivePlayers;
    }
    
    /**
     * Retourne une table associative des Bombes avec les cases qu'elles occupent
     * @author Xavier Pantet (260473)
     * @return une association cases-bombes
     */
    public Map<Cell, Bomb> bombedCells(){
        Map<Cell, Bomb> bombMap= new HashMap<>();
        
        for(Bomb b:bombs){
            bombMap.put(b.position(), b);
        }
       
        return bombMap;
    }
    
    /**
     * Retourne l'ensemble des cases sur lesquelles se trouve au moins une particule d'explosion
     * @return
     */
    public Set<Cell> blastedCells(){
        
        Set<Cell> cellSet = new HashSet<>();
        
        for (Sq<Cell> cell : blasts){
            cellSet.add(cell.head());
        }
        return cellSet;
    }
    
    /**
     * Retourne l'état du jeu pour le coup d'horloge suivant, 
     * en fonction de l'actuel et des événements donnés (speedChangeEvents et bombDropEvents)
     * @param speedChangeEvents
     * @param bombDropEvents
     * @return
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents){
        
        // Préliminaires: calcul des permutations
        List<PlayerID> playersPermutId = new ArrayList<>(PERMUTATIONS.get(ticks%PERMUTATIONS.size()));
        List<Player> playersPermut = new ArrayList<>();
        for(PlayerID id:playersPermutId){
            for(Player p:players){
                if(p.id().equals(id)){
                    playersPermut.add(p);
                }
            }
        }
        
        
        // (1) nextBlasts
        List<Sq<Cell>> nextBlasts = nextBlasts(blasts, board, explosions);
        
        // Pour 2, on calcule blastedCells
        Set<Cell> blastedCells = new HashSet<Cell>();
        for(Sq<Cell> c : nextBlasts){
            blastedCells.add(c.head());
        }
        
        // Pour 2 et 3, on calcule consummedBonuses
        Set<Cell> consumedBonuses = new HashSet<>();
        Map<PlayerID, Bonus> playerBonuses = new HashMap<PlayerID, Bonus>();
        
        Cell playerCase;
        PlayerID playerID;
        for (int i=0; i<playersPermut.size(); i++){
            playerCase = playersPermut.get(i).position().containingCell();
            playerID = playersPermut.get(i).id();
            

            if(playersPermut.get(i).position().isCentral() && board.blockAt(playerCase)==Block.BONUS_BOMB && !consumedBonuses.contains(playerCase)){
                consumedBonuses.add(playerCase);
                playerBonuses.put(playerID, Bonus.INC_BOMB);
            } else if (playersPermut.get(i).position().isCentral() && board.blockAt(playerCase)==Block.BONUS_RANGE && !consumedBonuses.contains(playerCase)){
                consumedBonuses.add(playerCase);
                playerBonuses.put(playerID, Bonus.INC_RANGE);
            }
        }
        
        // (2) NextBoard
        Board nextBoard = nextBoard(board, consumedBonuses, blastedCells);
        
        // (3) Explosions
        // Explosions dues à d'autres explosions
        List<Sq<Sq<Cell>>> nextExplosion = new LinkedList<>();
        for(Bomb b:bombs){
            if(blastedCells.contains(b.position())){
                nextExplosion.addAll(b.explosion());
            }
        }
        
        // nextExpolosions
        nextExplosion.addAll(nextExplosions(explosions));
        
        
        // Newly dropped Bombs
        List<Bomb> newBombs = newlyDroppedBombs(playersPermut, bombDropEvents, bombs);
        
        for (Bomb b:bombs){
            if(b.fuseLength()==1 || blastedCells.contains(b.position())){
                nextExplosion.addAll(b.explosion());
            }else{
                newBombs.add(new Bomb(b.ownerId(), b.position(), b.fuseLengths().tail(), b.range()));
            }
        }
        
        // Pour 5, on créé un set contenant les cellules portant une bombe
        Set<Cell> bombedCells = new HashSet<Cell>();
        for (Bomb b:newBombs){
            bombedCells.add(b.position());
        }

        // (5) NextPlayers
        List<Player> nextPlayers =  nextPlayers(players, playerBonuses, bombedCells, nextBoard, blastedCells, speedChangeEvents);
        
        return new GameState(ticks+1, nextBoard, nextPlayers, newBombs, nextExplosion, nextBlasts);
    }
    
    
    private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Sq<Cell>>> newExplosions=new ArrayList<>();
        
        for (Sq<Sq<Cell>> sq : explosions0) {
            if(!sq.tail().isEmpty()){
                newExplosions.add(sq.tail());
            }
        }
        return newExplosions;
    }
    
    
    /**
     * Calcule l'état futur du plateau de jeu
     * @author Xavier Pantet (260473)
     * @param plateau0
     * @param bonus consommés
     * @param particules
     * @return l'état futur du plateau de jeu
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses, Set<Cell> blastedCells1){
        List<Sq<Block>> board1 = new ArrayList<>();
        Block currentCell;
        
        // On parcourt le plateau actuel
        for(Cell c:Cell.ROW_MAJOR_ORDER){
            currentCell=board0.blockAt(c);
            
            // Si on est sur un mur destructible atteint par une explosion
            if(currentCell==Block.DESTRUCTIBLE_WALL && blastedCells1.contains(c)){
                Block b;
                
                // On calcule le bloc qui remplacera la case
                int prob = RANDOM.nextInt(3);
                switch (prob){
                    case 0: b=Block.BONUS_BOMB;
                    break;
                    
                    case 1: b=Block.BONUS_RANGE;
                    break;
                    
                    default: b=Block.FREE;
                }
                
                board1.add(Sq.constant(Block.CRUMBLING_WALL).limit(Ticks.WALL_CRUMBLING_TICKS)
                        .concat(Sq.constant(b)));
            }
            
            // Si on est sur un bonus qui a été consommé
            else if(consumedBonuses.contains(c)){
                board1.add(Sq.constant(Block.FREE));
            }
            
            // Si on est sur un bonus atteint par une explosion
            else if(currentCell.isBonus() && blastedCells1.contains(c)){
                
                // On parcourt les Ticks.BONUS_DISAPPEARING_TICKS premiers éléments de la séquence pour
                // savoir si le bonus a déjà été touché par une particule auparavent
                boolean foundFree=false;
                Sq<Block> tmpBlock=board0.blocksAt(c);
                
                // On pourrait utiliser un while à la place du for + break,
                // mais on devrait initialiser et gérer manuellement un compteur
                for(int i=0; i<Ticks.BONUS_DISAPPEARING_TICKS; i++){
                    if(tmpBlock.head()==Block.FREE){
                        foundFree=true;
                        break;
                    }
                    tmpBlock = tmpBlock.tail();
                }
                
                if(!foundFree){
                    board1.add(Sq.constant(currentCell).limit(Ticks.BONUS_DISAPPEARING_TICKS)
                            .concat(Sq.constant(Block.FREE)));
                }
                else{
                    board1.add(board0.blocksAt(c).tail());
                }
            }
            
            // Si on a rien de spécial, on ne change rien
            else{
                board1.add(board0.blocksAt(c).tail());
            }
        }
        return new Board(board1);
    }
    
    /**
     * Retourne les nouvelles bombes déposées par les joueurs, tenant compte
     * des piorités et de leurs droits
     * @author Xavier Pantet (260474)
     * @param joueurs0
     * @param dépôts de bombes
     * @param bombes actuelles
     * @return les nouvelles bombes
     */
    private static List<Bomb> newlyDroppedBombs(List<Player> players0, Set<PlayerID> bombDropEvents, List<Bomb> bombs0){
        
        // On copie bombs1 pour n'itérer que sur un seul tableau quand on teste si une case contient déjà une bombe ou non
        List<Bomb> bombs1 = new LinkedList<>(bombs0);
        int nbEvents=bombDropEvents.size();
        
        // Si personne ne veut déposer de bombe (assez probable), on saute directement au return
        if(nbEvents>0){
            
            // On parcourt les joueurs dans l'ordre
            for(Player p:players0){
                
                // S'il est vivant et qu'il veut déposer une bombe
                if(p.isAlive() && bombDropEvents.contains(p.id())){
                    // On va tester s'il n'a pas atteint son max et si la case est libre
                    int nbBombs=0;
                    boolean cellAlreadyOccupied=false;
                    for(Bomb b:bombs0){
                        if(b.ownerId().equals(p.id())){nbBombs++;}
                        if(p.position().containingCell().equals(b.position())){cellAlreadyOccupied=true;}
                    }
                    
                    // Si c'est tout bon
                    if(nbBombs<p.maxBombs() && !cellAlreadyOccupied){
                        bombs1.add(p.newBomb());
                    }
                }
            }
        }
        bombs1.removeAll(bombs0);
        return bombs1;
    }
    
    
    
    private static List<Player> nextPlayers(List<Player> players0, Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1, Board board1, Set<Cell> blastedCells1, Map<PlayerID, Optional<Direction>> speedChangeEvents){
        
        Sq<DirectedPosition> sequencePos;
        DirectedPosition nextSequencePos;
        Sq<LifeState> sequenceLife;
        List<Player> playerList = new ArrayList<Player>();
        Player newPlayer;
 
        
        for(Player p:players0){
            SubCell position=p.position();
            Sq<DirectedPosition> playerDirectedPosition = p.directedPositions();
            
            //Si le joueur a un desir de changement de direction
            if(speedChangeEvents.containsKey(p.id())){
                Optional<Direction> directionToGo=speedChangeEvents.get(p.id());
                if(directionToGo.isPresent()){
                
                    if(p.direction().isParallelTo(directionToGo.get())){
                  
                        sequencePos = DirectedPosition.moving(new DirectedPosition(position, directionToGo.get()));
                    } else {
                      
                        sequencePos = playerDirectedPosition.takeWhile(u -> !u.position().isCentral())
                                .concat(DirectedPosition.moving(new DirectedPosition(playerDirectedPosition.findFirst(u -> u.position().isCentral()).position(), directionToGo.get())));
                     
                    }
                }else{
                    sequencePos = playerDirectedPosition.takeWhile(u -> !u.position().isCentral())
                            
                            .concat(DirectedPosition.stopped(playerDirectedPosition.findFirst(u -> u.position().isCentral())));
             
                }
            }
            else{
                sequencePos=playerDirectedPosition;
            }
            nextSequencePos=sequencePos.head();
            
            
         
            if(p.lifeState().canMove()){
                if(!(p.position().isCentral()) || (p.position().isCentral() && board1.blockAt(p.position().containingCell().neighbor(nextSequencePos.direction())).canHostPlayer())){
                    if((p.position().distanceToCentral()!=6) || !(p.position().distanceToCentral()==6 && bombedCells1.contains(p.position().containingCell()) && sequencePos.findFirst(u -> !u.position().isCentral()).equals(SubCell.centralSubCellOf(nextSequencePos.position().containingCell())))){
                        sequencePos=sequencePos.tail();
                    }
                }
            }
            
            
            
            if(p.lifeState().canMove() && p.lifeState().state()==State.VULNERABLE && blastedCells1.contains(position.containingCell())){
                sequenceLife=p.statesForNextLife();
            }
            else{
                
                sequenceLife=p.lifeStates().tail();
            }
            
            newPlayer = new Player(p.id(), sequenceLife, sequencePos, p.maxBombs(), p.bombRange());
            
            if(playerBonuses.containsKey(p.id())){
                newPlayer = playerBonuses.get(p.id()).applyTo(newPlayer);
            }
            
            playerList.add(newPlayer);
   
        }

        return playerList;
    }
    
    
    
    
    
    /**
     * Calcule les particules d'explosion pour l'état suivant étant données celles de l'état courant, le plateau de jeu courant et les explosions courantes.
     * @param particules0
     * @param plateau0
     * @param explosions0
     * @return les nouvelles particules
     */
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> blasts1 = new ArrayList<>();
        for(Sq<Cell> s:blasts0){
            if(board0.blockAt(s.head()).isFree()){
                if(!s.tail().isEmpty()){blasts1.add(s.tail());}
            }
        }
        
        for(Sq<Sq<Cell>> s:explosions0){
           blasts1.add(s.head());
        }
        return blasts1;
    }
}