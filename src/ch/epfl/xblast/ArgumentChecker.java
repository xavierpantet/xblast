package ch.epfl.xblast;

/**
 * Classe utilitaire permettant de vérifier la validité d'arguments de méthodes.
 * Permet notamment de facilier l'écriture des constructeurs.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class ArgumentChecker {
    private ArgumentChecker(){}
    
    /**
     * Permet de vérifier qu'un entier est positif ou nul.
     * @param value (int) la valeur à vérifier 
     * @return value (int) si >=0 ou lève IllegalArgumentException sinon 
     * @throws IllegalArgumentException
     */
    public static int requireNonNegative(int value) throws IllegalArgumentException{
        if(value>=0){return value;}
        else{throw new IllegalArgumentException("La valeur doit être positive ou nulle");}
    }

}
