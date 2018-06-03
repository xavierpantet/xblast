package ch.epfl.xblast.server.debug;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateClient;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.graphics.BlockImage;
import ch.epfl.xblast.server.graphics.BoardPainter;

public class RandomGame {

    public static void main(String[] args) throws InterruptedException, IllegalArgumentException, URISyntaxException, IOException {
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
        
       
        
        // XBlast Component
        XBlastComponent component = new XBlastComponent();
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        Consumer<PlayerAction> c = System.out::println;
        
        
        // Palette de blocks
        Map<Block, BlockImage> palette=new HashMap<>();
        palette.put(Block.FREE, BlockImage.IRON_FLOOR);
        for(int i=1;i<Block.values().length;i++)
            palette.put(Block.values()[i], BlockImage.values()[i+1]);
        BoardPainter bp= new BoardPainter(palette, BlockImage.IRON_FLOOR_S);
        component.setGameState(GameStateDeserializer.deserialize(GameStateSerializer.serialize(bp, g)), PlayerID.PLAYER_1);
        
        // Fenêtre
        JFrame window = new JFrame("XBlast");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(component);
        window.pack();
        window.setVisible(true);
        component.addKeyListener(new KeyboardEventHandler(kb, c));
        component.requestFocusInWindow();
        
        while(!g.isGameOver()){
            g=g.next(randomShit.randomSpeedChangeEvents(), randomShit.randomBombDropEvents());

            component.setGameState(GameStateDeserializer.deserialize(GameStateSerializer.serialize(bp, g)), PlayerID.PLAYER_1);
        
            Thread.sleep(60);
            
            /*GameStatePrinter.printGameState(g);
           
                Thread.sleep(60);
            
            System.out.println("\u001b[2J");*/
        }
    }

}
