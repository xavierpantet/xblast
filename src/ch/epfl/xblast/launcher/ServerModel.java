package ch.epfl.xblast.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
}
