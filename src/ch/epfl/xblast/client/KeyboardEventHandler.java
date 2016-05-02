package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;

public final class KeyboardEventHandler extends KeyAdapter implements KeyListener {
    private final Map<PlayerAction, Integer> keyMap;
    private final Consumer<PlayerAction> consumer;
    
    public KeyboardEventHandler(Map<PlayerAction, Integer> keyMap, Consumer<PlayerAction> consumer){
        this.keyMap=Collections.unmodifiableMap(new HashMap<PlayerAction, Integer>(Objects.requireNonNull(keyMap)));
        this.consumer=Objects.requireNonNull(consumer);
    }
}
