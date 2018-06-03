package ch.epfl.xblast;

/**
 * Classe représentant une sous-case d'une case du tableau de jeu.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class SubCell {

    /**
     * Nombre de colonnes d'une case.
     */
    public final static int COLUMNS=16;

    /**
     * Nombre de lignes d'une case.
     */
    public final static int ROWS=16;

    // Deux variables utiles au fonctionnement de la classe
    private final static int midCaseX = COLUMNS/2;
    private final static int midCaseY = ROWS/2;
    private final static int TOTAL_COLUMNS=240;
    private final static int TOTAL_ROWS=208;

    // Attributs privés
    private final int x;
    private final int y;

    /**
     * Constructeur de sous-case
     * @param x (int) l'abscisse
     * @param y (int) l'ordonnée
     */
    public SubCell(int x, int y){
        this.x=Math.floorMod(x, TOTAL_COLUMNS);
        this.y=Math.floorMod(y, TOTAL_ROWS);
    }

    /**
     * Retourne l'abscisse de la cellule
     * @return position x - abscisse (int9
     */
    public int x(){
        return this.x;
    }

    /**
     * Retourne l'ordonnée de la cellule
     * @return position y - ordonnée (int)
     */
    public int y(){
        return this.y;
    }

    /**
     * Calcule la sous-case centrale d'une case donnée en paramètre.
     * Si la largeur, respectivement la hauteur, d'une cellule est paire, alors on retournera la sous-case qui se trouve juste à gauche, respectivement juste en-dessus, du milieu de la cellule.
     * @param cell (Cell) la cellule dont on veut calculer la sous-case centrale
     * @return la sous-case centrale de la cell (SubCell)
     */
    public static SubCell centralSubCellOf(Cell cell){

        // Calcul de la sous-case centrale par rapport au plateau
        int midX=cell.x()*COLUMNS+midCaseX;
        int midY=cell.y()*ROWS+midCaseY;

        return new SubCell(midX, midY);
    }

    /**
     * Retourne la distance de Manhattan de la sous-case à la sous-case centrale.
     * @return distance au centre (int)
     */
    public int distanceToCentral() {
        // Calcul des coordonnées
        int caseX = x()%COLUMNS;
        int caseY = y()%ROWS;

        // Calcul de la distance
        int distX = Math.abs(caseX-midCaseX);
        int distY = Math.abs(caseY-midCaseY);

        return distX+distY;
    }

    /**
     * Indique si la sous-case est une sous-case centrale.
     * @return vrai <=> la sous-case est une sous-case centrale (boolean)
     */
    public boolean isCentral(){
        return distanceToCentral()==0;
    }

    /**
     * Retourne la sous-case voisine, dans la direction donnée.
     * @param d (Direction) la direction donnée
     * @return sous-case voisine dans la direction d (SubCell)
     */
    public SubCell neighbor(Direction d){
        switch(d){
        case E:
            return new SubCell(x()+1, y());

        case W:
            return new SubCell(x()-1, y());

        case N:
            return new SubCell(x(), y()-1);

        case S:
            return new SubCell(x(), y()+1);

        default:
            return null;
        }

    }

    /**
     * Retourne la case contenant cette sous-case.
     * @return la cellule contenant la sous-case (Cell)
     */
    public Cell containingCell(){
        // Nombre de cases en largeur et longueur
        int numeroColonne = x()/COLUMNS;
        int numeroLigne = y()/ROWS;

        return new Cell(numeroColonne, numeroLigne);
    }

    /**
     * Redéfinition de equals de Object et retourne vrai si et seulement si l'objet that est une sous-case et ses coordonnées normalisées sont identiques à celles de la sous-case réceptrice.
     * @param that (Object) la sous-case à comparer
     * @return vrai <=> this = that (boolean)
     */
    @Override
    public boolean equals(Object that){
        // Vérifie si this et that sont des instances de la même classe
        if (that!= null && (that.getClass().equals(this.getClass()))){
            if (that instanceof SubCell){
                // On vérifie si les coordonnées normalisées sont les mêmes
                SubCell c = (SubCell) that;
                return (x()==c.x() && y()==c.y());
            }
            return false;
        }
        return false;
    }

    /**
     * Redéfinition de hashCode, retourne un une valeur de hachage pour la sous-case
     * @return une valeur de hachage (int)
     */
    @Override
    public int hashCode(){
        return y()*Cell.COLUMNS*COLUMNS+x();
    }

    /**
     * Redéfinition de toString de Object et retourne une représentation textuelle de la sous-case, constituée de ses coordonnées entre parenthèses.
     * @return la sous-case au format (x, y) (String)
     */
    @Override
    public String toString(){
        return "("+x()+", "+y()+")";
    }
}