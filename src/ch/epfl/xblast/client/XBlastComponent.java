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

        List<Image> boardImages = gameState.boardImages();
        List<Image> explosivesImages = gameState.explosivesImages();
        //Print des blocks, bombes et explosions
        for (int i = 0; i < boardImages.size(); i++) {
            
            //On print le board
            int x=i%Cell.COLUMNS*boardImages.get(i).getWidth(null);
            int y=(int) Math.floor((double) i/ (double) Cell.COLUMNS)*boardImages.get(i).getHeight(null);
            g.drawImage(boardImages.get(i), x, y, null);
            
            //On print les epxlosions et bombes
            g.drawImage(explosivesImages.get(i), x, y, null);
            
        }

        //Print du score
        int scoreX = 0;
        int scoreY=Cell.ROWS*gameState.boardImages().get(0).getHeight(null);
        for(Image s : gameState.scoreImages()){
            g.drawImage(s, scoreX, scoreY, null);
            scoreX = scoreX+s.getWidth(null);
        }
        
        //Print du nombre de vie
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        int[] xPos = new int[]{96, 240, 768, 912};
        int idImage = 0;
        for(Player p : gameState.players()){
            g.drawString(String.valueOf(p.lives()), xPos[idImage], 659);
            idImage++;
        }
        
        //Print du temps restant
        int scoreImageSize = gameState.timeLineImages().get(0).getWidth(null);
        int idScoreImage = 0;
        for(Image i:gameState.timeLineImages()){
            g.drawImage(i, idScoreImage*scoreImageSize, 672, null);
            idScoreImage++;
        }
       
        List<Player> orderedPlayers = gameState.players();
        //Collections.sort(orderedPlayers, (p1, p2) -> Integer.compare(p1.position().y(), p2.position().y()));
        
        for(Player p: orderedPlayers){
            g.drawImage(p.image(), 4*p.position().x()-24, 3*p.position().y()-52, null);
        }
        
    }
    
    public void setGameState(GameStateClient gameState, PlayerID playerId){
        this.gameState=gameState;
        this.playerId=playerId;
        repaint();
    }
}
