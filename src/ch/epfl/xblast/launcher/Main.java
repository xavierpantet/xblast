package ch.epfl.xblast.launcher;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;


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
        
        /*
         * PANNEL QUI CONTIENT LE BOUTON ET LE TEXT FIELD
         */
        Panel container = new Panel();
        container.setLayout(new FlowLayout());

        //On créer le boutton
        Button startButton = new Button("START");
        container.add(startButton, FlowLayout.LEFT);
        String tableauChaine[] = {"128.179.190.79"};
       
        //Le texte field
        JFormattedTextField textField = new JFormattedTextField();
        textField.setColumns(10);
        container.add(textField, FlowLayout.LEFT);

        panel.add(container, BorderLayout.SOUTH);
        
        //Le label
        Label titre = new Label("XBLast");
        titre.setAlignment(Label.CENTER);
        
        //Le titre
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.getAllFonts();
        Font font = new Font("Jokerman", Font.PLAIN, 35);
        titre.setFont(font);
        panel.add(titre, BorderLayout.NORTH);
        
        //Le logo
        ImageIcon logo = new ImageIcon(new ImageIcon("images/icon.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));            
        JLabel image = new JLabel(logo);
        panel.add(image, BorderLayout.CENTER);
        
        startButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                connectServerWithIP(textField.getText());
            }
        });
        
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
    
    private static void connectServerWithIP(String ip){

       String host = Objects.requireNonNull(ip); // Hôte par défaut

       /*
        * Phase 1:
        * Envoyer toutes les secondes une demande au serveur pour joindre la partie,
        * tant que le serveur ne renvoie pas de GameState
        */

       try {
           // Ouverture et configuration du canal avec UDP
           DatagramChannel channel;
           channel = DatagramChannel.open(StandardProtocolFamily.INET);
           SocketAddress socketAddress = new InetSocketAddress(host, 2016);
           channel.configureBlocking(false); // on le passe en non bloquant pour la première phase

           // Définition des buffers de réception et d'envoi
           ByteBuffer sendingBuffer = ByteBuffer.allocate(1);
           ByteBuffer receivingBuffer = ByteBuffer.allocate(410);
           sendingBuffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
           sendingBuffer.flip();

           // On envoie toutes les secondes, tant qu'on n'a pas de réponse du serveur
           System.out.println("Connexion au serveur en cours... (ip: "+ip+")");
           do{
               channel.send(sendingBuffer, socketAddress);
               Thread.sleep(1000);
           }while(channel.receive(receivingBuffer)==null);

           /*
            * Phase 2:
            * Le serveur a renvoyé une identité ainsi qu'un GameState
            */

           // On crée le composant Xblast et on configure les buffers
           XBlastComponent component = new XBlastComponent();
           sendingBuffer.clear();

           // On traite la réception du GameState
           System.out.println("Connecté au serveur");
           receive(receivingBuffer, component);

           // On crée le fil Swing
           SwingUtilities.invokeAndWait(() -> createUI(component, channel, sendingBuffer, socketAddress));

           // Pour la suite, on passe le canal en mode bloquant, et on fait de même
           channel.configureBlocking(true);
           while(channel.receive(receivingBuffer)!=null){
               receive(receivingBuffer, component);
           }

       } catch (Exception e) {
           System.out.println(e.getMessage());
       }
   }

   /**
    * Ouverture du fil Swing
    * @param component (XBlastComponent) le composant XBlast
    * @param channel (DatagramChannel) le canal de communication
    * @param sendingBuffer (ByteBuffer) le buffer d'envoi
    * @param socketAddress (SocketAddress) le socketAddress pour l'envoi
    */
   private static void createUI(XBlastComponent component, DatagramChannel channel, ByteBuffer sendingBuffer, SocketAddress socketAddress){
       // On crée la table associative touche -> action
       Map<Integer, PlayerAction> kb = new HashMap<>();
       kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
       kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
       kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
       kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
       kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
       kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);

       // On crée le consommateur qui enverra les actions du joueur au serveur
       Consumer<PlayerAction> c = k -> {
           sendingBuffer.put((byte) k.ordinal());
           sendingBuffer.flip();
           try {
               channel.send(sendingBuffer, socketAddress);
           } catch (Exception e) {
               //On ignore l'erreur
           }
           sendingBuffer.clear();
       };

       // On crée et on configure la fenêtre
       JFrame window = new JFrame("XBlast");
       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       window.add(component);
       window.pack();
       window.setVisible(true);
       component.addKeyListener(new KeyboardEventHandler(kb, c));
       component.requestFocusInWindow();
   }

   /**
    * Permet le traitement des données reçues par le serveur
    * @param receivingBuffer (ByteBuffer) le buffer de réception
    * @param component (XBlastComponent) le composant XBlast 
    * @throws IllegalArgumentException
    * @throws URISyntaxException
    * @throws IOException
    */
   private static void receive(ByteBuffer receivingBuffer, XBlastComponent component) throws IllegalArgumentException, URISyntaxException, IOException{
       receivingBuffer.flip();

       List<Byte> toDeserialize = new ArrayList<Byte>(); // La liste qui contiendra le GameState à Déserialiser
       while(receivingBuffer.hasRemaining()){
           toDeserialize.add(receivingBuffer.get());
       }

       // Définition de l'identité du joueur et mise à jour du GameState et affichage
       PlayerID playerID = PlayerID.values()[toDeserialize.remove(0)];
       component.setGameState(GameStateDeserializer.deserialize(toDeserialize), playerID);
       receivingBuffer.clear();
   }


}
