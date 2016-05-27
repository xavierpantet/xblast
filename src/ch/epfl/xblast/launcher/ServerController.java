package ch.epfl.xblast.launcher;

import java.util.Objects;

public final class ServerController {
    private final ServerModel model;
    
    public ServerController(ServerModel model){
        this.model=Objects.requireNonNull(model);
    }
}
