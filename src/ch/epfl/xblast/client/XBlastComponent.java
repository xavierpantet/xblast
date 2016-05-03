package ch.epfl.xblast.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateClient.Player;

public final class XBlastComponent extends JComponent {
    GameStateClient gameState=null;
    PlayerID playerId=null;
    
    public Dimension getPreferredSize(){
        return new Dimension(960, 688);
    }
    
    protected void paintComponent(Graphics g0){
        Graphics2D g = (Graphics2D) g0;
        Iterator<Image> boardIterator = gameState.boardImages().iterator();
        Iterator<Image> explosionIterator = gameState.explosivesImages().iterator();

        
        int idImage=0;
        while(boardIterator.hasNext() && explosionIterator.hasNext()){
            Image currentBoard=boardIterator.next();
            int x=idImage%Cell.COLUMNS*currentBoard.getWidth(null);
            int y=(int) Math.floor((double) idImage/ (double) Cell.COLUMNS)*currentBoard.getHeight(null);
            g.drawImage(currentBoard, x, y, null);
            
            Image currentExplosion = explosionIterator.next();
            g.drawImage(currentExplosion, x, y, null);
            idImage++;
        }
        
        /*for(Image s : gameState.scoreImages()){
            int x=idImage%Cell.COLUMNS*s.getWidth(null);
            int y=Cell.ROWS+1;
            g.drawImage(s, x, y, null);
            idImage++;
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        int[] xPos = new int[]{96, 240, 768, 912};
        idImage=0;
        for(Player p : gameState.players()){
            g.drawString(String.valueOf(p.lives()), xPos[idImage], 659);
            idImage++;
        }
        
        List<Player> orderedPlayers = gameState.players();
        //Collections.sort(orderedPlayers, (p1, p2) -> Integer.compare(p1.position().y(), p2.position().y()));
        
        for(Player p: orderedPlayers){
            g.drawImage(p.image(), 4*p.position().x()-24, 3*p.position().y()-52, null);
        }*/
        
    }
    
    public void setGameState(GameStateClient gameState, PlayerID playerId){
        this.gameState=gameState;
        this.playerId=playerId;
        repaint();
    }
}
