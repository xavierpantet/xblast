package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * Enumération des différents types de blocs et des méthodes associées.
 * @author Xavier Pantet (260473)
 */
public enum Block {
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE);
    
    private Bonus maybeAssociatedBonus;
    
    /**
     * Constructeur pour les bonus
     * @param le bonus associé
     */
    private Block(Bonus maybeAssociatedBonus) {
        this.maybeAssociatedBonus=maybeAssociatedBonus;
    }
    
    /**
     * Constructeur par défaut
     */
    private Block() {
        this.maybeAssociatedBonus=null;
    }
    
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
        return (this.equals(Block.FREE) || this.isBonus());
    }
    
    /**
     * Retourne vrai <=> le bloc projette une ombre sur le plateau de jeu
     * @return vrai <=> le bloc projette une ombre sur le plateau de jeu
     */
    public boolean castsShadow(){
        return (this.equals(Block.INDESTRUCTIBLE_WALL) || this.equals(Block.DESTRUCTIBLE_WALL) || this.equals(Block.CRUMBLING_WALL));
    }
    
    /**
     * Retour vrai <=> le bloc est un bonus
     * @return vrai <=> le bloc est un bonus
     */
    public boolean isBonus(){
        return (this.equals(BONUS_BOMB) || this.equals(BONUS_RANGE));
    }
    
    /**
     * Retourne le bonus associé au bloc s'il existe, NoSuchElementException sinon
     * @return le bonus associé
     * @throws NoSuchElementException
     */
    public Bonus associatedBonus() throws NoSuchElementException {
        if(maybeAssociatedBonus!=null){
            return maybeAssociatedBonus;
        }else{throw new NoSuchElementException("Aucun bonus correspondant");}
    }
}