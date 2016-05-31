package ch.epfl.xblast.launcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;

/**
 * Cette classe représente le controleur (sur le modèle de l'architecture MVC), de la fenêtre de lancement du jeu.
 * @author timotheedu
 *
 */
public class LauncherController implements ActionListener, KeyListener {

    private final LauncherModel model;
    private final LauncherView view;
    private Thread t1;

    /**
     * Constructeur de la calsse
     * @param modele (LauncherModel) le modèle
     * @param vue (LauncherView) la vue
     */
    public LauncherController(LauncherModel modele, LauncherView vue) {
        this.model = modele;
        this.view = vue;

        //On ajoute les listeners
        vue.startButton.addActionListener(this);
        vue.textField.addKeyListener(this);
        vue.localCheck.addActionListener(this);

        //On initialise les attributs de la fenêtre à leurs états initals
        modele.setInitialState();

    }

    @Override
    /**
     * Si une action est réalisée
     */
    public void actionPerformed(ActionEvent e) {

        //Si l'action provient du bouton d'action
        if(e.getSource() == view.startButton){
            //On lance ou annule le jeu et on change l'état du bouto
            if(model.getButtonState()==ButtonState.START){
                start();
            } else if(model.getButtonState()==ButtonState.CANCEL){
                cancel();
            }
        }
        
        //Si la'ction provient de la case à cocher "j'héberge le serveur sur cet ordinateur"
        if(e.getSource() == view.localCheck){
            //On passe à l'état initial ou on passe à l'état "j'héberge le serveur"
            if(model.getServerMode()){
                model.setInitialState();
            } else {
                model.setServerMode();
            }
        }
    }

    @Override
    /**
     * Si une touche est pressée
     */
    public void keyTyped(KeyEvent e) {
        //On récupère le caractère pressé
        char c = e.getKeyChar() ;

        /*Si le caractère n'est pas un chiffre ou un point, un l'ignore
        (Une IP est composée d'uniquement de ces caractères)*/
        if (!   ((c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE) 
                ||  (c == KeyEvent.VK_TAB) ||  (c== KeyEvent.VK_ENTER) 
                ||  (Character.isDigit(c)) || (c==KeyEvent.VK_PERIOD)))
        { e.consume();}
        
        //Si l'utilisateur presse "ENTER", on lance le precessus de connexion
        else if(c== KeyEvent.VK_ENTER){ start(); }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Méthode qui va récupérer l'adresse IP donnée, la controler, et lancer une connexion. 
     * Si le mode serveur est activé, alors on lance un connexion avec comme paramètre "localhost"
     */
    private void start(){

        if(model.getServerMode()){
            //On appele la méthode de connexion
            fetchIPandStart("localhost");
            
        } else {
            model.setIP(view.textField.getText());
            if(IPseemsCoorect()){
              //Si l'IP semble correcte, on appele la méthode de connexion avec celle-ci
                fetchIPandStart(model.getIP());

            } else {
                //Si l'IP n'a pas le bon format, on passe en mode "erreur"
                model.setNotCorrectState();
                
            } 
        }
    }

    /**
     * Méthode pour annuler la connexion et passer à l'état initial
     */
    private void cancel(){
        model.setInitialState();
        t1.interrupt();
    }

    /**
     * Méthode qui va lancer une connexion à l'adresse souhaité, attendre le début de la partie et commencer le jeu
     * @param adress (String) l'adresse du serveur
     */
    private void fetchIPandStart(String adress){
        String host = adress; // Hôte par défaut

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
            model.setConnectingState();

            t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        do{ 
                            channel.send(sendingBuffer, socketAddress);
                            Thread.sleep(1000);
                        }while(channel.receive(receivingBuffer)==null);

                        model.setConnectedState();

                        // On crée le composant Xblast et on configure les buffers
                        XBlastComponent component = new XBlastComponent();
                        sendingBuffer.clear();

                        // On traite la réception du GameState
                        System.out.println("Connecté au serveur");
                        receive(receivingBuffer, component);
                        view.window.remove(view.panel);
                        view.window.add(component);
                        
                        view.window.pack();
                        //Positionement de la fenêtre au centre
                        view.window.setLocationRelativeTo(null);

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

                    
                        component.addKeyListener(new KeyboardEventHandler(kb, c));
                        component.requestFocusInWindow();

                        channel.configureBlocking(true);
                        while(channel.receive(receivingBuffer)!=null){
                            receive(receivingBuffer, component);
                        }

                    } 
                    catch (IOException e) { e.printStackTrace();} 
                    catch (InterruptedException e) {} 
                    catch (IllegalArgumentException e) {e.printStackTrace();} 
                    catch (URISyntaxException e) {e.printStackTrace();}
                }
            });  

            t1.start();

        } catch (Exception e) {
            //Si il y a une erreur, on passe en mode "ERREUR"
            model.setErrorState();
            System.out.println(e.getMessage());
        }
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

    /**
     * Métode qui permet de vérifier si l'IP stockée dans le modèle semble correct.
     * C'est-à-dire si elle est composée de 4 blocks de numéros qui ne sont pas suprieurs à 255
     * @return (boolean) true si l'adresse semble correct, false sinon
     */
    private boolean IPseemsCoorect(){

        int numberOfDots = 0;
        String numbersToCheck = "";
        String IP = model.getIP();

        //On parcours l'IP
        for(int i=0; i<IP.length(); i++){
            char c = IP.charAt(i);
            
            if(numbersToCheck.length()==0){
                //Si le caractère n'est pas un chiffre -> l'adresse est fausse
                if(!(c > 47 && c< 58)){
                    return false;
                } else {
                    numbersToCheck += c;
                }
            } else if(numbersToCheck.length()>0&&numbersToCheck.length()<=3){
                //Si c'est un chiffre
                if(c > 47 && c< 58){
                    numbersToCheck += c;
                }
                //Si c'est un point ou que c'est la denière section
                else if (c==46 || numberOfDots==3){
                    numberOfDots ++;
                    //Si il y a plus que 4 points
                    if (numberOfDots>4){
                        return false;
                    } else {
                        //CHECK STRING
                        int value = Integer.parseInt(numbersToCheck);
                        if (numbersToCheck.length()>3){
                            return false;
                        } else if (value>255||value<0){
                            return false;
                        } else {
                            numbersToCheck="";
                        }
                    }
                }
                //Si c'est autre chose
                else {return false;}

            } else {return false;}
        }

        if(numberOfDots==3){

            int value = Integer.parseInt(numbersToCheck);

            if (numbersToCheck.length()>3){
                return false;
            } else if(value>255||value<0){
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }
    
    
}