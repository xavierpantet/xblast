package ch.epfl.xblast.server.graphics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

/**
 * Classe permettant de peindre le plateau de jeu en fonction des images que l'on a pour représenter chaque case.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class BoardPainter {
    private final Map<Block, BlockImage> pallet;
    private final BlockImage shadowedBlock;

    /**
     * Constructeur de BoardPainter
     * @param pallet (Map<Block, BlockImage>) une palette établissant la correspondance entre les blocks et leurs images respectives
     * @param shadowedBlock (BlockImage) le bloc-image à utiliser pour les ombres projetées
     */
    public BoardPainter(Map<Block, BlockImage> pallet, BlockImage shadowedBlock) throws NullPointerException{
        this.pallet=Objects.requireNonNull(Collections.unmodifiableMap(new HashMap<>(pallet)));
        this.shadowedBlock=shadowedBlock;
    }

    /**
     * Retourne l'octet correspondant à une case donnée du plateau de jeu.
     * @param board (Board) le plateau de jeu
     * @param cell (Cell) la case dont on veut l'octet
     * @return l'octet de cell pour le plateau board (byte)
     */
    public byte byteForCell(Board board, Cell cell){
        int id=0;
        Block block = board.blockAt(cell);

        // Si le bloc est libre, on doit savoir si le bloc à gauche projette une ombre ou pas
        if(block==Block.FREE){
            if(!board.blockAt(cell.neighbor(Direction.W)).castsShadow()){
                id=pallet.get(Block.FREE).ordinal(); 
            }
            else{
                id=shadowedBlock.ordinal();
            }
        }
        else{
            id=pallet.get(block).ordinal();
        }
        return (byte) id;
    }
}
