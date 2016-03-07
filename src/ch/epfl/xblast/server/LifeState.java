package ch.epfl.xblast.server;

public final class LifeState {
    
    private final int lives;
    private final State state;
    
    public enum State {
        INVULNERABLE, VULNERABLE, DYING, DEAD;
    }
    
    public LifeState(int lives, State state) throws IllegalArgumentException, NullPointerException{
        
        if(lives<0){
            throw new IllegalArgumentException();
        } else if (state==null){
            throw new NullPointerException();
        }
        
        this.lives = lives;
        this.state = state;
        
    }
    
    public int lives (){
       return this.lives;
    }
    
    public State state(){
        return this.state;
    }
    
    public boolean canMove(){
        if ((this.state==State.VULNERABLE)||(this.state==State.INVULNERABLE)){
            return true;
        } else {
            return false;
        }
    }

}
