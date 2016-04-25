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
import ch.epfl.xblast.server.Player.LifeState.State;
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
    
        
        //ENCODAGE DU BOARD
        List<Byte> encodedBoard = new LinkedList<Byte>();
        for(Cell c : Cell.SPIRAL_ORDER){
            encodedBoard.add(b.byteForCell(board, c));
        }
        
        //On compresse le board
        encodedBoard = RunLengthEncoder.encode(encodedBoard);
        
        //On ajoute la taille de la liste compressée au début
        encodedBoard.add(0, (byte)encodedBoard.size());
        
        //On ajoute le code du board à la liste finale
        encodedGame.addAll(encodedBoard);
        

        //ENCODAGE DES BOMBES ET EXPLOSION
        List<Byte> encodedBombs = new LinkedList<Byte>();
        for(Cell c : Cell.ROW_MAJOR_ORDER){
            
            if(bombedCells.containsKey(c)){
                encodedBombs.add(ExplosionPainter.byteForBomb(bombedCells.get(c)));
            }else if(blastedCells.contains(c)&&board.blockAt(c).isFree()){
                encodedBombs.add(ExplosionPainter.byteForBlast(blastedCells.contains(c.neighbor(Direction.N)), blastedCells.contains(c.neighbor(Direction.E)), blastedCells.contains(c.neighbor(Direction.S)), blastedCells.contains(c.neighbor(Direction.W))));
            } else {
                encodedBombs.add(ExplosionPainter.BYTE_FOR_EMPTY);
            }
        }
        
        encodedBombs = RunLengthEncoder.encode(encodedBombs);
        encodedBombs.add(0, (byte)encodedBombs.size());
        
        //On ajoute le code des bombs à la liste finale
        encodedGame.addAll(encodedBombs);
        
        //ENCODAGE DES PLAYERS
        
        //VOIR SI IL FAUT PLAYERS OU BIEN ALIVE PLAYERS
        for(Player p: g.players()){
            encodedGame.add((byte)p.lives());
            encodedGame.add((byte)p.position().x());
            encodedGame.add((byte)p.position().y());
            encodedGame.add(PlayerPainter.byteForPlayer(tick, p));
          
        }

        //Encodage du temps restant
        
        encodedGame.add((byte)Math.ceil(g.remainingTime()/2));

        //retour
        return (encodedGame);

    }
}
