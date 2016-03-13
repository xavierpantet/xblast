package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ListsTest {

    @Test
    public void mirrorsKayakCorrectly(){
        List<String> l = new ArrayList<String>();
        l.add("k");
        l.add("a");
        l.add("y");
        
        List<String> t = new ArrayList<String>();
        t.add("k");
        t.add("a");
        t.add("y");
        t.add("a");
        t.add("k");
        
        List<String> mirror = Lists.mirrored(l);
        assertEquals(t, mirror);
    }
    
    @Test
    public void mirrorOnTrivialArrayIsCorrect(){
        List<String> l = new ArrayList<String>();
        l.add("s");
        List<String> t = new ArrayList<String>();
        t.add("s");
        assertEquals(t, l);
    }
    
    @Test
    public void mirrorsCellsCorrectly(){
        List<Cell> l = new ArrayList<>();
        l.add(new Cell(1,2));
        l.add(new Cell(3,4));
        
        List<Cell> t = new ArrayList<>();
        t.add(new Cell(1,2));
        t.add(new Cell(3,4));
        t.add(new Cell(1,2));
        
        List<Cell> mirror = Lists.mirrored(l);
        assertEquals(t, mirror);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void throwsExceptionOnEmptyArray(){
        List<Integer> l = new ArrayList<>();
        List<Integer> t = new ArrayList<>();
        List<Integer> mirror = Lists.mirrored(l);
        assertEquals(t, mirror);
    }
    
    @Test
    public void permutationsWorks(){
        List<Integer> l = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8));
        List<List<Integer>> check = new ArrayList<>();
        int factorial=1;
        long startTime = System.currentTimeMillis();
        List<List<Integer>> t = Lists.permutations(l);
        long endTime   = System.currentTimeMillis();
        System.out.println("Temps de calcul: " + (endTime-startTime) + " ms");
        
        for(int i=2; i<=l.size(); i++){
            factorial*=i;
        }
        
        for(List<Integer> e:t){
            //System.out.println(e);
            if(!check.contains(e)){
                check.add(e);
            }
        }
        if(!check.contains(l)){check.add(l);}
        assertEquals(check.size(), factorial);
    }

}
