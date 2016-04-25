package ch.epfl.xblast.client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class GameState {
    private final List<Player> players;
    private final List<Image> boardImages;
    private final List<Image> explosivesImages;
    private final List<Image> scoreImages;
    private final List<Image> timeLineImages;
    
    public GameState(List<Player> players, List<Image> boardImages, List<Image> explosivesImages, List<Image> scoreImages, List<Image> timeLineImages){
        this.players=Collections.unmodifiableList(new LinkedList<>(players));
        this.boardImages=Collections.unmodifiableList(new LinkedList<>(boardImages));
        this.explosivesImages=Collections.unmodifiableList(new LinkedList<>(explosivesImages));
        this.scoreImages=Collections.unmodifiableList(new LinkedList<>(scoreImages));
        this.timeLineImages=Collections.unmodifiableList(new LinkedList<>(timeLineImages));
    }
    
    public final static class Player{
        private final PlayerID id;
        private final int lives;
        private final SubCell position;
        private final Image image;
        
        public Player(PlayerID id, int lives, SubCell position, Image image){
            this.id=id;
            this.lives=lives;
            this.position=position;
            this.image=image;
        }
        
        public PlayerID id(){return id;}
        public int lives(){return lives;}
        public SubCell position(){return position;}
        public Image image(){return image;}
    }
}
