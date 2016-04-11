package ch.epfl.xblast;

/**
 * Classe représentant une sous-case d'une case du tableau de jeu.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */

public final class SubCell {
    private final int x;
    private final int y;
    public final static int COLUMNS=16;
    public final static int ROWS=16;
    public final static int midCaseX = (int) Math.floor(COLUMNS/2);
    public final static int midCaseY = (int) Math.floor(ROWS/2);
    
    /**
     * Constructeur de la subcell
     * @param x
     * @param y
     */
    public SubCell(int x, int y){
        this.x=Math.floorMod(x, 240);
        this.y=Math.floorMod(y, 208);
    }
    
    /**
     * Retourne l'abscisse de la cellule
     * @return position x
     */
    public int x(){
        return this.x;
    }
    
    /**
     * Retourne l'ordonnée de la cellule
     * @return position y
     */
    public int y(){
        return this.y;
    }
    
    /**
     * Calcule la sous-case centrale d'une case donnée en paramètre.
     * Si la largeur, respectivement la hauteur, d'une cellule est paire, alors on retournera la sous-case
     * qui se trouve juste à gauche, respectivement juste en-dessus, du milieu de la cellule.
     * @param la cellule
     * @return la sous-case centrale de cell
     */
    public static SubCell centralSubCellOf(Cell cell){
        
        // Calcul de la sous-case centrale par rapport au plateau
        int midX=cell.x()*COLUMNS+midCaseX;
        int midY=cell.y()*ROWS+midCaseY;

        return new SubCell(midX, midY);
    }
    
    /**
     * Retourne la distance de Manhattan de la sous-case à la sous-case centrale
     * @return distance au centre
     */
    public int distanceToCentral() {
        // Calcul des coordonnées
        int caseX = x%COLUMNS;
        int caseY = y%ROWS;
        
        // Calcul de la distance
        int distX = Math.abs(caseX-midCaseX);
        int distY = Math.abs(caseY-midCaseY);
     
        return distX+distY;
    }
    
    /**
     * Retourne vrai si et seulement si la sous-case est une sous-case centrale
     * @return vrai si et seulement si la sous-case est une sous-case centrale
     */
    public boolean isCentral(){
        return distanceToCentral()==0;
    }
    
    /**
     * Retourne la sous-case voisine, dans la direction donnée.
     * @return sous-case voisine
     */
    public SubCell neighbor(Direction d){
        switch(d){
        case E:
            return new SubCell(x+1, y);
            
        case W:
            return new SubCell(x-1, y);
            
        case N:
            return new SubCell(x, y-1);
            
        case S:
            return new SubCell(x, y+1);
            
        default:
            return null;
        }
        
    }
    
    /**
     * Retourne la case contenant cette sous-case.
     * @return la cellule contenante
     */
    public Cell containingCell(){
        // Nombre de cases en largeur et longueur
        int numeroColonne = (int) Math.ceil(x/16);
        int numeroLigne = (int) Math.ceil(y/16);
        
        return new Cell(numeroColonne, numeroLigne);
    }
    
    /**
     * Redéfinit la méthode equals de Object et retourne vrai si et seulement si 
     * l'objet that est une sous-case et ses coordonnées normalisées sont identiques à celles de la sous-case réceptrice
     * @param that
     * @return vrai <=> this = that
     */
    @Override
    public boolean equals(Object that){
        // Vérifie si this et that sont des instances de la même classe
        if (that!= null && (that.getClass().equals(this.getClass()))){
            if (that instanceof SubCell){
                // On vérifie si les coordonnées normalisées sont les mêmes
                SubCell c = (SubCell) that;
                return (this.x==c.x() && this.y==c.y());
            }
            return false;
        }
        return false;
    }
    
    /**
     * Retourne un identifiant unique pour la sous-case
     * @return un hashcode unique
     */
    @Override
    public int hashCode(){
        return y*Cell.COLUMNS*COLUMNS+x;
    }
    /**
     * Redéfinit la méthode toString de Object et 
     * retourne une représentation textuelle de la sous-case, 
     * constituée de ses coordonnées entre parenthèses.
     * @return version textuelle
     */
    @Override
    public String toString(){
        return "("+x+", "+y+")";
    }
}