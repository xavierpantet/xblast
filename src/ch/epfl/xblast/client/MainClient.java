package ch.epfl.xblast.client;

import java.awt.event.KeyEvent;
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
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

/**
 * Programme principal du client.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public class MainClient{

    /**
     * Classe principale chargée de lancer le programme
     * @param args (String[]) un tableau pouvant contenir le nom d'hôte du serveur de la partie
     */
    public static void main(String[] args) {
        String host = "localhost"; // Hôte par défaut

        // On modifie l'hôte le cas échéant
        if(args.length==1){
            host = args[0];
        }

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
            System.out.println("Connexion au serveur en cours...");
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