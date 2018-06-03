package ch.epfl.xblast;

import java.util.NoSuchElementException;

/**
 * Enumération des 4 directions que l'on peut prendre dans le jeu.
 * Pour Nord, Est, Sud et West
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public enum Direction {
    N, E, S, W;

     /**
     * Retourne la direction opposée à laquelle on applique la méthode.
     * @return la direction opposée (Direction)
     * @throws NoSuchElementException
     */
    public Direction opposite() throws NoSuchElementException {
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
            throw new NoSuchElementException();
        }
    }

    /**
     * Indique si la direction est horizonzale ou non (E ou W).
     * @return vrai <=> la direction est E ou W (boolean)
     */
    public boolean isHorizontal(){
        return (this.equals(Direction.E) || this.equals(Direction.W));
    }

    /**
     * Indique si la direction this est parallèle à la direction that (that est elle même ou son opposée).
     * @param that (Direction ) la direction à tester
     * @return vrai <=> la direction est elle-même ou son opposée (boolean)
     */
    public boolean isParallelTo(Direction that){
        return (this.equals(that) || this.equals(that.opposite()));
    }

}
