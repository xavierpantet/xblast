package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {
    private Lists(){}

    public static <T> List<T> mirrored(List<T> l) throws IllegalArgumentException{
        if(!l.isEmpty()){
            return l.addAll(Collections.reverse(l).subList(1, l.size()));
            
        }else{throw new IllegalArgumentException("La liste ne peut pas être vide");}
    }
}
