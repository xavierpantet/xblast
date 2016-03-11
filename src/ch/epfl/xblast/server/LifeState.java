package ch.epfl.xblast.server;

public final class LifeState {
    
    private final int lives;
    private final State state;
    
    /**
     * Enum qui decrit les trois etats possibles d'un joueur
     * @author timotheedu
     *
     */
    public enum State {
        INVULNERABLE, VULNERABLE, DYING, DEAD;
    }
    
    /**
     * Constructeur de la classe
     * @param lives
     * @param state
     * @throws IllegalArgumentException
     * @throws NullPointerException
     */
    public LifeState(int lives, State state) throws IllegalArgumentException, NullPointerException{
        
        if(lives<0){
            throw new IllegalArgumentException();
        } else if (state==null){
            throw new NullPointerException();
        }
        
        this.lives = lives;
        this.state = state;
        
    }
    
    /**
     * Methode qui retorune le nombre de vie restant
     * @return this.lives
     */
    public int lives (){
       return this.lives;
    }
    
    /**
     * Méthode qui retourne l'état actuel d'un joueur
     * @return this.state
     */
    public State state(){
        return this.state;
    }
    
    /**
     * Methode qui retour true si je jour peut bouger et false sinon
     * @return
     */
    public boolean canMove(){
        if ((this.state==State.VULNERABLE)||(this.state==State.INVULNERABLE)){
            return true;
        } else {
            return false;
        }
    }

}
