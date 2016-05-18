package ch.epfl.xblast.server.graphics;

import ch.epfl.xblast.server.Bomb;

/**
 * Classe permettant de peindre les bombes et explosions en fonction des images que l'on a pour représenter chaque cas.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 *
 */
public final class ExplosionPainter {
    
    //Byte représentant une image vide
    public static final byte BYTE_FOR_EMPTY = 0b10000;
            
    /**
     * Constructeur par défaut privé car la classe n'est pas instanciable
     */
    private ExplosionPainter(){}
    
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
        return (byte) (((b.fuseLength()&(b.fuseLength()-1))==0) ? 21:20);   
    }
    
    /**
     * Retourne l'octet identifiant l'image à utiliser pour dessiner l'exposion qu'on lui passe en argument. 
     * Prend en arguments quatre booléens exprimant la présence ou l'absence d'une particule d'explosion dans chaque case voisine d'une case contenant une particule d'explosion. 
     * Elle retourne l'octet correspondant à l'image à utiliser.
     * @param cN (boolean) true si une particule d'expolosion est présente dans la case voisine NORD
     * @param cE (boolean) true si une particule d'expolosion est présente dans la case voisine EST
     * @param cS (boolean) true si une particule d'expolosion est présente dans la case voisine SUD
     * @param cW (boolean) true si une particule d'expolosion est présente dans la case voisine OUEST
     * @return
     */
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
