package ch.epfl.xblast.launcher;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import ch.epfl.xblast.client.ImageCollection;

public final class ServerModel {
    private List<String> filesList = new ArrayList<>();
    private static final String FOLDER = "levels";
    
    public ServerModel(){
        try{
            // On ouvre le répertoire et on récupère la liste des fichiers
            File directory = new File(ImageCollection.class
                    .getClassLoader()
                    .getResource(FOLDER)
                    .toURI());
            File[] files = directory.listFiles();

            // Pour chacun des fichiers...
            for(File f : files){
                if(f.getName().charAt(0)!='.'){
                    filesList.add(f.getName());
                }
            }
        }
        catch(Exception e){}
    }
    
    public List<String> getFilesList(){
        return Collections.unmodifiableList(new ArrayList<>(filesList));
    }
    
    public String getFileAtPosition(int pos){
        return filesList.get(pos);
    }
    
    
    public String getIP() throws SocketException{
        // Code largement inspiré de http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements()){
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements()){
                InetAddress i = (InetAddress) ee.nextElement();
                if(!i.isLoopbackAddress() && !i.isSiteLocalAddress() && !i.isAnyLocalAddress() && !i.isLinkLocalAddress())
                    return i.getHostAddress();
            }
        }
        return null;
    }
}
