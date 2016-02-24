package ch.epfl.xblast.server;

/**
 * Enumération des différents types de blocs et des méthodes associées
 * @author Xavier Pantet (260473)
 */
public enum Block {
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL;
    
    /**
     * Retourne vrai <=> le bloc est libre
     * @return vrai <=> le bloc est libre
     */
    public boolean isFree(){
        return this.equals(Block.FREE);
    }
    
    /**
     * Retourne vrai <=> le bloc peut contenir un joueur
     * @return vrai <=> le bloc peut contenir un joueur
     */
    public boolean canHostPlayer(){
        return this.equals(Block.FREE);
    }
    
    /**
     * Retourne vrai <=> le bloc projette une ombre sur le plateau de jeu
     * @return vrai <=> le bloc projette une ombre sur le plateau de jeu
     */
    public boolean castsShadow(){
        return (this.equals(Block.INDESTRUCTIBLE_WALL) || this.equals(Block.DESTRUCTIBLE_WALL) || this.equals(Block.CRUMBLING_WALL));
    }
}