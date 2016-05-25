package ch.epfl.xblast.server;

/**
 * Enumération des bonus du jeu.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public enum Bonus {
    
    /**
     * Définition concrète de applyTo. Retourne un joueur à qui l'on a fait consommer un bonus INC_BOMB, s'il en a le droit.
     * @param player (Player) le joueur qui consomme un bonus
     * @return un nouveau joueur dont les capacités sont améliorées (Player)
     */
    INC_BOMB {
        @Override
        public Player applyTo(Player player) {
            int nbBombs=player.maxBombs();
            return (nbBombs<MAX_BOMBS)? player.withMaxBombs(nbBombs+1) : player;
        }
    },

    /**
     * Définition concrète de applyTo. Retourne un joueur à qui l'on a fait consommer un bonus INC_RANGE, s'il en a le droit.
     * @param player (Player) le joueur qui consomme le bonus
     * @return un nouveau joueur dont les capacités sont améliorées (Player)
     */
    INC_RANGE {
        @Override
        public Player applyTo(Player player) {
            int bombRange=player.bombRange();
            return (bombRange<MAX_BOMBS)? player.withBombRange(bombRange+1) : player;
        }
    },
    
    /**
     * BONUS
     * Définition concrète de applyTo.
     * Retourne un joueur ayant une vie de plus.
     * @param player (Player) le joueur qui consomme le bonus
     * @return un nouveau joueur ayant une vie supplémentaire
     */
    INC_LIFE {
        @Override
        public Player applyTo(Player player){
            return player.withLives(player.lives()+1);
        }
        
    };
    
    private static final int MAX_BOMBS = 9;

    abstract public Player applyTo(Player player);

}