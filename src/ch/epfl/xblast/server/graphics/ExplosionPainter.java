package ch.epfl.xblast.server;

/**
 * Représente un peintre de bombes et d'explosions
 * @author timotheedu
 *
 */
public final class ExplosionPainter {
    
    public static final BYTE_FOR_EMPTY = 0b10000;
            
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
    public static byte byteForBomb(Bomb b){
        
        /*
         * il s'agit de l'image de la bombe noire, 
         * sauf si la longueur de la mèche est une puissance de deux, 
         * auquel cas il s'agit de l'image de la bombe blanche
         */
        
        //Si la longeur de meche n'est pas une puissance de 2
        if (b.fuseLength()&1==1){
           //bombe noir
            return (byte) BlockImage.BONUS_BOMB.ordinal();
        } 
        //Si c'est une puissance de 2
        else {
            return (byte) BlockImage.BONUS_BOMB.ordinal();
        }
        
        
    }
    
    public static byte byteForBlast(boolean cN, boolean cE, boolean cS, boolean cW){
        
        int imageNumber = 0b0000;
        
        if(cN==true){
            imageNumber+= 0b1000;
        }
        
        if(cE==true){
            imageNumber+= 0b0100;
        }
        
        if(cS==true){
            imageNumber+= 0b0010;
        }
        
        if(cW==true){
            imageNumber+= 0b0001;
        }
        
        return (byte) imageNumber;
              
    }

}
