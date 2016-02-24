package ch.epfl.xblast;

/**
 * Les 4 directions que l'on peut prendre dans le jeu.
 * Pour Nord, Est, Sud et West
 * @author Xavier Pantet (260473)
 */
public enum Direction {
    N, E, S, W;
    
    /**
     * Retourne la direction opposée à laquelle on applique la méthode
     * @return la direction opposée
     */
    public Direction opposite(){
        switch(this){
        case E:
            return W;
            
        case W:
            return E;
            
        case N:
            return S;
            
        case S:
            return N;
        default:
            return null;
        }
    }
    
    /**
     * Retourne vrai <=> la direction est horizonzale (E ou W)
     * @return vrai <=> la direction est E ou W
     */
    public boolean isHorizontal(){
        return (this.equals(Direction.E) || this.equals(Direction.W));
    }
    
    /**
     * Retourne vrai <=> la direction this est parallèle à la direction that
     * (that est elle même ou son opposée)
     * @return vrai <=> la direction est elle-même ou son opposée
     */
    public boolean isParallelTo(Direction that){
        return (this.equals(that) || this.equals(that.opposite()));
    }
    
}
