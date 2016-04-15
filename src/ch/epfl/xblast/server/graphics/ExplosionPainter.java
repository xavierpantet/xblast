package ch.epfl.xblast.server;

/**
 * Représente un peintre de bombes et d'explosions
 * @author timotheedu
 *
 */
public final class ExplosionPainter {
    
    /**
     * Constructeur par défaut privé car la classe n'est pas instanciable
     */
    private ExplosionPainter(){
        
    }
    
    /**
     * Retourne l'octet identifiant l'image à utiliser pour dessiner la bombe qu'on lui passe en argument.
     * @param b (Bomb) la bombe à dessiner
     * @return l'octet (byte) identifiant l'image à utiliser pour dessiner la bombe qu'on lui passe ne argument.
     */
    static final byte byteForBomb(Bomb b){
        
        /*
         * il s'agit de l'image de la bombe noire, 
         * sauf si la longueur de la mèche est une puissance de deux, 
         * auquel cas il s'agit de l'image de la bombe blanche
         */
        
        return null;
    }

}
