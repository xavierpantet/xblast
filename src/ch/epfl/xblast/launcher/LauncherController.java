package ch.epfl.xblast.launcher;

import java.awt.Color;
import java.awt.datatransfer.StringSelection;
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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;

public class LauncherController implements ActionListener, KeyListener {

    LauncherModel modele;
    LauncherView vue;
    
    Thread t1;
    
    public LauncherController(LauncherModel modele, LauncherView vue) {
        this.modele = modele;
        modele.setInitialState();
        this.vue = vue;

        vue.startButton.addActionListener(this);
        vue.textField.addKeyListener(this);
        
        modele.setInitialState();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    
        if(e.getSource() == vue.startButton){
            if(modele.getButtonState()==ButtonState.START){
                start();
            } else if(modele.getButtonState()==ButtonState.CANCEL){
                cancel();
            }
           
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar() ;
        
        if (!   ((c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE) 
            ||  (c == KeyEvent.VK_TAB) ||  (c== KeyEvent.VK_ENTER) 
            ||  (Character.isDigit(c)) || (c==KeyEvent.VK_PERIOD)))
        {
           e.consume();
       }
        else if(c== KeyEvent.VK_ENTER){
            start();
        }
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    public void start(){
        modele.setIP(vue.textField.getText());
        if(IPseemsCoorect()){
            fetchIPandWait();
        } else {
            modele.setNotCorrectState();
        }
    }
    
    public void cancel(){
        modele.setInitialState();
        t1.interrupt();
    }
    
    public void fetchIPandWait(){
        String host = modele.getIP(); // Hôte par défaut

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
           modele.setConnectingState();
           
           t1 = new Thread(new Runnable() {
               public void run() {
                   System.out.println("connect thread start");
                   try {
                       do{   
                           System.out.println("SEND");
                           channel.send(sendingBuffer, socketAddress);
                           Thread.sleep(1000);
                   }while(channel.receive(receivingBuffer)==null);
                       
                       modele.setConnectedState();
                       
                       // On crée le composant Xblast et on configure les buffers
                       XBlastComponent component = new XBlastComponent();
                       sendingBuffer.clear();

                       // On traite la réception du GameState
                       System.out.println("Connecté au serveur");
                       receive(receivingBuffer, component);
                       vue.window.remove(vue.panel);
                       vue.window.add(component);
                       vue.window.pack();
                       
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
                       
                       
                   } catch (IOException e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
                   } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
               }
          });  
          

           t1.start();
          
            /*
             * Phase 2:
             * Le serveur a renvoyé une identité ainsi qu'un GameState
             */
          
          
          /*
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
            }*/
            
        } catch (Exception e) {
            modele.setErrorState();
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

private boolean IPseemsCoorect(){
    
    int numberOfDots = 0;
    String numbersToCheck = "";
    String IP = modele.getIP();
    
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
                    } else if(value>255||value<0){
                        return false;
                    } else {
                        numbersToCheck="";
                    }
                }
            }
            //Si c'est autre chose
            else {
                return false;
            }
            
        } else {
            return false;
        }
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

    
}
