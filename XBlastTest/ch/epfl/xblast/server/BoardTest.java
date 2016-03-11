package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;

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
        
        b.blockAt(c)
        
    }

}
