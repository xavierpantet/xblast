package ch.epfl.xblast.server.graphics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.DirectedPosition;

public class GraphicsTests {
    @Test
    public void byteForCellWorksProperly(){
        Map<Block, BlockImage> palette=new HashMap<>();
        palette.put(Block.FREE, BlockImage.IRON_FLOOR);
        for(int i=1;i<Block.values().length;i++)
            palette.put(Block.values()[i], BlockImage.values()[i+1]);
        
        for(Map.Entry<Block, BlockImage> map : palette.entrySet())
            System.out.println("Key: "+ map.getKey()+"\nValue: "+map.getValue());
        
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
        
        byte myByteFreeShadow=bp.byteForCell(board, new Cell(1,1));
        byte myByteFree=bp.byteForCell(board, new Cell(2, 1));
        byte myByteIndestructible=bp.byteForCell(board, new Cell(2, 2));
        byte myByteDestructible=bp.byteForCell(board, new Cell(3, 2));
        
        assertEquals(myByteFreeShadow, 1);
        assertEquals(myByteFree, 0);
        assertEquals(myByteIndestructible, 2);
        assertEquals(myByteDestructible, 3);
    }
    
    @Test
    public void byteForPlayerWorksProperly(){
        int t=0;
        
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
        players.add(new Player(PlayerID.PLAYER_1,3, new Cell(13, 1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_2,Sq.constant(new Player.LifeState(3, Player.LifeState.State.INVULNERABLE)),Player.DirectedPosition.moving(new DirectedPosition(new SubCell(13*16+8,24),Direction.S)),5,5));
        players.add(new Player(PlayerID.PLAYER_3,Sq.constant(new Player.LifeState(3, Player.LifeState.State.DYING)),Player.DirectedPosition.moving(new DirectedPosition(new SubCell(24,24),Direction.S)),5,5));
        players.add(new Player(PlayerID.PLAYER_4,Sq.constant(new Player.LifeState(1, Player.LifeState.State.DYING)),Player.DirectedPosition.moving(new DirectedPosition(new SubCell(24,24),Direction.S)),5,5));

        Set<PlayerID> set=new HashSet<>();
        set.add(PlayerID.PLAYER_4);
        
        Map<PlayerID, Optional<Direction>> map=new HashMap<>();
        map.put(PlayerID.PLAYER_1, Optional.of(Direction.W));

        GameState g = new GameState(board, players);

        while(t<32){
            g=g.next(map,set);
          System.out.println(t);
          t++;
          if (t%4==1){
            assertEquals(7+4,PlayerPainter.byteForPlayer(t, g.players().get(0))%20);
            assertEquals(7,PlayerPainter.byteForPlayer(t, g.players().get(1))%20);
          }
          else if (t%4==3){
            assertEquals(5+5,PlayerPainter.byteForPlayer(t, g.players().get(0))%20);
            assertEquals(8,PlayerPainter.byteForPlayer(t, g.players().get(1))%20);
          }
          else{
            assertEquals(6+3,PlayerPainter.byteForPlayer(t, g.players().get(0))%20);
            assertEquals(6,PlayerPainter.byteForPlayer(t, g.players().get(1))%20);
          }
          
          if (t%2==1) assertEquals(85, PlayerPainter.byteForPlayer(t, g.players().get(1)),5);
          else assertEquals(25, PlayerPainter.byteForPlayer(t, g.players().get(1)),5);
          
          assertEquals(52, PlayerPainter.byteForPlayer(t, g.players().get(2)));
          assertEquals(73, PlayerPainter.byteForPlayer(t, g.players().get(3)));
        }
    }

    }
