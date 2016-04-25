import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.graphics.BlockImage;
import ch.epfl.xblast.server.graphics.BoardPainter;

public class GameStateSerializerTest {

    @Test
    public void CodeingIsCorrect() {
        
        Map<Block, BlockImage> palette=new HashMap<>();
        palette.put(Block.FREE, BlockImage.IRON_FLOOR);
        for(int i=1;i<Block.values().length;i++)
            palette.put(Block.values()[i], BlockImage.values()[i+1]);
    
        
        BoardPainter bp= new BoardPainter(palette, BlockImage.IRON_FLOOR_S);

        
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
        players.add(new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_2, 3, new Cell(13, 1), 5, 5));
        players.add(new Player(PlayerID.PLAYER_3, 3, new Cell(1, 11), 5, 5));
        players.add(new Player(PlayerID.PLAYER_4, 3, new Cell(13, 11), 5, 5));
        
        GameState g = new GameState(board, players);
        
        //System.out.println("RESULTAT"+RunLengthEncoder.encode(GameStateSerializer.serialize(bp, g)));
        
        System.out.println("RESULTAT"+GameStateSerializer.serialize(bp, g));
        List<Integer> givenListI = new ArrayList<Integer>(Arrays.asList(121, -50, 2, 1, -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3, 1, 3, 1, 1, -2, 0, 1, 3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1, 3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3, 1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2, 3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2, 4, -128, 16, -63, 16, 3, 24, 24, 6, 3, -40, 24, 26, 3, -40, -72, 46, 3, 24, -72, 66, 60));
        List<Byte> givenListB = new ArrayList<Byte>();
        
        for(int i=0; i<givenListI.size(); i++){
            givenListB.add(givenListI.get(i).byteValue());
        }
        
        System.out.println("ATTENTE"+givenListB);
        fail("Not yet implemented");
    }

}
