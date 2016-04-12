package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * Enumération des différents types de blocs et des méthodes associées.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public enum Block {
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE);

    private Bonus maybeAssociatedBonus;

    /**
     * Constructeur par défaut.
     */
    private Block() {
        this.maybeAssociatedBonus=null;
    }

    /**
     * Constructeur pour les bonus.
     * @param maybeAssociatedBonus (Bonus) le bonus associé
     */
    private Block(Bonus maybeAssociatedBonus) {
        this.maybeAssociatedBonus=maybeAssociatedBonus;
    }

    /**
     * Indique si le bloc est libre
     * @return vrai <=> le bloc est libre (boolean)
     */
    public boolean isFree(){
        return this.equals(Block.FREE);
    }

    /**
     * Indique si le bloc peut contenir un joueur.
     * @return vrai <=> le bloc peut contenir un joueur (boolean)
     */
    public boolean canHostPlayer(){
        return (this == Block.FREE || this.isBonus());
    }

    /**
     * Indique si le bloc projette une ombre sur le plateau de jeu.
     * @return vrai <=> le bloc projette une ombre sur le plateau de jeu (boolean)
     */
    public boolean castsShadow(){
        return (this.equals(Block.INDESTRUCTIBLE_WALL) || this.equals(Block.DESTRUCTIBLE_WALL) || this.equals(Block.CRUMBLING_WALL));
    }

    /**
     * Indique si le bloc est un bonus.
     * @return vrai <=> le bloc est un bonus (boolean)
     */
    public boolean isBonus(){
        return (this == Block.BONUS_BOMB || this == Block.BONUS_RANGE);
    }

    /**
     * Retourne le bonus associé au bloc s'il existe, ou retroune l'erreur "NoSuchElementException" sinon
     * @return le bonus associé (Bonus)
     * @throws NoSuchElementException
     */
    public Bonus associatedBonus() throws NoSuchElementException {
        if(maybeAssociatedBonus!=null){
            return maybeAssociatedBonus;
        }else{throw new NoSuchElementException("Aucun bonus correspondant");}
    }
}