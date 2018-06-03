package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.debug.GameStatePrinter;

public class BoardTest {

    @Test(expected=IllegalArgumentException.class)
    public void constructorException() {
        List<Sq<Block>> testList = new ArrayList<Sq<Block>>();
        Board b = new Board(testList);
        
    }
    
    @Test
    public void constructorTest() {
        
        List<Sq<Block>> testList = new ArrayList<Sq<Block>>();
        testList.addAll(Collections.nCopies(Cell.COUNT, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
        Board b = new Board(testList);
        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ofRowsException() {
        List<List<Block>> testList = new ArrayList<List<Block>>();
        Board.ofRows(testList);
        
    }
    
    @Test
    public void ofRows() {
        List<List<Block>> testList = new ArrayList<List<Block>>();
        for (int i=0; i<13; i++){
            testList.add(new ArrayList<Block>());
            testList.get(i).addAll(Collections.nCopies(15, Block.INDESTRUCTIBLE_WALL));
        }
        Board.ofRows(testList);
        
    }
    
    @Test
    public void correctBoardWithGivenMatrix() {
       
        List<List<Block>> testList = new ArrayList<List<Block>>();
        List<Block> sousList = new ArrayList<Block>();
        sousList.add(Block.DESTRUCTIBLE_WALL);
        sousList.add(Block.FREE);
        sousList.add(Block.FREE);
        sousList.add(Block.FREE);
        sousList.add(Block.DESTRUCTIBLE_WALL);
        sousList.add(Block.FREE);
        sousList.add(Block.FREE);
        sousList.add(Block.FREE);
        sousList.add(Block.DESTRUCTIBLE_WALL);
        sousList.add(Block.FREE);
        sousList.add(Block.DESTRUCTIBLE_WALL);
        sousList.add(Block.FREE);
        sousList.add(Block.DESTRUCTIBLE_WALL);
        sousList.add(Block.FREE);
        sousList.add(Block.DESTRUCTIBLE_WALL);
        
        for(int i=0; i<13; i++){
            testList.add(sousList);
        }
        
        
        Board b =  Board.ofRows(testList);
        
        //b.blockAt(c);
        
    }
    
    @Test
    public void ofRowsWorks(){
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        
        Board board = Board.ofRows(
        Arrays.asList(
          Arrays.asList(XX, XX, XX, XX, XX, XX, XX, __, __, __, __, __, __, __, XX),
          Arrays.asList(__, __, __, __, __, xx, __, __, __, __, __, __, __, __, XX),
          Arrays.asList(__, XX, xx, XX, xx, XX, xx, __, __, __, __, __, __, __, XX),
          Arrays.asList(__, xx, __, __, __, xx, __, __, __, __, __, __, __, __, XX),
          Arrays.asList(xx, XX, __, XX, XX, XX, XX, __, __, __, __, __, __, __, XX),
          Arrays.asList(__, xx, __, xx, __, __, __, __, __, __, __, __, __, __, XX),
          Arrays.asList(xx, XX, xx, XX, xx, XX, __, __, __, __, __, __, __, __, XX),
          Arrays.asList(__, __, __, __, __, xx, __, __, __, __, __, __, __, __, XX),
          Arrays.asList(__, XX, xx, XX, xx, XX, xx, __, __, __, __, __, __, __, XX),
          Arrays.asList(__, xx, __, __, __, xx, __, __, __, __, __, __, __, __, XX),
          Arrays.asList(xx, XX, __, XX, XX, XX, XX, __, __, __, __, __, __, __, XX),
          Arrays.asList(__, xx, __, xx, __, __, __, __, __, __, __, __, __, __, XX),
          Arrays.asList(xx, XX, xx, XX, xx, XX, __, __, __, __, __, __, __, __, XX)));
        
        List<Player> players = new ArrayList<>();
        players.add(new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_2, 0, new Cell(13, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_3, 0, new Cell(1, 11), 5, 5));
        players.add(new Player(PlayerID.PLAYER_4, 0, new Cell(13, 11), 5, 5));
        
        GameState g = new GameState(board, players);
        GameStatePrinter.printGameState(g);
    }
    
    @Test
    public void quadrantNWBlocksWorks(){
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
        players.add(new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_2, 0, new Cell(13, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_3, 0, new Cell(1, 11), 5, 5));
        players.add(new Player(PlayerID.PLAYER_4, 0, new Cell(13, 11), 5, 5));
        
        GameState g = new GameState(board, players);
        GameStatePrinter.printGameState(g);
    }
    
    @Test
    public void ofInnerBlocksWalledWorks(){
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofInnerBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __),
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __)));
        
        List<Player> players = new ArrayList<>();
        players.add(new Player(PlayerID.PLAYER_1, 0, new Cell(1, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_2, 0, new Cell(13, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_3, 0, new Cell(1, 11), 5, 5));
        players.add(new Player(PlayerID.PLAYER_4, 0, new Cell(13, 11), 5, 5));
        
        GameState g = new GameState(board, players);
        GameStatePrinter.printGameState(g);
    }
}
