package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
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
     * Constructeur de GameState.
     * Construit l'état du jeu pour le coup d'horloge, le plateau de jeu, les joueurs, les bombes, les explosions et les particules d'explosion (blasts) donnés ; 
     * lève l'exception IllegalArgumentException si le coup d'horloge est (strictement) négatif ou 
     * si la liste des joueurs ne contient pas exactement 4 éléments, 
     * ou l'exception NullPointerException si l'un des cinq derniers arguments est nul.
     * @param ticks (int) le coup d'horloge
     * @param board (Board) le plateau
     * @param players (List<Player>) la liste des joueurs
     * @param bombs (List<Bomb>) la liste des bombes
     * @param explosions (List<Sq<Sq<Cell>>>) la liste des explosions
     * @param blasts (List<Sq<Cell>>) la liste des particules d'explosion
     */
    public GameState(int ticks, Board board, List<Player> players, List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions, List<Sq<Cell>> blasts) throws IllegalArgumentException, NullPointerException{
        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        this.board=Objects.requireNonNull(board);
        
        if(players.size()==4){
            this.players = Collections.unmodifiableList(new LinkedList<Player>(players));
        }else{throw new IllegalArgumentException("La liste des joueurs doit contenir 4 éléments");}
        
        this.bombs = Collections.unmodifiableList(new LinkedList<Bomb>(bombs));
        this.explosions = Collections.unmodifiableList(new LinkedList<Sq<Sq<Cell>>>(explosions));
        this.blasts = Collections.unmodifiableList(new LinkedList<Sq<Cell>>(blasts));   
    }
    
    /**
     * Construit l'état du jeu pour le plateau et les joueurs donnés, pour le coup d'horloge 0 et aucune bombe, explosion ou particule d'explosion.
     * @param board (Board) le plateau de jeu
     * @param players (List<Player>) les joueurs
     */
    public GameState(Board board, List<Player> players){
       this(0, board, players, new LinkedList<Bomb>(), new LinkedList<Sq<Sq<Cell>>>(), new LinkedList<Sq<Cell>>());
    }
    
    /**
     * Retourne le coup d'horloge correspondant à l'état.
     * @return le coup d'horloge (int)
     */
    public int ticks(){
        return this.ticks;
    }
    
    /**
     * Indique si l'état correspond à une partie terminée, c-à-d si le nombre de coups d'horloge d'une partie (Ticks.TOTAL_TICKS) est écoulé, ou s'il n'y a pas plus d'un joueur vivant.
     * @return vrai <=> la partie est terminée (boolean)
     */
    public boolean isGameOver(){
        
        // Check du temps
        if (this.ticks>Ticks.TOTAL_TICKS){
            return true;
        }
        
        // Check du nombre de joueurs vivants
        int nbOfAlivePlayers = 0;
        for(Player p : players){
            if(p.isAlive()){nbOfAlivePlayers++;}
        }
        
        return nbOfAlivePlayers<=1;
    }

    /**
     * Retourne le temps restant dans la partie, en secondes.
     * @return le temps restant en secondes (double)
     */
    public double remainingTime(){
        return ((double) Ticks.TOTAL_TICKS-this.ticks)/(double) Ticks.TICKS_PER_SECOND;
    }
    
    /**
     * Retourne un Optional sur le vainqueur de la partie.
     * Si un vainqueur est défini, on le retourne, sinon on retourne un Optional vide.
     * @return le vainqueur de la partie, s'il existe (Optional<PlayerID>)
     */
    public Optional<PlayerID> winner(){
        Optional<PlayerID> winner = Optional.empty();
        
        if(alivePlayers().size()==1){
            winner = Optional.of(alivePlayers().get(0).id());
        }
        return winner;
    }
    
    /**
     * Retourne le plateau de jeu.
     * @return le plateau de jeu (Board)
     */
    public Board board(){
        return board;
    }

    /**
     * Retourne les joueurs, sous la forme d'une liste contenant toujours 4 éléments, car même les joueurs morts en font partie.
     * @return les joueurs morts et vivants (List<Player>)
     */
    public List<Player> players(){
        return players;
    }
    
    /**
     * Retourne les joueurs vivants, c-à-d ceux ayant au moins une vie.
     * @return les joueurs vivants (List<Player>)
     */
    public List<Player> alivePlayers(){
        List<Player> alivePlayers = new LinkedList<Player>();
        
        for(Player p: players){
            if(p.isAlive()){
                alivePlayers.add(p); 
            }
        }
        return alivePlayers;
    }
    
    /**
     * Retourne une table associative des bombes avec les cases qu'elles occupent.
     * @return une table associative position-bombe (Map<Cell, Bomb>)
     */
    public Map<Cell, Bomb> bombedCells(){
        Map<Cell, Bomb> bombMap=new HashMap<>();
        
        for(Bomb b:bombs){
            bombMap.put(b.position(), b);
        }
        return bombMap;
    }
    
    /**
     * Retourne l'ensemble des cases sur lesquelles se trouve au moins une particule d'explosion.
     * @return les cases sur lesquelles se trouvent une particule d'explosion (Set<Cell>)
     */
    public Set<Cell> blastedCells(){
        Set<Cell> cellSet = new HashSet<>();
        
        for (Sq<Cell> cell : blasts){
            cellSet.add(cell.head());
        }
        return cellSet;
    }
    
    /**
     * Retourne l'état du jeu pour le coup d'horloge suivant, en fonction de l'actuel et des événements donnés (speedChangeEvents et bombDropEvents).
     * @param speedChangeEvents (Map<PlayerID, Optional<Direction>>) les événements de changement de direction
     * @param bombDropEvents (Set<PlayerID> les événements de dépôt de bombe)
     * @return l'état du jeu au tick suivant (GameState)
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents){
        
        // Préliminaires: calcul des permutations
        List<PlayerID> playersPermutId = new LinkedList<>(PERMUTATIONS.get(ticks%PERMUTATIONS.size()));
        List<Player> playersPermut = new LinkedList<>();
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
        
        Cell playerCell;
        PlayerID playerID;
        
        for(Player p : playersPermut){
            playerCell = p.position().containingCell();
            playerID = p.id();
            

            if(p.position().isCentral() && board.blockAt(playerCell)==Block.BONUS_BOMB && !consumedBonuses.contains(playerCell)){
                consumedBonuses.add(playerCell);
                playerBonuses.put(playerID, Bonus.INC_BOMB);
            }else if (p.position().isCentral() && board.blockAt(playerCell)==Block.BONUS_RANGE && !consumedBonuses.contains(playerCell)){
                consumedBonuses.add(playerCell);
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
        newBombs.addAll(bombs);
        
        List<Bomb> nextBombs = new LinkedList<>();

        // (5) Bombes
        for (Bomb b : newBombs){
            if(b.fuseLength()==1 || blastedCells.contains(b.position())){
                nextExplosion.addAll(b.explosion());
            }else{
                nextBombs.add(new Bomb(b.ownerId(), b.position(), b.fuseLengths().tail(), b.range()));
            }
        }
        
        // Pour 6, on créé un set contenant les cellules portant une bombe
        Set<Cell> bombedCells = new HashSet<Cell>();
        for (Bomb b:nextBombs){
            bombedCells.add(b.position());
        }

        // (6) NextPlayers
        List<Player> nextPlayers =  nextPlayers(players, playerBonuses, bombedCells, nextBoard, blastedCells, speedChangeEvents);
        
        return new GameState(ticks+1, nextBoard, nextPlayers, nextBombs, nextExplosion, nextBlasts);
    }
    
    
    private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0){
        // Les prochaines explosions sont la queue de chaque élément de explosions0
        List<Sq<Sq<Cell>>> newExplosions=new LinkedList<>();
        for (Sq<Sq<Cell>> sq : explosions0) {
            if(!sq.tail().isEmpty()){
                newExplosions.add(sq.tail());
            }
        }
        return newExplosions;
    }
    
    /**
     * Calcule l'état futur des explosions
     * @param board0 (Board) le plateau de jeu actuel
     * @param consumedBonuses (Set<Cell>) les positions des bonus consommés par le joueurs
     * @param blastedCells1 (Set<Cell>) les positions des particules d'explosion futures
     * @return le plateau de jeu suivant (Board)
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
                }else{
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
     * Retourne les nouvelles bombes déposées par les joueurs, tenant compte des priorités et de leurs droits.
     * @param players0  (List<Player>) les joueurs ordonnés selon les permutations
     * @param bombDropEvents (Set<PlayerID>) événements de dépôts de bombes
     * @param bombs0 (List<Bomb>) les bombes actuelles
     * @return les nouvelles bombes déposées par les joueurs (List<Bomb>)
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
    
    /**
     * Calcule l'état des joueurs au coup d'horloge suivant.
     * @param players0 (List<Player>) les joueurs actuels
     * @param playerBonuses (Map<PlayerID, Bonus>) les bonus consommés par les joueurs
     * @param bombedCells1 (Set<Cell>) les cellules contenant une bombe
     * @param board1 (Board) le plateau
     * @param blastedCells1 (Set<Cell>) les cellules sur lesquelles se trouvent des particules d'explosions
     * @param speedChangeEvents (Map<PlayerID, Optional<Direction>>) les événements de changement de direction
     * @return les joueurs au tick suivant (List<Player>)
     */
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
                
                // S'il s'agit d'une direction définie
                if(directionToGo.isPresent()){
                    
                    // S'il s'agit d'une direction parallèle, il peut changer directement
                    if(p.direction().isParallelTo(directionToGo.get())){
                        sequencePos = DirectedPosition.moving(new DirectedPosition(position, directionToGo.get()));
                    }
                    
                    // Sinon, il doit aller jusqu'à la prochaine sous-case centrale
                    else {
                          sequencePos = playerDirectedPosition.takeWhile(u -> !u.position().isCentral())
                                .concat(DirectedPosition.moving(new DirectedPosition(playerDirectedPosition.findFirst(u -> u.position().isCentral()).position(), directionToGo.get())));
                    }
                }
                
                // Si le joueur veut s'arrêter
                else{
                    
                    // On doit d'abord vérifier que le joueur n'avait pas prévu de changer de direction
                    Direction dirTest = p.direction();
                    boolean found=false;
                    Sq<DirectedPosition> sq = p.directedPositions();
                    DirectedPosition temp;
                    
                    // Donc on doit itérer sur les 15 prochaines sous-cases (15 est la distance maximale que le joueur doit parcourir avant d'atteindre la prochaine sous-case centrale)
                    for(int i=0; i<15; i++){
                        temp=sq.head();
                        
                        // Si on détecte, un changement de direction...
                        if(!temp.direction().equals(dirTest)){
                            found=true;
                            break;
                        }
                        sq=sq.tail();
                    }
                    
                    // Si le joueur ne voulait pas changer, il gardera sa direction actuelle
                    if(!found){
                        sequencePos = playerDirectedPosition.takeWhile(u -> !u.position().isCentral())
                            .concat(DirectedPosition.stopped(new DirectedPosition(playerDirectedPosition.findFirst(u -> u.position().isCentral()).position(), p.direction())));
                    }
                    
                    // Si le joueur voulait changer, on doit prendre la direction qu'il voulait utiliser
                    else{
                        sequencePos = playerDirectedPosition.takeWhile(u -> !u.position().isCentral())
                                .concat(DirectedPosition.stopped(new DirectedPosition(playerDirectedPosition.findFirst(u -> u.position().isCentral()).position(), sq.head().direction())));
                    }
                    
                }
            }
            
            // Si le joueur ne veut pas changer, on ne change rien
            else{
                sequencePos=playerDirectedPosition;
            }
            
            // On prend simplement sa prochaine position dirigée
            nextSequencePos=sequencePos.head();
            
            // Si le joueur peut bouger
            if(p.lifeState().canMove()){
                
                // Et qu'il n'est pas bloqué par un mur
                if(!p.position().isCentral() || (p.position().isCentral() && board1.blockAt(p.position().containingCell().neighbor(nextSequencePos.direction())).canHostPlayer())){
                    
                    // Et qu'il n'est pas bloqué par une bombe
                    if((p.position().distanceToCentral()!=6) || !(p.position().distanceToCentral()==6 && bombedCells1.contains(p.position().containingCell()) && sequencePos.findFirst(u -> u.position().isCentral()).position().equals(SubCell.centralSubCellOf(p.position().containingCell())))){
                        
                        // Alors, on fait évoluer la position dirigée
                        sequencePos=sequencePos.tail();
                    }
                }
            }
            
            // On met éventuellement à jour l'état de vie
            if(p.lifeState().state()==State.VULNERABLE && blastedCells1.contains(sequencePos.head().position().containingCell())){
                sequenceLife=p.statesForNextLife();
            }
            else{
                sequenceLife=p.lifeStates().tail();
            }
            
            // On crée le nouveau joueur
            newPlayer = new Player(p.id(), sequenceLife, sequencePos, p.maxBombs(), p.bombRange());
            
            // Auquel on applique éventuellement les bonus consommés
            if(playerBonuses.containsKey(p.id())){
                newPlayer = playerBonuses.get(p.id()).applyTo(newPlayer);
            }
            playerList.add(newPlayer);
   
        }
        return playerList;
    }
    
    /**
     * Calcule les particules d'explosion pour l'état suivant étant données celles de l'état courant, le plateau de jeu courant et les explosions courantes.
     * @param blasts0 (List<Sq<Cell>>) les particules actuelles
     * @param board0 (Board) le plateau actuel
     * @param explosions0 (List<Sq<Sq<Cell>>>) les explosions actuelles
     * @return les nouvelles particules (List<Sq<Cell>>)
     */
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> blasts1 = new LinkedList<>();
        
        // Pour faire évoluer les blasts, on prend simplement la queue de la séquence
        for(Sq<Cell> s:blasts0){
            if(board0.blockAt(s.head()).isFree()){
                if(!s.tail().isEmpty()){blasts1.add(s.tail());}
            }
        }
        
        // Et pour les explosions, on prend la tête de chaque élément de la liste
        for(Sq<Sq<Cell>> s:explosions0){
           blasts1.add(s.head());
        }
        return blasts1;
    }
}