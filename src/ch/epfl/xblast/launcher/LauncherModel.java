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
    
    private final String titleText = "XBlast";
    
    private final String initialButtonText = "START";
    private final String initialEtatText = "Entrez une adresse IP";
    private final Color initialEtatColor = Color.CYAN;
    private final ButtonState initialButtonState = ButtonState.START;
    private boolean initialTextFieldIsVisible = true;
    
    private final String connectingButtonText = "ANNULER";
    private final String connectingEtatText = "Connexion en cours à:";
    private final Color connectingEtatColor = Color.orange;
    private final ButtonState connectingButtonState = ButtonState.CANCEL;
    private boolean connectingTextFieldIsVisible = false;
    
    private final String connectedButtonText = "START";
    private final String connectedEtatText = "Partie en cours";
    private final Color connectedEtatColor = Color.green;
    private final ButtonState connectedButtonState = ButtonState.START;
    private boolean connectedTextFieldIsVisible = false;
    
    private final String errorButtonText = "START";
    private final String errorEtatText = "Erreur, réessayez";
    private final Color errorEtatColor = Color.red;
    private final ButtonState errorButtonState = ButtonState.START;
    private boolean errorTextFieldIsVisible = true;
    
    private final String notCorrectButtonText = "START";
    private final String notCorrectEtatText = "L'adresse IP n'est pas coorecte, réessayez";
    private final Color notCorrectEtatColor = Color.yellow;
    private final ButtonState notCorrectButtonState = ButtonState.START;
    private boolean notCorrectTextFieldIsVisible = true;
    
    public String getTitleText(){
        return titleText;
    }
    public void setInitialState(){
        etatText = initialEtatText;
        stateColor = initialEtatColor;
        buttonState = initialButtonState;
        buttonText = initialButtonText;
        textFieldIsVisible = initialTextFieldIsVisible;
        
        
        setChanged();
        notifyObservers();
    }
    
    public void setConnectingState(){
        etatText = connectingEtatText+IP;
        stateColor = connectingEtatColor;
        buttonState = connectingButtonState;
        buttonText = connectingButtonText;
        textFieldIsVisible = connectingTextFieldIsVisible;
        
        setChanged();
        notifyObservers();
    }
    
    public void setConnectedState(){
        etatText = connectedEtatText;
        stateColor = connectedEtatColor;
        buttonState = connectedButtonState;
        buttonText = connectedButtonText;
        textFieldIsVisible = connectedTextFieldIsVisible;
        
        setChanged();
        notifyObservers();
    }
    
    public void setErrorState(){
        etatText = errorEtatText;
        stateColor = errorEtatColor;
        buttonState = errorButtonState;;
        buttonText = errorButtonText;
        textFieldIsVisible = errorTextFieldIsVisible;
        
        setChanged();
        notifyObservers();
    }
    
    public void setNotCorrectState(){
        etatText = notCorrectEtatText;
        stateColor = notCorrectEtatColor;
        buttonState = notCorrectButtonState;
        buttonText = notCorrectButtonText;
        textFieldIsVisible = notCorrectTextFieldIsVisible;
        
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
