package ch.epfl.xblast.server;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.SubCell;

public final class DirectedPosition {

    private final SubCell position;
    private final Direction direction;
    /**
     * construit une position dirigée avec la position et la direction donnés, 
     * ou lève l'exception NullPointerException si l'un ou l'autre de ces arguments est nul
     * @param position
     * @param direction
     */
    public DirectedPosition(SubCell position, Direction direction) throws NullPointerException{
       
       if ((position==null||direction==null)){
           throw new NullPointerException();
           
       } else {
           this.position = position;
           this.direction = direction;
       }
    }
    
    
    /**
     * retourne une séquence infinie composée uniquement de la position dirigée donnée et 
     * représentant un joueur arrêté dans cette position
     * @param p
     * @return
     */
    
    public Sq<DirectedPosition> stopped(DirectedPosition p){
 
        return Sq.constant(p);
    }
    
    /**
     * retourne une séquence infinie de positions dirigées représentant un joueur se déplaçant dans la direction dans laquelle il regarde ; 
     * le premier élément de cette séquence est la position dirigée donnée, 
     * le second a pour position la sous-case voisine de celle du premier élément dans la direction de regard, 
     * et ainsi de suite
     * @param p
     * @return
     */
    public Sq<DirectedPosition> moving(DirectedPosition p){
        
        Sq<DirectedPosition> liste = Sq.constant(p).limit(1);
       
        
        liste.iterate(new Cell(3, 6), c -> c.neighbor(direction));
        
    }
    
    /**
     * Retourne la position
     * @return
     */
    public SubCell position(){
        
    }
    
    /**
     * Retourne une position dirigée dont la position est celle donnée, et la direction est identique à celle du récepteur
     * @param newPosition
     * @return
     */
    public DirectedPosition withPosition(SubCell newPosition){
        
    }
    
    /**
     * Retourne la direction de la position dirigée
     * @return
     */
    public Direction direction(){
        
    }
    
    /**
     * Retourne une position dirigée dont la direction est celle donnée, et la position est identique à celle du récepteur.
     * @param newDirection
     * @return
     */
    public DirectedPosition withDirection(Direction newDirection){
        
    }
}
