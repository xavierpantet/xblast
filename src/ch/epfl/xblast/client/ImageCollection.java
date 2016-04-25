package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

public final class ImageCollection {
    private final String collection;
    public ImageCollection(String dir){
        this.collection=dir;
    }
    
    public Image image(int i) throws NoSuchElementException, URISyntaxException, IOException{
        Image im = imageOrNull(i);
        if(im!=null){
            return im;
        }
        else{throw new NoSuchElementException("Aucune image trouvé pour l'index " + i);}
    }
    
    public Image imageOrNull(int i) throws URISyntaxException, IOException{
        File dir = new File(ImageCollection.class
                .getClassLoader()
                .getResource(collection)
                .toURI());
        File[] files = dir.listFiles();
        for(File f:files){
            if(i==Integer.parseInt(f.getName().substring(0, 3))){
                return ImageIO.read(f);
            }
        }
        return null;
    }
}
