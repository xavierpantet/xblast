package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * Classe permettant de représenter un état de jeu mais adopté du point de vue du client
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class GameStateClient {
    private final List<Player> players;
    private final List<Image> boardImages;
    private final List<Image> explosivesImages;
    private final List<Image> scoreImages;
    private final List<Image> timeLineImages;

    /**
     * Constructeur
     * @param players (List<GameStateClient.Player>) la liste des joueurs
     * @param boardImages (List<Image>) la liste les images pour représenter le plateau de jeu
     * @param explosivesImages (List<Image>) la liste des images pour représenter les bombes et les explosions
     * @param scoreImages (List<Image>) la liste des images pour représenter les scores
     * @param timeLineImages (List<Image>) la liste des images pour représenter les données temporelles
     */
    public GameStateClient(List<GameStateClient.Player> players, List<Image> boardImages, List<Image> explosivesImages, List<Image> scoreImages, List<Image> timeLineImages) throws NullPointerException {
        this.players=Collections.unmodifiableList(new LinkedList<>(Objects.requireNonNull(players)));
        this.boardImages=Collections.unmodifiableList(new LinkedList<>(Objects.requireNonNull(boardImages)));
        this.explosivesImages=Collections.unmodifiableList(new LinkedList<>(Objects.requireNonNull(explosivesImages)));
        this.scoreImages=Collections.unmodifiableList(new LinkedList<>(Objects.requireNonNull(scoreImages)));
        this.timeLineImages=Collections.unmodifiableList(new LinkedList<>(Objects.requireNonNull(timeLineImages)));
    }

    public List<Player> players(){return players;}
    public List<Image> boardImages(){return boardImages;}
    public List<Image> explosivesImages(){return explosivesImages;}
    public List<Image> scoreImages(){return scoreImages;}
    public List<Image> timeLineImages(){return timeLineImages;}

    /**
     * Classe pouvant représenter un joueur pour le client dans le contexte de l'état de jeu
     * @author Xavier Pantet (260473), Timothée Duran (258683)
     */
    public final static class Player{
        private final PlayerID id;
        private final int lives;
        private final SubCell position;
        private final Image image;

        /**
         * Constructeur
         * @param id (PlayerID) l'identifiant
         * @param lives (int) le nombre de vies
         * @param position (SubCell) la position
         * @param image (Image) l'image représentant le joueur
         * @throws NullPointerException
         * @throws IllegalArgumentException
         */
        public Player(PlayerID id, int lives, SubCell position, Image image) throws NullPointerException, IllegalArgumentException {
            this.id=Objects.requireNonNull(id);
            this.lives=ArgumentChecker.requireNonNegative(lives);
            this.position=Objects.requireNonNull(position);
            this.image=image;
        }

        /**
         * Retourne l'identifiant du joueur
         * @return l'identifiant (PlayerID)
         */
        public PlayerID id(){return id;}

        /**
         * Retourne le nombre de vies du joueur
         * @return le nombre de vies (int)
         */
        public int lives(){return lives;}

        /**
         * Retourne la position du joueur
         * @return la position (SubCell)
         */
        public SubCell position(){return position;}

        /**
         * Retourne l'image à afficher pour le joueur
         * @return l'image à afficher (Image)
         */
        public Image image(){return image;}
    }
}
