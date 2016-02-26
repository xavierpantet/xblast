package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant une liste d'objets quelconques
 * @author Xavier Pantet (260473)
 */
public final class Lists {
    private Lists(){}

    /**
     * Retourne un miroir de la liste passée en pramètre ou IllegalArgumentException si
     * celle-ci est vide
     * @param la liste à laquelle on va appliquer le miroir
     * @return un miroir de la liste en entrée
     * @throws IllegalArgumentException
     */
    public static <T> List<T> mirrored(List<T> l) throws IllegalArgumentException{
        if(!l.isEmpty()){
            List<T> list = new ArrayList<T>(l);
            List<T> sub = new ArrayList<T>(list.subList(0, list.size()-1));
            Collections.reverse(sub);
            list.addAll(sub);
            return list;
        }else{throw new IllegalArgumentException("La liste ne peut pas être vide");}
    }
}
