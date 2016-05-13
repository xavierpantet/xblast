package ch.epfl.xblast.client;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.graphics.BlockImage;
import ch.epfl.xblast.server.graphics.BoardPainter;

public class Main {

    public static void main(String[] args) {
        
        String host = "localhost";

        //Si la taille est de 1, on récupère le nom de l'hôte sur lequel se trouve le serveur, sinon c'est localhost par défaut
        if(args.length==1){
            host = args[0];
        }
        
        /*Premier état
        Il boucle en envoyant à intervalles réguliers (p.ex. toutes les secondes) un message au serveur, 
        exprimant son intention de se joindre à la partie. 
        Il fait cela tant et aussi longtemps qu'il n'a pas reçu le premier état du jeu de la part du serveur.*/
        
        DatagramChannel channel;
        try {
              channel = DatagramChannel.open(StandardProtocolFamily.INET);
              SocketAddress socketAddress = new InetSocketAddress(host, 2016);
              //channel.bind(socketAddress);
              channel.configureBlocking(false);
              
              ByteBuffer sendingBuffer = ByteBuffer.allocate(1);
              ByteBuffer receivingBuffer = ByteBuffer.allocate(410);
              sendingBuffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
              sendingBuffer.flip();
   
              do{
                  channel.send(sendingBuffer, socketAddress);
                  Thread.sleep(1000);
              }while(channel.receive(receivingBuffer)==null);
              
              System.out.println("VOILA JE TE PRINT LE BUFFER JUSTE APRES LE WHILE CONNARD "+receivingBuffer);

              // XBlast Component
              XBlastComponent component = new XBlastComponent();
              
              //409 octets au maximum pour l'état sérialisé + 1 byte qui indique quel jouer on est 
              
              //channel.configureBlocking(true);

              do{
                  receivingBuffer.flip();
                  System.out.println("OK"+receivingBuffer.remaining());
                  List<Byte> toDeserialize = new ArrayList<Byte>();
              
                  System.out.println("OK has remaining ????"+receivingBuffer.hasRemaining()+" pourtant position "+receivingBuffer.position());
                  while(receivingBuffer.hasRemaining()){
                      toDeserialize.add(receivingBuffer.get());
                      System.out.println("K");
                  }
              
                  System.out.println(toDeserialize);
                  PlayerID playerID = PlayerID.values()[toDeserialize.remove(0)];
                  GameStateClient deserializedGame = GameStateDeserializer.deserialize(toDeserialize);
              
                  // SetGameState 
                  component.setGameState(deserializedGame, playerID);
                 
                  receivingBuffer.clear();
              }while(channel.receive(receivingBuffer)!=null);
          
              SwingUtilities.invokeAndWait(() -> createUI(component));

        } catch (Exception e) {
            e.printStackTrace();
        }
        
             
      

    }
    
    private static void createUI(XBlastComponent component){
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        Consumer<PlayerAction> c = System.out::println;
        
        JFrame window = new JFrame("XBlast");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(component);
        window.pack();
        window.setVisible(true);
        component.addKeyListener(new KeyboardEventHandler(kb, c));
        component.requestFocusInWindow();
    }


}