package ch.epfl.xblast;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe utilitaire permettant de compresser et décompresser une liste d'octets.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class RunLengthEncoder {
    private RunLengthEncoder(){}

    /**
     * Méthode de compression.
     * @param l (List<Byte>) la liste d'octets à compresser
     * @return Une liste des octets de "l" compressés
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public static List<Byte> encode(List<Byte> l) throws NullPointerException, IllegalArgumentException {
        if(!l.isEmpty()){
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

                        // Si le compteur est à 130, on devra créer une deuxième séquence car on ne
                        // peut pas représenter un nombre plus petit que -128 sur un entier signé
                        if(counter==130){
                            toReturn.add((byte) (-counter+2));
                            toReturn.add(lastElement);
                            counter=0;
                        }
                    }else{

                        // Sinon, si on a une répétition de 1 ou 2 éléments, on les ajoute tels quels
                        if(counter==1 || counter==2){
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
                }else{throw new IllegalArgumentException("Il ne peut pas y avoir de valeur négative");}
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
        else{throw new NullPointerException("La liste ne peut pas être vide");}
    }

    /**
     * Méthode de décompression.
     * @param l la liste à décompresser
     * @return  une liste des éléments de l décompressés
     * @throws IllegalArgumentException
     */
    public static List<Byte> decode(List<Byte> l) throws IllegalArgumentException {
        if(!l.isEmpty()){
            List<Byte> toReturn = new LinkedList<>();
            int nCopies=0;
            boolean trustNCopies=false;

            // Si le dernier élément est négatif, on lance une exception
            if(l.get(l.size()-1).compareTo((byte) 0) >= 0){

                // On parcourt les éléments de la liste
                for(Byte b : l){

                    // Si l'élément est négatif, il s'agit d'un nombre de répétitions, que l'on stocke en indiquant
                    // que cette valeur est à prendre en compte pour l'ajout des éléments suivants
                    if(b.compareTo((byte) 0) < 0){
                        nCopies=-b+2;
                        trustNCopies=true;
                    }

                    // Si l'élément est positif, il s'agit de l'élément à ajouter
                    else{

                        // Si on doit tenir compte d'un nombre de répétitions précédents, on l'ajoute nCopies fois
                        if(trustNCopies){
                            toReturn.addAll(Collections.nCopies(nCopies, b));
                            trustNCopies=false;
                        }

                        // Sinon, il s'agit d'un élément seul ou d'un doublet qui ne dépendent pas d'un nombre de répétitions
                        // donc on les ajoute simplement tels quels
                        else{
                            toReturn.add(b);
                        }
                    }
                }
            }else{throw new IllegalArgumentException("La liste ne peut pas se terminer par une valeur négative");}

            return toReturn;
        }
        else{throw new NullPointerException("La liste ne peut pas être vide");}
    }
}
