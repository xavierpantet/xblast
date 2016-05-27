package ch.epfl.xblast.launcher;

import java.net.SocketException;

public final class ServerController {
    public static void main(String[] args) throws SocketException{
        ServerModel model = new ServerModel();
        System.out.println(model.getIP());
    }
}