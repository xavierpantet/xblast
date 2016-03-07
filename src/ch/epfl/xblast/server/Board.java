package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
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
       List<Sq<Block>> tmpBlocks = new ArrayList<Sq<Block>>();
       
      
           for(int i=0; i<Cell.COLUMNS; i++){tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));}
           tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
           for(List<Block> line:innerBlocks){
              
                   for(Block block:line){
                       tmpBlocks.add(Sq.constant(block));
                   }
              
           }
           tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
           for(int i=0; i<Cell.COLUMNS; i++){tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));}
  
       
       return new Board(tmpBlocks);
   }
   
   /**
    * construit un plateau muré symétrique avec les blocs du quadrant nord-ouest donnés, ou lève l'exception IllegalArgumentException si la liste reçue n'est pas constituée de 6 listes de 7 éléments chacune.
    * @param quadrantNWBlocks
    * @return
    */
   
   public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks) throws IllegalArgumentException{
       checkBlockMatrix(quadrantNWBlocks, (Cell.ROWS-1)/2, (Cell.COLUMNS-1)/2);

       List<Sq<Block>> tmpBlocks = new ArrayList<Sq<Block>>();
       
       try{
    
           //ETAPE 1 : On construit la première ligne du jeu, remplie de mur
           tmpBlocks.addAll(Collections.nCopies(Cell.COLUMNS, Sq.constant(Block.INDESTRUCTIBLE_WALL)));
           
           //ETAPE 2: On construit ligne par ligne a partir de la ligne 2 et jusqu'a la fin du plateau exemple.
           for (int i=0; i<(Cell.ROWS-1)/2; i++){
               //On ajoute un mur au début
               tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
               //On regarde si la sous-liste contient autant d'éléments qu'attendu
               
                   //Si c'est OK on ajoute alors la ligne en question à tmpBlocks. On créer tmp Line pour séoudre un problème du Sq.constant
                   List<Block> tmpLine = new ArrayList<Block>();
                   tmpLine = Lists.mirrored(quadrantNWBlocks.get(i));
                   for(int j=0; j<tmpLine.size(); j++){
                       tmpBlocks.add(Sq.constant(tmpLine.get(j)));
                   }  
              
               //On ajoute le mur à la fin de la ligne
               tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
  
           }
           //ETAPE 3: On construit maintenant la moitié inférieure du jeu. Pour eviter de tout recalculer, on sait que la moitié inférieure du jeu et le mirroir de la partir suppérieur. Alors il suffit de faire passer dans la méthode mirrored la liste tmpBlocks que on a deja calculé.
           
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
