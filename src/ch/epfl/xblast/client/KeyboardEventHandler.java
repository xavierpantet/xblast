package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;

/**
 * Classe permettant de gérer les actions des joueurs en fonction des appuis sur les touches du clavier
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class KeyboardEventHandler extends KeyAdapter implements KeyListener {
    private final Map<Integer, PlayerAction> keyMap;
    private final Consumer<PlayerAction> consumer;
    
    /**
     * Constructeur
     * @param keyMap (Map<Integer, PlayerAction>) une table associative faisant correspondre un code clavier à chaque action de joueur
     * @param consumer (Consumer<PlayerAction>) un consommateur qui consommera les appuis sur les touches de clavier
     */
    public KeyboardEventHandler(Map<Integer, PlayerAction> keyMap, Consumer<PlayerAction> consumer){
        this.keyMap=Collections.unmodifiableMap(new HashMap<>(Objects.requireNonNull(keyMap)));
        this.consumer=Objects.requireNonNull(consumer);
    }
    
    @Override
    /**
     * Redéfiniiton de keyPressed
     * Permet de consommer une action après un appui sur une touche du clavier
     * @param k (KeyEvent) un événement clavier
     */
    public void keyPressed(KeyEvent k){
        if(keyMap.containsKey(k.getKeyCode())){
            consumer.accept(keyMap.get(k.getKeyCode()));
        }
    }
}
