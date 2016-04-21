package ch.epfl.xblast.server.graphics;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * Classe permettant de peindre les joueurs selon leurs caractéristiques.
 * @author Xavier Pantet (260473)
 */
public final class PlayerPainter {
    private PlayerPainter(){}
    
    /**
     * Retourne l'octet correspondant à l'image qui doit être utilisée pour peindre le joueur.
     * @param tick  le tick
     * @param player    le joueur
     * @return
     */
    public static byte byteForPlayer(int tick, Player player){
        int byteCode=0;
        PlayerID id = player.id();
        State state = player.lifeState().state();
        Direction dir = player.direction();
        SubCell position = player.position();
        
        if(!(state==State.INVULNERABLE && tick%2==0)){
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
        else{
            byteCode+=80;
        }
        
        if(state==State.DYING){
            if(player.statesForNextLife().head().lives()>1){
                byteCode+=12;
            }
            else{
                byteCode+=13;
            }
        }
        
        else if(state==State.VULNERABLE || state==State.INVULNERABLE){
            byteCode+=dir.ordinal()*3;
            
            int remainder=0;
            if(dir.isHorizontal()){
                remainder=position.x()%4;
            }
            else{
                remainder=position.y()%4;
            }
            
            if(remainder==1){
                byteCode+=1;
            }
            else if(remainder==3){
                byteCode+=2;
            }
        }
        
        return (byte) byteCode;
    }
}
