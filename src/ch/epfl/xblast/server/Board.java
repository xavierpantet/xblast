package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;

/** Classe représentant un plateau de jeu
 * @author Xavier Pantet (260473)
 * @author Timothée Duran (258683)
 */
public class Board {
    private List<Sq<Block>> blocks;
    
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
       
       List<Sq<Block>> tmpBlocks = new ArrayList<Sq<Block>>();
       
       if(rows.size()==Cell.ROWS){
           for (int i=0; i<Cell.ROWS; i++){
               if(rows.get(i).size()==Cell.COLUMNS){
                   for (int j=0; j<Cell.COLUMNS; j++){
                       tmpBlocks.add(Sq.constant(rows.get(i).get(j)));
                   }
               //On retroune une erreur qui dit que la ligne n'a pas le bon nombre d'éléments
               } else {
                   throw new IllegalArgumentException("La ligne "+i+" n'a pas le bon nombre d'éléments");
               }
            
           }
         
       //On retourne une erreur qui dit que le tableau n'a pas le bon nombre d'éléments
       } else {
           throw new IllegalArgumentException("Le tableau n'a pas le bon nombre d'éléments");
       }
       
       return new Board(tmpBlocks); 
   }
   
   public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) throws IllegalArgumentException{
       List<Sq<Block>> tmpBlocks = new ArrayList<Sq<Block>>();
       
       if(innerBlocks.size()==Cell.ROWS-2){
           for(int i=0; i<Cell.COLUMNS; i++){tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));}
           tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
           for(List<Block> line:innerBlocks){
               if(line.size()==Cell.COLUMNS-2){
                   for(Block block:line){
                       tmpBlocks.add(Sq.constant(block));
                   }
               }
               else{throw new IllegalArgumentException("La ligne n'a pas le bon nombre d'éléments");}
           }
           tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
           for(int i=0; i<Cell.COLUMNS; i++){tmpBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));}
       }else{throw new IllegalArgumentException("Le tableau n'a pas le bon nombre d'éléments");}
       
       return new Board(tmpBlocks);
   }
}
