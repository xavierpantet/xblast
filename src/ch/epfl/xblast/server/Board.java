package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/** Classe représentant un plateau de jeu
 * @author Xavier Pantet (260473)
 * @author Timothée Duran (258683)
 */
public class Board {
    private final List<Sq<Block>> blocks;
    
    /**
     * Initialise les séquences avec celles passées en paramètres
     * Lève IllegalArgumentException si la liste ne contient pas 195 éléments
     * @param liste des séquences pour chaque bloc
     * @throws IllegalArgumentException
     */
    public Board(List<Sq<Block>> blocks) throws IllegalArgumentException{
        if(blocks.size()==Cell.COUNT){
            this.blocks=blocks;
        }
        else{throw new IllegalArgumentException("La liste doit posséder "+Cell.COUNT+" éléments");}
    
    }
   
    /**
     * qui construit un plateau constant avec la matrice de blocs donnée, 
     * ou lève l'exception IllegalArgumentException si la liste reçue n'est pas constituée 
     * de 13 listes de 15 éléments chacune.
     * @param rows
     * @return
     */
   public static Board ofRows(List<List<Block>> rows) throws IllegalArgumentException{

       checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);
       
       List<Sq<Block>> tmpBlocks = new ArrayList<Sq<Block>>();

       for (int i=0; i<Cell.ROWS; i++){
           for (int j=0; j<Cell.COLUMNS; j++){
               tmpBlocks.add(Sq.constant(rows.get(i).get(j)));
       
           }
       }
       
       return new Board(tmpBlocks); 
   }
   
   public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) throws IllegalArgumentException{
       checkBlockMatrix(innerBlocks, Cell.ROWS-2, Cell.COLUMNS-2);
       
       List<Sq<Block>> tmpBlocks = new LinkedList<Sq<Block>>();
       tmpBlocks.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
       for(List<Block> l : innerBlocks){
           tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
           for(Block e:l){
               tmpBlocks.add(Sq.constant(e));
           }
           tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));  
       }
       tmpBlocks.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
       return new Board(tmpBlocks);
   }
   
   /**
    * construit un plateau muré symétrique avec les blocs du quadrant nord-ouest donnés, ou lève l'exception IllegalArgumentException si la liste reçue n'est pas constituée de 6 listes de 7 éléments chacune.
    * @param quadrantNWBlocks
    * @return
    */
   
   public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks) throws IllegalArgumentException{
       List<Sq<Block>> tmpBlocks = new ArrayList<Sq<Block>>();
      
       try{
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

       }catch(Exception e){
           throw new IllegalArgumentException(e);
       }
       return new Board(tmpBlocks);
       
   }
   
   /**
    * Retourne la séquence des blocs pour la case donnée
    * @param c
    * @return
    */
   public Sq<Block> blocksAt(Cell c){
       //On cherche l'identifiant de la cellule
       int CellID = c.rowMajorIndex();
       //On retroune le sous-tableau à cet ID
       return blocks.get(CellID);
   }
   
   public Block blockAt(Cell c){
     Sq<Block> tmpList = blocksAt(c);
     return tmpList.head();
   }
   
   private static void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns) throws IllegalArgumentException{
       
          int matrixSize =matrix.size();
          if (matrixSize==rows){
              for (int i=0; i<matrixSize; i++){
                  if (matrix.get(i).size()!=columns){
                      throw new IllegalArgumentException("La matrice fournie contient "+matrix.get(i).size()+" colonnes à l'index "+i+" alors qu'on en attend "+columns);
                  }
              }
          } else {
              throw new IllegalArgumentException("La matrice fournie contient "+matrixSize+" lignes alors qu'on en attend "+rows);
          }
   }
   
}
