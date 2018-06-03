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

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

/**
 * A client of the game
 * 
 * @author Guillaume Michel (258066)
 * @author Adrien Vandenbroucque (258715)
 *
 */
public class Client {
    public final static int MAX_BUFFER_SIZE = 410;
    public final static int DEFAULT_PORT = 2016;
    
    private static DatagramChannel channel;
    private static SocketAddress chaussette;
    private static ByteBuffer firstState;
    private static ByteBuffer join;
    private static ByteBuffer currentState = ByteBuffer.allocate(MAX_BUFFER_SIZE);
    private static PlayerID id;
    private static List<Byte> list=new ArrayList<>();
    private static XBlastComponent component;
    private static byte returnValue;
    
    /**
     * Initialize the game's connections
     * 
     * @param s
     *      The address of the server we will try to join
     */
    public final static void initialize(String s){
        try {
            channel = DatagramChannel.open(StandardProtocolFamily.INET);
            channel.configureBlocking(false);
            chaussette = new InetSocketAddress(s, DEFAULT_PORT);
            join = ByteBuffer.allocate(1);
            firstState = ByteBuffer.allocate(MAX_BUFFER_SIZE);
            join.put((byte)PlayerAction.JOIN_GAME.ordinal()).flip();
            
            System.out.println("Connecting the server ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Try to connect the server
     * 
     * @return
     *      The socketAddress of the server if a server connected and null if not
     */
    public final static SocketAddress connect(){
        try {
            channel.send(join, chaussette);
            Thread.sleep(1000);
            return channel.receive(firstState);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Set the first gamestate and get the game ready to run
     * 
     * @param component0
     *      The xblastcomponent containing the game
     * @throws IOException 
     * @throws URISyntaxException 
     * @throws IllegalArgumentException 
     */
    public final static void start(XBlastComponent component0) throws IllegalArgumentException, URISyntaxException, IOException {
        firstState.flip();
        component = component0;
        id = PlayerID.values()[firstState.get()];
        while(firstState.hasRemaining())//transfer the buffer to a list
            list.add(firstState.get());
        component.setGameState(GameStateDeserializer.deserialize(list), id);
        PlaySound.loop();
        list.clear();
        firstState.clear();
        try {
            channel.configureBlocking(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Play one "tick" of the game
     * 
     * @return
     *      A byte, 0x10 if the game is not ended or a bit containing the winners if the game is over
     * @throws URISyntaxException 
     * @throws IllegalArgumentException 
     */
    public final static byte play() throws IllegalArgumentException, URISyntaxException{
        try {
            channel.receive(currentState);
            currentState.flip();//receive the gamestate
            while (currentState.hasRemaining())
                list.add(currentState.get());//transfert into a list
            if (list.size()<2){
                returnValue=list.get(0);
                list.clear();
                currentState.clear();
                channel.close();
                return returnValue;
            }
            component.setGameState(GameStateDeserializer.deserialize(list.subList(1, list.size())), id);
            //display the deserialized gamestate to screen
            currentState.clear();
            list.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0x10;

}
    
    /**
     * Get the map of the keys and their corresponding playerActions. It is the controls of the game
     * 
     * @return
     *      The map of the keys and their corresponding playerActions
     */
    public final static Map<Integer, PlayerAction> getMap(){
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        return kb;
    }
    
    /**
     * Get the consumer that will send the player actions to the server
     * 
     * @return
     *      The consumer that will send the player actions to the server
     */
    public final static Consumer<PlayerAction> getConsumer(){
        Consumer<PlayerAction> c = x -> {
            try {//if a key in the map is pressed send the key event to the server
                ByteBuffer senderBuffer = ByteBuffer.allocate(1);
                senderBuffer.put((byte)x.ordinal());
                senderBuffer.flip();
                channel.send(senderBuffer, chaussette);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return c;
    }
}