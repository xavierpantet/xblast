package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math;

/**
 * Classe représentant une case (cellule) du tableau de jeu.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class Cell {
    
    /**
     * Nombre de colonnes total d'un plateau de jeu.
     */
    public final static int COLUMNS=15;
    
    /**
     * Nombre de lignes total d'un plateau de jeu.
     */
    public final static int ROWS=13;
    
    /**
     * Nombre de cases (cellules) total d'un plateau de jeu.
     */
    public final static int COUNT=COLUMNS*ROWS;
    
    /**
     * Les cases d'un plateau de jeu selon l'ordre de lecture.
     */
    public final static List<Cell> ROW_MAJOR_ORDER=Collections.unmodifiableList(rowMajorOrder());
    
    /**
     * Les cases d'un plateau de jeu en spirale.
     */
    public final static List<Cell> SPIRAL_ORDER=Collections.unmodifiableList(spiralOrder());
    
    // Attributs privés
    private final int x;
    private final int y;
    
    /**
     * Constructeur de cellule.
     * @param x (int) l'abscisse
     * @param y (int) l'ordonnée
     */
    public Cell(int x, int y){
        this.x=Math.floorMod(x, COLUMNS);
        this.y=Math.floorMod(y, ROWS);
    }
    
    /**
     * Retourne l'abscisse de la cellule.
     * @return position x (abscisse)
     */
    public int x(){
        return this.x;
    }
    
    /**
     * Retourne l'ordonnée de la cellule.
     * @return position y (ordonnée)
     */
    public int y(){
        return this.y;
    }
    
    /**
     * Retourne une ArrayList de Cell qui content les cases triées dans l'ordre de lecture
     * @return ArrayList de Cell triée par ordre de lecture (ArrayList<Cell>)
     */
    private static ArrayList<Cell> rowMajorOrder(){
        ArrayList<Cell> cells = new ArrayList<Cell>();
        
        // On parcourt simplement le tableau selon l'ordre de lecture et on créé les cellules correspondantes
        for(int i=0; i<ROWS; i++){
            for(int j=0; j<COLUMNS; j++){
                cells.add(new Cell(j, i));
            }
        }
        return cells;
    }
    
    /**
     * Retourne une ArrayList de Cell qui content les cases triées dans un ordre en spirale
     * @return ArrayList de Cell triée dans un ordre de spirale (ArrayList<Cell>)
     */
    private static ArrayList<Cell> spiralOrder(){
        // Voir algorithme proposé sur le site du cours
        
        
        // On construit les tableaux ix et iy
        List<Integer> ix = new ArrayList<Integer>();
        List<Integer> iy = new ArrayList<Integer>();
        for(int i=0; i<COLUMNS; i++){ix.add(i);}
        for(int i=0; i<ROWS; i++){iy.add(i);}
        
        // Quelques variables utiles pour l'algorithme
        boolean horizontal=true;
        List<Integer> i1;
        List<Integer> i2;
        Cell c;
        
        // ArrayList contenant les cellules
        ArrayList<Cell> spiral = new ArrayList<Cell>();
        
        // Algorithme
        while(!ix.isEmpty() && !iy.isEmpty()){
            // Définition des lignes
            if(horizontal){i1=ix; i2=iy;}
            else{i1=iy; i2=ix;}
            
            // On ampute la première ligne
            int c2=i2.remove(0);
            
            // On parcourt la ligne et on crée les cellules
            for(int c1: i1){
                if(horizontal){c = new Cell(c1, c2);}
                else{c = new Cell(c2, c1);}
                spiral.add(c);
            }
            Collections.reverse(i1);
            horizontal=!horizontal;
        }
        return spiral;
    }
    
    /**
     * Retourne l'index de la case selon l'ordre de lecture (ROW_MAJOR_ORDER).
     * @return l'index de la case en ROW_MAJOR_ORDER (int)
     */
    public int rowMajorIndex(){
        return y*COLUMNS+x;
    }
    
    /**
     * Retourne la case voisine dans la direction d, en tenant compte du tore.
     * Par défaut, on retourne null.
     * @param d (Direction) la direction de la case voisine désirée
     * @return la case voisine dans la direction d (Cell)
     */
    public Cell neighbor(Direction d){
        // On détecte les éventuels problèmes liés au tore
        boolean leftProblem=(x==0 && d.equals(Direction.W));
        boolean rightProblem=(x==COLUMNS-1 && d.equals(Direction.E));
        boolean topProblem=(y==0 && d.equals(Direction.N));
        boolean bottomProblem=(y==ROWS-1 && d.equals(Direction.S));
        int indexOfThisCell=rowMajorIndex();
        
        // Si on est dans un cas problématique
        if(leftProblem){
            return ROW_MAJOR_ORDER.get(indexOfThisCell+COLUMNS-1);
        }
        else if(rightProblem){
            return ROW_MAJOR_ORDER.get(indexOfThisCell-COLUMNS+1);
        }
        else if(topProblem){
            return ROW_MAJOR_ORDER.get(indexOfThisCell+(ROWS-1)*COLUMNS);
        }
        else if(bottomProblem){
            return ROW_MAJOR_ORDER.get(indexOfThisCell-(ROWS-1)*COLUMNS);
        }
        
        // Si on se trouve au milieu du plateau
        else{
            switch(d){
            case E:
                return ROW_MAJOR_ORDER.get(indexOfThisCell+1);
            case W:
                return ROW_MAJOR_ORDER.get(indexOfThisCell-1);
            case S:
                return ROW_MAJOR_ORDER.get(indexOfThisCell+COLUMNS);
            case N:
                return ROW_MAJOR_ORDER.get(indexOfThisCell-COLUMNS);
            default:
                return null;
            }
        }
    }
    
    /**
     * Redéfinition de equals. Retourne vrai <=> that est une classe dont les coordonnées normalisées sont les mêmes que this.
     * @param that (Object) la cellule à tester
     * @return vrai <=> this=that (boolean)
     */
    @Override
    public boolean equals(Object that){
        // Vérifie si this et that sont des instances de la même classe
        if (that!=null && (that.getClass().equals(this.getClass()))){
            if (that instanceof Cell){
                
                // On vérifie si les coordonnées normalisées sont les mêmes
                Cell c = (Cell) that;
                return (this.x==c.x() && this.y==c.y());
            }
            return false;
        }
        return false;
    }
    
    /**
     * Retourne une valeur de hachage pour la case.
     * @return une valeur de hachage (int)
     */
    @Override
    public int hashCode(){
        return rowMajorIndex();
    }
    
    /**
     * Redéfinition de toString.
     * @return les coordonnées de la case au format (x, y) (String)
     */
    @Override
    public String toString(){
        return "("+x+", "+y+")";
    }
    
}
