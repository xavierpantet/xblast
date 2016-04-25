package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.server.graphics.BoardPainter;
import ch.epfl.xblast.server.graphics.ExplosionPainter;
import ch.epfl.xblast.server.graphics.PlayerPainter;

/**
 * Représente un sérialiseur d'état de jeu
 * @author timotheedu
 *
 */
public final class GameStateSerializer {

    /**
     * Constructeur par défaut privé pour quôn ne puisse pas créer d'instance. La classe est publique, finale et non instanciable.
     */
    private GameStateSerializer(){};
    
    /**
     * Etant donné un peintre de plateau (de type BoardPainter) et un état de jeu (de type GameState), 
     * retourne la version sérialisée de l'état, sous forme d'une liste d'octets de type List<Byte>
     * @param b (BoardPainter)
     * @param g (GameState)
     * @return liste d'octets de l'état sérialisée (List<Byte>)
     */
    public static List<Byte> serialize(BoardPainter b, GameState g){
    
        List<Byte> encodedGame = new LinkedList<Byte>();
        Board board = g.board();
        Set<Cell> blastedCells = new HashSet<Cell>(g.blastedCells());
        Map<Cell, Bomb> bombedCells = new HashMap<Cell, Bomb>(g.bombedCells());
        int tick = g.ticks();
        
        //UTILISER SPIRAL ORDER !!!!!!!!!
        
        //Encodage du board
        for(Cell c : Cell.SPIRAL_ORDER){
                encodedGame.add(b.byteForCell(board, c));
        }
        
        encodedGame = RunLengthEncoder.encode(encodedGame);
        encodedGame.add(0, (byte)encodedGame.size());
        System.out.println("Board"+encodedGame);
         encodedGame.clear();
        
        //Encodage des bombes et explosions
        for(Cell c : Cell.ROW_MAJOR_ORDER){
            
            if(bombedCells.containsKey(c)){
                encodedGame.add(ExplosionPainter.byteForBomb(bombedCells.get(c)));
            }else if(blastedCells.contains(c)&&board.blockAt(c).isFree()){
                encodedGame.add(ExplosionPainter.byteForBlast(blastedCells.contains(c.neighbor(Direction.N)), blastedCells.contains(c.neighbor(Direction.E)), blastedCells.contains(c.neighbor(Direction.S)), blastedCells.contains(c.neighbor(Direction.W))));
            } else {
                encodedGame.add(ExplosionPainter.BYTE_FOR_EMPTY);
            }
        }
        
        encodedGame = RunLengthEncoder.encode(encodedGame);
        encodedGame.add(0, (byte)encodedGame.size());
        System.out.println("Board"+encodedGame);
         encodedGame.clear();

        
        //Encodage des players
        
        //VOIR SI IL FAUT PLAYERS OU BIEN ALIVE PLAYERS
        for(Player p: g.players()){
            encodedGame.add((byte)p.lives());
            encodedGame.add((byte)p.position().x());
            encodedGame.add((byte)p.position().y());
            encodedGame.add(PlayerPainter.byteForPlayer(tick, p));
          
        }
        
        System.out.println("Players"+(encodedGame));
        encodedGame.clear();

        
        
        //Encodage du temps restant
        
        encodedGame.add((byte)Math.ceil(g.remainingTime()/2));
        
        System.out.println("Time"+RunLengthEncoder.encode(encodedGame));
        encodedGame.clear();
        
        //Compression et retour
   
        //return RunLengthEncoder.encode(encodedGame);
        return (encodedGame);

    }
}
