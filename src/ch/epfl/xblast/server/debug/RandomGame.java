package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public class RandomGame {

    public static void main(String[] args) throws InterruptedException {
        String red = "\u001b[42m";
        String std = "\u001b[m";
        String s = "Un mot en " + red + "rouge" + std + "…";
        System.out.println(s);
        
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        
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
        players.add(new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_2, 3, new Cell(13, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_3, 3, new Cell(1, 11), 5, 5));
        players.add(new Player(PlayerID.PLAYER_4, 3, new Cell(13, 11), 5, 5));
        
        GameState g = new GameState(board, players);
        
        while(!g.isGameOver()){
            GameStatePrinter.printGameState(g);
            g=g.next(randomShit.randomSpeedChangeEvents(), randomShit.randomBombDropEvents());
            Thread.sleep(50);
        }
    }

}
