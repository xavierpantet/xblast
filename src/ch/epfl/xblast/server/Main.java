package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
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
 * Programme principal du serveur.
 * @author Xavier Pantet (260473) & Timothée Duran (258683)
 */
public final class Main {
    /**
     * Classe principale, chargée de lancer le programme.
     * @param args  un tableau pouvant contenir le nombre de joueurs effectifs qui prendront part à la partie
     */
    public static void main(String[] args) {
        int nbOfPlayers=4; // Nombre de joueurs par défaut
        
        // On met éventuellement à jour le nombre de joueurs
        if(args.length==1){
            nbOfPlayers=Integer.parseInt(args[0]);
        }
        
        // Table associtative SocketAddress -> PlayerID
        Map<SocketAddress, PlayerID> playersMap = new HashMap<>();

        try {
            // Création du channel et liaison avec le port 2016
            DatagramChannel channel;
            channel = DatagramChannel.open(StandardProtocolFamily.INET);
            channel.bind(new InetSocketAddress(2016));

            // Création du buffer de réception
            ByteBuffer receivingBuffer = ByteBuffer.allocate(1);
            
            /*
             * Phase 1: on attend les connexions des joueurs
             */
            int nextPlayerID=0; // id du prochain joueur à ajouter à la partie
            System.out.println("En attente de joueurs...");
            while(playersMap.size()!=nbOfPlayers){
                SocketAddress senderAddress = channel.receive(receivingBuffer);
                
                // On teste que le joueur désire bien rejoindre la partie
                if(receivingBuffer.get(0)==PlayerAction.JOIN_GAME.ordinal()){
                    if(!playersMap.containsKey(senderAddress)){
                        playersMap.put(senderAddress, PlayerID.values()[nextPlayerID]);
                        System.out.println(playersMap.size() + "/" + nbOfPlayers + " trouvés");
                        nextPlayerID++;
                    }
                    
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
            
            System.out.println("Début de la partie...");
            long startTime = System.nanoTime(); // Temps du début de la partie
            
            while(!g.isGameOver()){
                // On sérialise le gameState et on l'envoie
                List<Byte> gameState = GameStateSerializer.serialize(boardPainter, g);
                send(gameState, playersMap, channel);
                
                // On prépare les variables et on reçoit les actions des joueurs
                speedChangeEvents.clear();
                bombDropEvents.clear();
                
                // On reçoit les actions des joueurs
                receive(playersMap, channel, receivingBuffer, speedChangeEvents, bombDropEvents);
                
                // On calcule le prochain état de jeu
                g=g.next(speedChangeEvents, bombDropEvents);
                
                // On calcule le temps du prochain coup d'horloge
                long nextTickTime=startTime + (long)Ticks.TICK_NANOSECOND_DURATION*g.ticks();
                
                // On calcule le temps pendant lequel le serveur doit "dormir"
                long sleepTime = (long) (nextTickTime-System.nanoTime());
                if(sleepTime>0){ // S'il ne faut pas dormir, on ne s'arrête pas
                    Thread.sleep((long) (sleepTime*Time.MS_PER_S/Time.NS_PER_S), (int) (sleepTime%(Time.NS_PER_S/Time.MS_PER_S)));
                }
            }
            
            // On affiche à l'écran l'identité du joueur qui a gagné la partie 
            if(g.winner().isPresent()){
                System.out.println(g.winner().get());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Permet d'envoyer les données aux joueurs.
     * @param gameState le GameState sérialisé à envoyer
     * @param playersMap    la table associative des joueurs avec leur SocketAddress
     * @param channel   le canal d'envoi
     * @throws IOException
     */
    private static void send(List<Byte> gameState, Map<SocketAddress, PlayerID> playersMap, DatagramChannel channel) throws IOException{
        // On crée un buffer d'envoi
        ByteBuffer sendingBuffer = ByteBuffer.allocate(gameState.size()+1);
        
        // On prévoit une place pour l'identité du joueur au début
        sendingBuffer.put((byte) 0);
        
        // On remplit le buffer avec les données du GameState
        for(Byte b : gameState){
            sendingBuffer.put(b);
        }
        sendingBuffer.flip();

        // Pour chacun des joueurs...
        for(Entry<SocketAddress, PlayerID> e : playersMap.entrySet()){
            // ... on ajoute son identité et on envoie
            sendingBuffer.put(0, (byte) e.getValue().ordinal());
            channel.send(sendingBuffer, e.getKey());
            sendingBuffer.rewind();
        }
    }
    
    /**
     * Permet de recevoir les actions des joueurs pour les traiter
     * @param playersMap    la table associative des joueurs et de leur SocketAddres
     * @param channel   le canal de communication
     * @param receivingBuffer   le buffer de réception
     * @param speedChangeEvents le tableau des événements de changement de direction
     * @param bombDropEvents    le tableau des demandes de dépôt de bombes
     * @throws IOException
     */
    private static void receive(Map<SocketAddress, PlayerID> playersMap,  DatagramChannel channel, ByteBuffer receivingBuffer, Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents) throws IOException{
        SocketAddress senderAddress;
        while((senderAddress = channel.receive(receivingBuffer)) !=null){
            
            // Si le joueur veut déposer une bombe, on remplit bombDropEvents
            if(receivingBuffer.get(0)==PlayerAction.DROP_BOMB.ordinal()){
                bombDropEvents.add(playersMap.get(senderAddress));
            }
            
            // Si le joueur veut s'arrêter, on met un Optional vide dans speedChangeEvents
            else if(receivingBuffer.get(0)==PlayerAction.STOP.ordinal()){
                speedChangeEvents.put(playersMap.get(senderAddress), Optional.empty());
            }
            
            // Sinon, on ajoute la direction qu'il veut emprunter
            else{
                if(receivingBuffer.get(0)!=0){
                    speedChangeEvents.put(playersMap.get(senderAddress), Optional.of(Direction.values()[receivingBuffer.get(0)-1]));
                }
            }
            receivingBuffer.clear();
        }
    }
}
