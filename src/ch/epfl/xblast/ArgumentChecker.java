package ch.epfl.xblast;

/**
 * Classe permettant de vérifier qu'un entier est non négatif
 * @author Xavier Pantet (260473)
 */
public final class ArgumentChecker {
    private ArgumentChecker(){}
    
    /**
     * Permet de vérifier qu'un entier soit positif ou nul
     * @param la valeur à tester
     * @return la valeur si >=0
     * @throws IllegalArgumentException
     */
    public static int requireNonNegative(int value) throws IllegalArgumentException{
        if(value>=0){
            return value;
        }
        else{
            throw new IllegalArgumentException("La valeur doit être positive ou nulle");
        }
    }

}
