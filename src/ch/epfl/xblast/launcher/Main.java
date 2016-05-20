package ch.epfl.xblast.launcher;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ch.epfl.xblast.client.Main;

/**
 * Cette classe lance une fenêtre java qui permet de lancer le jeu
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 *
 */
public class Main {

    public static void main(String[] args) {
        
        //On créer le panel
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        
        //On créer le boutton
        Button startButton = new Button("START");
        panel.add(startButton, BorderLayout.SOUTH);
        
        //Le label
        Label titre = new Label("XBLast");
        titre.setAlignment(Label.CENTER);
        
        String tableauChaine[] = {"128.179.190.79"};
        startButton.addActionListener(e ->Main.main(tableauChaine));
        
        
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.getAllFonts();

        Font font = new Font("Jokerman", Font.PLAIN, 35);
        
        titre.setFont(font);
        
        panel.add(titre, BorderLayout.NORTH);

        //Le logo
        ImageIcon logo = new ImageIcon(new ImageIcon("images/icon.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
                       
        JLabel image = new JLabel(logo);
        
        panel.add(image, BorderLayout.CENTER);
        
        JFormattedTextField textField = new JFormattedTextField();
        textField.setValue(new Integer(5));
        textField.setColumns(10);
        
        panel.add(textField, BorderLayout.PAGE_START);
        
        
        // On créer la fenêtre
        JFrame window = new JFrame("XBLast");
        //Définit sa taille : 400 pixels de large et 100 pixels de haut
        window.setSize(400, 400);
        //Nous demandons maintenant à notre objet de se positionner au centre
        window.setLocationRelativeTo(null);
        //Termine le processus lorsqu'on clique sur la croix rouge
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.add(panel);
     
        
        window.setIconImage(new ImageIcon("images/icon.png").getImage());

        //Et enfin, la rendre visible   
        window.setVisible(true);
        
        
        

    }

}
