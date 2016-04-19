package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.graphics.BoardPainter;

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
    
        List<Byte> encodedGame = new ArrayList<Byte>();
        Board board = g.board();
        
        //UTILISER SPIRAL ORDER !!!!!!!!!
        
        //Encodage du board
        for(int i=0; i<Cell.ROWS; i++){
            for(int j=0; j<Cell.COLUMNS; j++){
                encodedGame.add(b.byteForCell(board, new Cell(i, j)));
            }
        }
        
        //Encodage des bombes et explosions
        for()
        
        
        return null;
    }
}
