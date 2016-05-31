package ch.epfl.xblast.launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Cette classe représente tout ce qui est en rapport avec l'interface graphique, sur le modèle MWC
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 *
 */
public class LauncherView implements Observer {

    protected LauncherModel model;
    
    //bouton start
    protected JButton startButton = new JButton();
    
    //Text field (le texte pour entrer l'adresse IP)
    protected JFormattedTextField textField = new JFormattedTextField();
    
    //Label qui représente le titre
    protected Label titre = new Label();
    
    //Label qui indique l'état du launcher
    protected Label etat = new Label();
    
  //Label qui indique l'état du launcher
    protected JCheckBox localCheck = new JCheckBox("J'utilise cet ordinateur comme serveur");
    
    //Image qui représente le logo
    protected ImageIcon logo = new ImageIcon(new ImageIcon("images/icon.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT)); 
    protected JLabel image = new JLabel(logo);
    
    //Panel (contient les éléments de toute la fenetre)
    protected Panel panel = new Panel();
    
    //Panel (contient les éléments de toute la fenetre)
    protected Panel stateBackground = new Panel();
    
    //Contient une barre de texte et le bouton start
    protected Panel container = new Panel();
    
    //La fenêtre
    protected JFrame window = new JFrame("XBlast");

    public LauncherView(LauncherModel model){
        
        this.model = model;
        
        //le bouton
        startButton.setText(model.getButtonText());
        
        //etatText
        etat.setText(model.getEtatText());
        etat.setAlignment(Label.LEFT);
       
        //Observeur
        model.addObserver(this);
        
        //On créer les layouts
        container.setLayout(new BorderLayout());
        panel.setLayout(new BorderLayout());
        stateBackground.setLayout(new BorderLayout());
        
        //Taille du champs 
        textField.setColumns(10);
        
        //Le titre
        titre.setText(model.getTitleText());
        titre.setAlignment(Label.CENTER);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.getAllFonts();
        Font font = new Font("Jokerman", Font.PLAIN, 35);
        titre.setFont(font);
        
        //Le stateBackground
        stateBackground.setBackground(Color.BLUE);
        stateBackground.add(etat, BorderLayout.WEST);

        /*
         * AJOUTS DES ELEMENTS DANS LES CONTENEURS
         */
        container.add(startButton, BorderLayout.EAST);
        container.add(textField, BorderLayout.WEST);
        container.add(stateBackground, BorderLayout.NORTH);
        container.add(localCheck, BorderLayout.PAGE_END);
        
        panel.add(container, BorderLayout.SOUTH);
        panel.add(titre, BorderLayout.NORTH);
        panel.add(image, BorderLayout.CENTER);
        
        // On créer la fenêtre
        
        //Définit sa taille : 400 pixels de large et 400 pixels de haut
        window.setSize(400, 400);
        //Nous demandons maintenant à notre objet de se positionner au centre
        window.setLocationRelativeTo(null);
        //Termine le processus lorsqu'on clique sur la croix rouge
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(panel);
        //Et enfin, la rendre visible   
        window.setVisible(true);
        
    }

    @Override
    /**
     * Méthode qui met à jour les composants
     */
    public void update(Observable o, Object arg) {
       
        etat.setText(model.getEtatText());
        stateBackground.setBackground(model.getStateColor());
        textField.setVisible(model.getTextFieldIsVisible());
        localCheck.setVisible(model.getCheckBoxIsVisible());
        startButton.setText(model.getButtonText());
        
        if(model.getServerMode()){
            //Si on est en mode serveur alors la checkBox est cochée
            localCheck.setSelected(true);
        } else {
            //Sinon, la case n'est pas cochée
            localCheck.setSelected(false);
        }
    }
    
}
