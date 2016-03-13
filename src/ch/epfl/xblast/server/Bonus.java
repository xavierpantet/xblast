package ch.epfl.xblast.server;

/**
 * Enumération des bonus du jeu
 * @author Xavier Pantet (260473)
 */
public enum Bonus {
    INC_BOMB {
      @Override
      public Player applyTo(Player player) {
          int nbBombs=player.maxBombs();
          return (nbBombs<9)? player.withMaxBombs(nbBombs+1) : player;
      }
    },

    INC_RANGE {
      @Override
      public Player applyTo(Player player) {
          int bombRange=player.bombRange();
          return (bombRange<9)? player.withBombRange(bombRange+1) : player;
      }
    };

    abstract public Player applyTo(Player player);
  }