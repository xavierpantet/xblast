package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public class RandomGame {

    public static void main(String[] args) throws InterruptedException {
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Block b = Block.BONUS_BOMB;
        
        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        List<Player> players = new ArrayList<>();
        RandomEventGenerator randomShit=new RandomEventGenerator(2016, 30, 100);
        players=Arrays.asList(
                new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 2, 3),
                new Player(PlayerID.PLAYER_2, 3, new Cell(-2, 1), 2, 3),
                new Player(PlayerID.PLAYER_3, 3, new Cell(-2, -2), 2, 3),
                new Player(PlayerID.PLAYER_4, 3, new Cell(1, -2), 2, 3));
        
        //GameState g = new GameState(board, players);
        GameState g = new GameState(0, board, players, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
        GameStatePrinter.printGameState(g);
        while(!g.isGameOver()){
            g=g.next(randomShit.randomSpeedChangeEvents(), randomShit.randomBombDropEvents());
            GameStatePrinter.printGameState(g);
           
                Thread.sleep(60);
            
            System.out.println("\u001b[2J");
        }
    }

}
