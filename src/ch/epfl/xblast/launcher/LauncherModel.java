package ch.epfl.xblast.launcher;

import java.awt.Color;
import java.util.Observable;

public class LauncherModel extends Observable {

    private String IP;
    private String etatText;
    private String buttonText;
    private Color stateColor;
    private ButtonState buttonState = ButtonState.START;
    private boolean textFieldIsVisible; 
    private boolean checkBoxIsVisible; 
    private final String titleText = "XBlast";
    private boolean serverMode;
    
    public String getTitleText(){
        return titleText;

    }
    
    public boolean getServerMode(){
        return serverMode;
    };
    
    
    public void setServerMode(){
        etatText = "Le serveur est sur cet ordianteur";
        stateColor = Color.CYAN;
        buttonState = ButtonState.START;
        buttonText = "START";
        textFieldIsVisible = false;
        checkBoxIsVisible = true;
        serverMode = true;
        
        
        setChanged();
        notifyObservers();
    }
    
    public void setInitialState(){
        etatText = "Entrez une adresse IP";
        stateColor = Color.CYAN;
        buttonState = ButtonState.START;
        buttonText = "START";
        textFieldIsVisible = true;
        checkBoxIsVisible = true;
        serverMode=false;

        setChanged();
        notifyObservers();
    }
    
    public void setConnectingState(){
        etatText = "Connexion en cours à:"+IP;
        stateColor = Color.orange;
        buttonState = ButtonState.CANCEL;
        buttonText = "ANNULER";
        textFieldIsVisible = false;
        checkBoxIsVisible = false;
        serverMode=false;
        setChanged();
        notifyObservers();
    }
    
    public void setConnectedState(){
        etatText = "Partie en cours";
        stateColor = Color.green;
        buttonState = ButtonState.START;
        buttonText = "START";
        textFieldIsVisible = false;
        checkBoxIsVisible = false;
        serverMode=false;
        
        setChanged();
        notifyObservers();
    }
    
    public void setErrorState(){
        etatText = "Erreur, réessayez";
        stateColor = Color.red;
        buttonState = ButtonState.START;
        buttonText = "START";
        textFieldIsVisible = true;
        checkBoxIsVisible = true;
        serverMode=false;
        
        setChanged();
        notifyObservers();
    }
    
    public void setNotCorrectState(){
        etatText = "L'adresse IP n'est pas coorecte, réessayez";
        stateColor = Color.yellow;
        buttonState = ButtonState.START;
        buttonText = "START";
        textFieldIsVisible = true;
        checkBoxIsVisible = true;
        serverMode=false;

        setChanged();
        notifyObservers();
    }

    public String getButtonText(){
        return buttonText;
    }
    
    public String getIP(){
        return IP;
    }
    
    public void setIP(String ip){
        IP = ip;
        setChanged();
        notifyObservers();
    }
    
    public String getEtatText(){
        return etatText;
        
    }
    
    public boolean getTextFieldIsVisible(){
        return textFieldIsVisible;
        
    }
    
    public boolean getCheckBoxIsVisible(){
        return checkBoxIsVisible;
        
    }
    
    public void setEtatText(String etatText){
        this.etatText = etatText;
        setChanged();
        notifyObservers();
    }
    
    public Color getStateColor(){
        return stateColor;
    }
    
    public void setStateColor(Color stateColor){
        this.stateColor = stateColor;
        setChanged();
        notifyObservers();
    }
    
    public void setButtonState(ButtonState state){
        buttonState = state;
        setChanged();
        notifyObservers();
    }
    
    public ButtonState getButtonState(){
       return buttonState;
    }
   
}
