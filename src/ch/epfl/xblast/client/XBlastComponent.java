package ch.epfl.xblast.client;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import ch.epfl.xblast.PlayerID;

public final class XBlastComponent extends JComponent {
    GameStateClient gameState=null;
    PlayerID playerId=null;
    
    public Dimension getPreferredSize(){
        return new Dimension(960, 688);
    }
    
    protected void paintComponent(Graphics g){
        
    }
    
    public void setGameState(GameStateClient gameState, PlayerID playerId){
        this.gameState=gameState;
        this.playerId=playerId;
        repaint();
    }
}
