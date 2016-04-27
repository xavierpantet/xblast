package ch.epfl.xblast.client;

import static org.junit.Assert.*;

import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.epfl.xblast.client.GameStateDeserializer;

import org.junit.Test;

public class GameStateDeserializerTest {

    @Test
    public void subListAreCorrect() {
        
        List<Integer> givenListI = new ArrayList<Integer>(Arrays.asList(121, -50, 2, 1, -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3, 1, 3, 1, 1, -2, 0, 1, 3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1, 3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3, 1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2, 3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2, 4, -128, 16, -63, 16, 3, 24, 24, 6, 3, -40, 24, 26, 3, -40, -72, 46, 3, 24, -72, 66, 60));
        List<Byte> givenListB = new ArrayList<Byte>();
        for(int i=0; i<givenListI.size(); i++){
            givenListB.add(givenListI.get(i).byteValue());
        }
 
    }
    @Test
   
    public void listForTime() throws URISyntaxException, IOException{

        List<Integer> givenListI = new ArrayList<Integer>(Arrays.asList(121, -50, 2, 1, -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3, 1, 3, 1, 1, -2, 0, 1, 3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1, 3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3, 1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2, 3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2, 4, -128, 16, -63, 16, 3, 24, 24, 6, 3, -40, 24, 26, 3, -40, -72, 46, 3, 24, -72, 66, 60));
        List<Byte> givenListB = new ArrayList<Byte>();
        for(int i=0; i<givenListI.size(); i++){
            givenListB.add(givenListI.get(i).byteValue());
        }
        
        GameStateDeserializer.deserialize(givenListB);
        
       
    }
    

}
