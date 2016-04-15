package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/** Classe représentant un plateau de jeu
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class Board {
    private final List<Sq<Block>> blocks;

    /**
     * Constructeur de Board.
     * Initialise les séquences avec celles passées en paramètres.
     * Lève IllegalArgumentException si la liste ne contient pas 195 éléments.
     * @param blocks (List<Sq<Block>>) liste des séquences pour chaque bloc
     * @throws IllegalArgumentException
     */
    public Board(List<Sq<Block>> blocks) throws IllegalArgumentException{
        if(blocks.size()==Cell.COUNT){
            // On choisit un ArrayList car on y accède par get dans blocksAt
            this.blocks=Collections.unmodifiableList(new ArrayList<Sq<Block>>(blocks));
        } else {
            throw new IllegalArgumentException("La liste doit posséder "+Cell.COUNT+" éléments");
        }
    }

    /**
     * Construit un plateau constant avec la matrice de blocs donnée, 
     * ou lève l'exception IllegalArgumentException si la liste reçue n'est pas constituée 
     * de 13 listes de 15 éléments chacune.
     * @param rows (List<List<Block>>) matrice de contenu
     * @return un plateau ofRows (Board)
     * @throws IllegalArgumentException
     */
    public static Board ofRows(List<List<Block>> rows) throws IllegalArgumentException{

        checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);
        List<Sq<Block>> tmpBlocks = new ArrayList<Sq<Block>>();

        for(List<Block> l : rows){
            for(Block e:l){
                tmpBlocks.add(Sq.constant(e));
            }
        }
        return new Board(tmpBlocks); 
    }

    /**
     * Construit un plateau constant avec la matrice des blocs donnée emmuré,
     * ou lève l'erreur IllegalArgumentException si la matrice n'a pas les bonnes dimensions.
     * @param innerBlocks (List<List<Block>>) matrices de blocs
     * @return un plateau ofInnerBlocksWalled (Board)
     * @throws IllegalArgumentException
     */
    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) throws IllegalArgumentException{
        checkBlockMatrix(innerBlocks, Cell.ROWS-2, Cell.COLUMNS-2);

        List<Sq<Block>> tmpBlocks = new ArrayList<Sq<Block>>();

        // On ajoute le mur du haut
        tmpBlocks.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
        for(List<Block> l : innerBlocks){

            // On commence une ligne par un mur
            tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));

            // On ajoute les blocs
            for(Block e:l){
                tmpBlocks.add(Sq.constant(e));
            }

            // On termine par un mur
            tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));  
        }

        // On ajoute le mur du bas
        tmpBlocks.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
        return new Board(tmpBlocks);
    }

    /**
     * Construit un plateau muré symétrique avec les blocs du quadrant nord-ouest donnés,
     * ou lève l'exception IllegalArgumentException si la liste reçue n'est pas constituée de 6 listes de 7 éléments chacune.
     * @param quadrantNWBlocks (List<List<Block>>) matrice de blocs
     * @return un plateau muré symétrique avec les blocs du quadrant nord-ouest donnés (Board)
     */  
    public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks) throws IllegalArgumentException{
        List<Sq<Block>> tmpBlocks = new LinkedList<Sq<Block>>();
        checkBlockMatrix(quadrantNWBlocks, (Cell.ROWS-1)/2, (Cell.COLUMNS-1)/2);

        // On ajoute la première ligne de blocs indestructibles
        tmpBlocks.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));

        List<Sq<Block>> tmpLine = new LinkedList<>();
        // Pour chaque ligne de la matrice d'entrée
        for(List<Block> l : quadrantNWBlocks){

            // On ajoute le mur gauche
            tmpLine.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));

            // Puis, on ajoute chaque élément
            for(Block b : l){
                tmpLine.add(Sq.constant(b));
            }

            // On ajoute au board la ligne obtenue en miroir
            tmpLine=Lists.mirrored(tmpLine);
            tmpBlocks.addAll(tmpLine);
            tmpLine.clear();
        }

        /* A ce stade, on obient les (Cell.ROWS-1)/2 premières lignes du board
         * Il suffit de retirer la dernière demi-ligne et de prendre le board en miroir
         * pour obtenir le board final
         */
        tmpBlocks=tmpBlocks.subList(0, tmpBlocks.size()-(Cell.COLUMNS-1)/2);
        tmpBlocks=Lists.mirrored(tmpBlocks);
        return new Board(tmpBlocks);  
    }

    /**
     * Retourne la séquence des blocs pour la case donnée.
     * @param c (Cell) la case dont on veut connaître la séquence des blocks
     * @return la séquence des blocks de c (Sq<Block>)
     */
    public Sq<Block> blocksAt(Cell c){
        // On cherche l'identifiant de la cellule
        int cellID = c.rowMajorIndex();

        // On retroune la séquence correspondante
        return blocks.get(cellID);
    }

    /**
     * Retourne le bloc courant à la cellule donnée.
     * @param c (Cell) la cellule dont on veut connaître le bloc
     * @return le bloc courant de c (Block)
     */
    public Block blockAt(Cell c){
        Sq<Block> tmpList = blocksAt(c);
        return tmpList.head();
    }

    /**
     * Méthode privée qui vérifie que les matrices passées en paramètres sont conformes.
     * Notamment, elles doivent avoir le même nombre d'éléments dans chaque ligne. Retourne une erreur "IllegalArgumentException" sinon 
     * Le nombre de lignes et de colonnes voulu est passé en paramètre.
     * @param matrix (List<List<Block>>)
     * @param rows (int)
     * @param columns (int)
     * @throws IllegalArgumentException
     */
    private static void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns) throws IllegalArgumentException{

        int matrixHeight=matrix.size();
        if (matrixHeight==rows){
            for(List<Block> tmpL : matrix){
                if(tmpL.size()!=columns){
                    throw new IllegalArgumentException("L'une des lignes de la matrice fournie contient "+tmpL.size()+" colonnes alors qu'on en attend "+columns);
                }
            }
        } else {
            throw new IllegalArgumentException("La matrice fournie contient "+matrixHeight+" lignes alors qu'on en attend "+rows);
        }
    }

}
