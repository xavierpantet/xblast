package ch.epfl.xblast;

/**
 * Classe représentant une sous-case d'une case du tableau de jeu
 * @author Xavier Pantet (260473)
 */

public final class SubCell {
    private final int x;
    private final int y;
    
    public SubCell(int x, int y){
        this.x=Math.floorMod(x, 239);
        this.y=Math.floorMod(y, 207);
    }
    
    /**
     * Calcule la sous-case centrale d'une case donnée en paramètre
     * Si la largeur, respectivement la hauteur, d'une cellule est paire, alors on retournera la sous-case
     * qui se trouve juste à gauche, respectivement juste en-dessus, du milieu de la cellule
     * @param cell
     * @return une instance de SubCell qui représente la sous-case centrale de cell
     */
    public static SubCell centralSubCellOf(Cell cell){
        int midX=(int) Math.ceil(cell.x()/2);
        int midY=(int) Math.ceil(cell.y()/2);
        return new SubCell(midX, midY);
    }
}
