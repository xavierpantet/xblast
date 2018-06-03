package ch.epfl.xblast.server.graphics;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * Classe permettant de peindre les joueurs selon leurs caractéristiques.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class PlayerPainter {
    private PlayerPainter(){}

    /**
     * Retourne l'octet correspondant à l'image qui doit être utilisée pour peindre le joueur.
     * @param tick (int) le tick
     * @param player (Player) le joueur
     * @return l'octet de l'image correspondant au joueur
     */
    public static byte byteForPlayer(int tick, Player player){
        int byteCode=0;
        PlayerID id = player.id();
        State state = player.lifeState().state();
        Direction dir = player.direction();
        SubCell position = player.position();

        // Si le personnage ne clignotte pas, on commence dans la rangée de 20 correspondant au joueur
        if(!(state==State.INVULNERABLE && tick%2==1)){
            switch(id){
            case PLAYER_2:
                byteCode+=20;
                break;

            case PLAYER_3:
                byteCode+=40;
                break;

            case PLAYER_4:
                byteCode+=60;
                break;

            default:
            }
        }
        // Si le personnage doit clignotter, on passe directement dans les 80
        else { byteCode+=80; }

        // On gère le cas où il est DYING
        if(state==State.DYING){
            if(player.statesForNextLife().head().lives()>1){
                byteCode+=12;
            } else {
                byteCode+=13;
            }
            
        } else if(state==State.VULNERABLE || state==State.INVULNERABLE){
            // On sélectionne la bonne direction
            byteCode+=dir.ordinal()*3;

            // On sélectionne la bonne image pour les jambes
            int remainder=0;
            int nbOfPossibleStates=4;
            if(dir.isHorizontal()){
                remainder=position.x()%nbOfPossibleStates;
            }
            else{
                remainder=position.y()%nbOfPossibleStates;
            }

            if(remainder==1){
                byteCode+=1;
            }
            else if(remainder==3){
                byteCode+=2;
            }
            
        } else {
            byteCode=15;
        }
        
        return (byte) byteCode;
    }
}
