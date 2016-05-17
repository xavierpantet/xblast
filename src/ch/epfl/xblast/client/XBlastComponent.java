package ch.epfl.xblast.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateClient.Player;

/**
 * Classe décrivant un composant XBlast, notamment permettra son affichage avec Swing
 * @author Xavier Pantet (260473) & Timothée Duran (258683)
 */
@SuppressWarnings("serial")
public final class XBlastComponent extends JComponent {
    GameStateClient gameState=null;
    PlayerID playerId=null;
    
    @Override
    /**
     * Redéfinition de getPreferredSize.
     * Retourne les dimensions de la fenètre
     */
    public Dimension getPreferredSize(){
        return new Dimension(960, 688);
    }
    
    @Override
    /**
     * Redéfinition de paintComponent
     * Permet de peindre le composant (l'afficher à l'écran)
     * @param g0    le contexte graphique de l'affichage
     */
    protected void paintComponent(Graphics g0){
        Graphics2D g = (Graphics2D) g0;
        
        /*
         * Affichage des blocs et des explosions
         */
        Iterator<Image> boardImagesIterator=gameState.boardImages().iterator();
        Iterator<Image> explosiveImagesIterator=gameState.explosivesImages().iterator();
        int loopCounter=0;
        
        while(boardImagesIterator.hasNext() && explosiveImagesIterator.hasNext()){
            Image boardImage = boardImagesIterator.next();
            Image explosiveImage = explosiveImagesIterator.next();
            
            int x = loopCounter%Cell.COLUMNS*boardImage.getWidth(null);
            int y=(int) Math.floor((double) loopCounter / (double) Cell.COLUMNS)*boardImage.getHeight(null);
            g.drawImage(boardImage, x, y, null);
            g.drawImage(explosiveImage, x, y, null);
            loopCounter++;
        }

        /*
         * Affichage du score
         */
        int scoreX = 0;
        int scoreY=Cell.ROWS*gameState.boardImages().get(0).getHeight(null);
        for(Image s : gameState.scoreImages()){
            g.drawImage(s, scoreX, scoreY, null);
            scoreX = scoreX+s.getWidth(null);
        }
        
        /*
         * Affichage du nombre de vies des joueurs
         */
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        int[] xPos = new int[]{96, 240, 768, 912}; // les positions selon l'axe x
        int idImage = 0;
        for(Player p : gameState.players()){
            g.drawString(String.valueOf(p.lives()), xPos[idImage], 659);
            idImage++;
        }
        
        /*
         * Affichage de la barre temporelle
         */
        int scoreImageSize = gameState.timeLineImages().get(0).getWidth(null);
        int idScoreImage = 0;
        for(Image i:gameState.timeLineImages()){
            g.drawImage(i, idScoreImage*scoreImageSize, 672, null);
            idScoreImage++;
        }
       
        /*
         * Affichage des joueurs
         */
        
        // On récupère les joueurs et on les stocke dans un nouveau tableau modifiable
        List<Player> players = new LinkedList<>(gameState.players());
        
        // On crée un comparateur qui va trier les joueurs selon leur position y, puis si nécessaire selon leur identifiant
        Comparator<Player> yPositionComparator = (p1, p2) -> Integer.compare(p1.position().y(), p2.position().y());
        Comparator<Player> idComparator = (p1, p2) -> {
            int val1 = Math.floorMod(p1.id().ordinal()-playerId.ordinal()-1, 4);
            int val2 = Math.floorMod(p2.id().ordinal()-playerId.ordinal()-1, 4);
            return Integer.compare(val1, val2);
        };
        Collections.sort(players, yPositionComparator.thenComparing(idComparator)); // On trie
        
        // On affiche
        for(Player p: players){
            g.drawImage(p.image(), 4*p.position().x()-24, 3*p.position().y()-52, null);
        }
        
    }
    
    /**
     * Permet de mettre à jour le GameState et de le redessiner
     * @param gameState le nouveau GameState
     * @param playerId  l'identifiant du joueur pour lequel on affiche
     */
    public void setGameState(GameStateClient gameState, PlayerID playerId){
        this.gameState=gameState;
        this.playerId=playerId;
        repaint();
    }
}
