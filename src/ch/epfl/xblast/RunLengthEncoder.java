package ch.epfl.xblast;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe utilitaire permettant de compresser et décompresser une liste d'octets.
 * @author Xavier Pantet (260473)
 */
public final class RunLengthEncoder {
    private RunLengthEncoder(){}
    
    /**
     * Méthode de compression.
     * @param l la liste d'octets à compresser
     * @return une liste compressés des octets de l
     */
    public static List<Byte> encode(List<Byte> l) throws IllegalArgumentException {
        
        // Il nous faut un compteur pour détecter les répétitions et de quoi stocker le dernier élément testé
        int counter=0;
        Byte lastElement=l.get(0);
        
        // Le tableau de retour
        List<Byte> toReturn = new LinkedList<>();
        
        // On parcourt tous les octets du tableau
        for(Byte b : l){
            // On vérifie qu'ils sont positifs ou nuls
            if((b.compareTo((byte) 0) >= 0)){
                
                // Si l'octet est égal au précédent, on incrémente simplement le compteur
                if(b.equals(lastElement)){
                    counter++;
                }
                else{
                    
                    // Sinon, si on a une répétition de 1 ou 2 éléments, on les ajoute tels quels
                    if(counter>0 && counter<=2){
                        toReturn.addAll(Collections.nCopies(counter, lastElement));
                    }
                    
                    // Si le compteur est à 0, c'est qu'on a détecté un élément unique
                    else if(counter==0){
                        toReturn.add(b);
                    }
                    
                    // Si on a plus de 2 éléments, on ajoute le compteur et l'élément en question
                    else{
                        toReturn.add((byte) (-counter+2));
                        toReturn.add(lastElement);
                    }
                    
                    // On restaure le compteur et on mémorise le nouvel élément
                    counter=1;
                    lastElement=b;
                }
            }
            else{throw new IllegalArgumentException("Il ne peut pas y avoir de valeur négative");}
        }
        
        // On doit encore gérer les derniers cas que la boucle aurait pu oublier
        // Si le compteur est supérieur à 2, on ajoute le compteur et le dernier élément
        if(counter>2){
            toReturn.add((byte) (-counter+2));
        }
        
        // Si le compteur vaut 2, on ajoute 2x le dernier élément
        else if(counter==2){
            toReturn.add(lastElement);
        }
        toReturn.add(lastElement);
        return toReturn;
    }
    
    /**
     * Méthode de décompression.
     * @param l la liste à décompresser
     * @return  une liste des éléments de l décompressés
     * @throws IllegalArgumentException
     */
    public static List<Byte> decode(List<Byte> l) throws IllegalArgumentException {
        List<Byte> toReturn = new LinkedList<>();
        int nCopies=0;
        boolean trustNCopies=false;
        
        if(l.get(l.size()-1).compareTo((byte) 0) >= 0){
            for(Byte b : l){
                if(b.compareTo((byte) 0) < 0){
                    nCopies=-b+2;
                    trustNCopies=true;
                }
                else{
                    if(trustNCopies){
                        toReturn.addAll(Collections.nCopies(nCopies, b));
                        trustNCopies=false;
                    }
                    else{
                        toReturn.add(b);
                    }
                }
            }
        }else{throw new IllegalArgumentException("La liste ne peut pas se terminer par une valeur négative");}
        
        return toReturn;
    }

}
