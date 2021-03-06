package ch.epfl.xblast.server;

import ch.epfl.xblast.Time;

/**
 * Interface décrivant le nombre de pas de différents éléments du jeu
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public interface Ticks {
    static final int PLAYER_DYING_TICKS=8;
    static final int PLAYER_INVULNERABLE_TICKS=64;
    static final int BOMB_FUSE_TICKS=100;
    static final int EXPLOSION_TICKS=30;
    static final int WALL_CRUMBLING_TICKS=EXPLOSION_TICKS;
    static final int BONUS_DISAPPEARING_TICKS=EXPLOSION_TICKS;

    static final int TICKS_PER_SECOND=20;
    static final int TICK_NANOSECOND_DURATION=Time.NS_PER_S/TICKS_PER_SECOND;
    static final int TOTAL_TICKS=TICKS_PER_SECOND*120;
}
