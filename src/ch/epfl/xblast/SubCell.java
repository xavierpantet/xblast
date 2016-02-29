package ch.epfl.xblast;

/**
 * Classe représentant une sous-case d'une case du tableau de jeu
 * @author Timothée Duran (258683)
 */

public final class SubCell {
    private final int x;
    private final int y;
    public final static int COLUMNS=16;
    public final static int ROWS=16;
    
    public final static int midCaseX = (int) Math.floor(COLUMNS/2);
    public final static int midCaseY = (int) Math.floor(ROWS/2);
    
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
     * Calcule la sous-case centrale d'une case donnée en paramètre
     * Si la largeur, respectivement la hauteur, d'une cellule est paire, alors on retournera la sous-case
     * qui se trouve juste à gauche, respectivement juste en-dessus, du milieu de la cellule
     * @param cell
     * @return une instance de SubCell qui représente la sous-case centrale de cell
     */
    public static SubCell centralSubCellOf(Cell cell){
        

        /**
         * Calcul de la sous-case centrale par rapport au plateau
         */
        int midX=cell.x()*COLUMNS+midCaseX;
        int midY=cell.y()*ROWS+midCaseY;
    
 
        return new SubCell(midX, midY);
    }
    
    /**
     * Retourne la distance de Manhattan à la sous-case centrale la plus proche
     * 
     */
    public int distanceToCentral() {
   
      
   
       
       /**
        * Calcul des coordonnées de la case en question par rapport à la sous-case
        */ 
       int caseX = x%COLUMNS;
       int caseY = y%ROWS;
   
   
       
       /**
        * Calcul de la distance entre la case et la case centrale
        */
      int distX = Math.abs(caseX-midCaseX);
      int distY = Math.abs(caseY-midCaseY);
     
      return distX+distY;
        
    }
    
    /**
     * Retourne vrai si et seulement si la sous-case est une sous-case centrale
     * 
     */
    public boolean isCentral(){
        
        if((midCaseX-(x%COLUMNS)==0)&&(midCaseY-(y%ROWS)==0)) {
            return true;
        } else {
            return false;
        }
            
    }
    
    /**
     * retourne la sous-case voisine, dans la direction donnée
     * 
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
            /**J'AIMERAIS CREER UNE ERREURE QUAND C'EST CE CAS LA! GENRE C'EST PAS POSSIBLE TSAIS
             * 
             */
         
            return null;
        
        }
        
    }
    
    /**
     * retourne la case contenant cette sous-case.
     * 
     */
    public Cell containingCell(){
        /** Obtention du nombre de case en largeur et longeur
         * 
         */
        int numeroColonne = (int) Math.ceil(x/16);
        int numeroLigne = (int) Math.ceil(y/16);
        
        return new Cell(numeroColonne, numeroLigne);
        
    }
    
    /**
     * redéfinit la méthode equals de Object et retourne vrai si et seulement si 
     * l'objet that est une sous-case et ses coordonnées normalisées sont identiques à celles de la sous-case réceptrice
     * 
     */
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
     * redéfinit la méthode toString de Object et 
     * retourne une représentation textuelle de la sous-case, 
     * constituée de ses coordonnées entre parenthèses
     * 
     */
    public String toString(){
        return "("+x+", "+y+")";
    }
}