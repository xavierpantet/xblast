package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Classe générique représentant une liste d'objets quelconques.
 * Fournit uniquement des méthodes statiques utilitaires applicables à des listes.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class Lists {
    private Lists(){}

    /**
     * Retourne un miroir de la liste passée en paramètre ou IllegalArgumentException si celle-ci est vide.
     * @param l (List<T>) la liste à laquelle on va appliquer le miroir
     * @return un miroir de la liste l (List<T>)
     * @throws IllegalArgumentException
     */
    public static <T> List<T> mirrored(List<T> l) throws IllegalArgumentException{
        
        //Si la liste passée n'est pas vide
        if(!l.isEmpty()){
            // On copie l
            List<T> list = new ArrayList<T>(l);
            List<T> sub = new ArrayList<T>(list.subList(0, list.size()-1));
            
            // On inverse la sous-liste
            Collections.reverse(sub);
            
            // On concatène les deux listes
            list.addAll(sub);
            return list;
        } else {
            throw new IllegalArgumentException("La liste ne peut pas être vide");
        }
    }
    
    /**
     * Calcule de manière récursive l'ensemble des combinaisons possibles des éléments d'une sur une liste.
     * @param l (List<T>) la liste dont on veut calculer les permutations
     * @return une liste des combinaisons des éléments de l (List<List<T>>)
     */
    public static <T> List<List<T>> permutations(List<T> l){
        int size = l.size();
        
        // Si la liste contient 0 ou 1 élément
        if(size==0 || size==1){
            // On retourne un tableau de tableaux vide, ou un tableau contenant un tableau du premier élément
            List<List<T>> trivialArray = (size==0)? new ArrayList<>(Arrays.asList(Arrays.asList())) : new ArrayList<>(Arrays.asList(Arrays.asList(l.get(0))));
            return trivialArray;
        } else {
            T firstElement = l.get(0);
            
            /*
             *  BOTTOM OF THE RECURSION
             *  Permutations d'un tableau de deux éléments: [X, Y] --> [[X, Y], [Y, X]]
             */
            if(size==2){
                List<List<T>> queue = new ArrayList<>();
                queue.add(new ArrayList<>(l));
                Collections.reverse(l);
                queue.add(new ArrayList<>(l));
                return queue;
            }
            
            // RECURSION
            else{
                // On applique la récursion sur la queue du tableau
                List<List<T>> recursive = new ArrayList<>();
                recursive.addAll(permutations(l.subList(1, size)));
                
                List<T> temp;
                List<List<T>> headPermut=new ArrayList<>();
                int elementSize=0;
                
                // Pour chaque élément de recursive
                for(List<T> element:recursive){
                    elementSize=element.size();
                    
                    // On ajoute la tête à toutes les places possibles
                    for(int i=0; i<=elementSize; i++){
                        temp=new ArrayList<>(element);
                        temp.add(i, firstElement);
                        headPermut.add(temp);
                    }
                }
                return headPermut;
            }
        }
    }
    
}
