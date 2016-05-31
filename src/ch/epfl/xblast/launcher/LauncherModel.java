package ch.epfl.xblast.launcher;

import java.awt.Color;
import java.util.Observable;

/**
 * Cette classe représente le modèle (sur le modèle de l'architecture MVC), de la fenêtre de lancement du jeu.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 *
 */
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
        stateColor = Color.orange;
        buttonState = ButtonState.CANCEL;
        buttonText = "ANNULER";
        textFieldIsVisible = false;
        checkBoxIsVisible = false;
        if(serverMode){
            etatText = "Attente des joueurs";
        } else {
            etatText = "Connexion en cours à:"+IP;
        }
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

    public void setEtatText(String etatText){
        this.etatText = etatText;
        setChanged();
        notifyObservers();
    }
    
    public void setIP(String ip){
        IP = ip;
        setChanged();
        notifyObservers();
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

    public boolean getServerMode(){
        return serverMode;
    };

    public String getButtonText(){
        return buttonText;
    }

    public String getIP(){
        return IP;
    }

    public String getEtatText(){
        return etatText;

    }

    public boolean getTextFieldIsVisible(){
        return textFieldIsVisible;

    }
    
    public String getTitleText(){
        return titleText;

    }

    public boolean getCheckBoxIsVisible(){
        return checkBoxIsVisible;

    }

    public Color getStateColor(){
        return stateColor;
    }

    public ButtonState getButtonState(){
        return buttonState;
    }

}
