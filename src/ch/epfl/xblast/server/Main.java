package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;
import ch.epfl.xblast.server.graphics.BoardPainter;

/**
 * Classe principale du programme serveur.
 * Le serveur commence par attendre les connexions des joueurs, puis il va calculer les états de jeu pour les renvoyer.
 * @author Xavier Pantet (260473)
 */
public final class Main {
    /**
     * Classe principale, chargée de lancer le programme.
     * @param args  un tableau pouvant contenir le nombre de joueurs effectifs qui prendront part à la partie
     */
    public static void main(String[] args) {
        /*
         * Définition du nombre de joueurs
         */
        int nbOfPlayers=4;
        int nextPlayerID=0;
        
        Map<SocketAddress, PlayerID> playersMap = new HashMap<>();
        if(args.length==1){
            nbOfPlayers=Integer.parseInt(args[0]);
        }
        
        /*
         * Programme principal
         */
        try {
            // Création du channel et liaison avec le port 2016
            DatagramChannel channel;
            channel = DatagramChannel.open(StandardProtocolFamily.INET);
            channel.bind(new InetSocketAddress(2016));

            ByteBuffer receivingBuffer = ByteBuffer.allocate(1);
            
            /*
             * Phase 1: on attend les connexions des joueurs
             */
            while(playersMap.size()!=nbOfPlayers){
                SocketAddress senderAddress = channel.receive(receivingBuffer);
                
                // On teste que le joueur désire bien rejoindre la partie
                if(receivingBuffer.get(0)==PlayerAction.JOIN_GAME.ordinal()){
                    playersMap.put(senderAddress, PlayerID.values()[nextPlayerID]);
                    nextPlayerID++;
                    receivingBuffer.clear();
                }
            }
            
            /*
             * Phase 2: jeu
             */
            
            // Création du gameState et du BoardPainter
            GameState g = Level.DEFAULT_LEVEL.g();
            BoardPainter boardPainter = Level.DEFAULT_LEVEL.b();
            
            // Création des variables de récupération des actions des joueurs et passage
            // du buffer en mode non bloquant (pour récupérer les actions des joueurs)
            Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
            Set<PlayerID> bombDropEvents = new HashSet<>();
            channel.configureBlocking(false);
            
            while(!g.isGameOver()){
                long startTime = System.nanoTime();
                
                // On sérialise le gameState et on l'envoie
                List<Byte> gameState = GameStateSerializer.serialize(boardPainter, g);
                send(gameState, playersMap, channel);
                
                // On prépare les variables et on reçoit les actions des joueurs
                speedChangeEvents.clear();
                bombDropEvents.clear();
                receive(playersMap, channel, receivingBuffer, speedChangeEvents, bombDropEvents);
                
                // On calcul le temps qui a été nécessaire pour le traitement
                long sleepTime = Ticks.TICK_NANOSECOND_DURATION-(System.nanoTime()-startTime);
                
                // Si ce temps est de plus de 50ms, on continue sans plus attendre
                if(sleepTime>0){
                    Thread.sleep(sleepTime/Time.MS_PER_S, (int) sleepTime%Time.MS_PER_S);
                }
                
                // On calcule le prochain état de jeu
                g=g.next(speedChangeEvents, bombDropEvents);
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void send(List<Byte> gameState, Map<SocketAddress, PlayerID> playersMap, DatagramChannel channel) throws IOException{
        ByteBuffer sendingBuffer = ByteBuffer.allocate(gameState.size()+1);
        for(Byte b : gameState){
            sendingBuffer.put(b);
        }
        sendingBuffer.flip();

        for(Entry<SocketAddress, PlayerID> e : playersMap.entrySet()){
            ByteBuffer sendingBufferPerPlayer = sendingBuffer.duplicate();
            sendingBufferPerPlayer.put(0, (byte) e.getValue().ordinal());
            sendingBufferPerPlayer.flip();
            channel.send(sendingBufferPerPlayer, e.getKey());
        }
    }
    
    private static void receive(Map<SocketAddress, PlayerID> playersMap,  DatagramChannel channel, ByteBuffer receivingBuffer, Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents) throws IOException{
        SocketAddress senderAddress;
        while((senderAddress = channel.receive(receivingBuffer)) !=null){
            if(receivingBuffer.get(0)==PlayerAction.DROP_BOMB.ordinal()){
                bombDropEvents.add(playersMap.get(senderAddress));
            }
            else if(receivingBuffer.get(0)==PlayerAction.STOP.ordinal()){
                speedChangeEvents.put(playersMap.get(senderAddress), Optional.empty());
            }
            else{
                speedChangeEvents.put(playersMap.get(senderAddress), Optional.of(Direction.values()[receivingBuffer.get(0)+1]));
            }
            receivingBuffer.clear();
        }
    }
}
