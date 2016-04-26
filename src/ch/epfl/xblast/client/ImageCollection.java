package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.imageio.ImageIO;

/**
 * Classe qui permet d'accéder à un élément d'un répertoire en connaissant son index
 * @author Xavier Pantet (260473)
 */
public final class ImageCollection {
    private final String collection;
    
    /**
     * Constructeur qui permet de construire une collection d'accès aux images en connaissant son nom
     * @param dir   le répertoire de la collection
     */
    public ImageCollection(String dir) throws NullPointerException {
        this.collection=Objects.requireNonNull(dir);
    }
    
    /**
     * Retourne une image dans la collection d'après son index ou lève NoSuchElementException
     * si l'index ne correspond à aucune image
     * @param i l'index de l'image
     * @return l'image pour l'index i dans la collection
     * @throws NoSuchElementException
     * @throws URISyntaxException
     * @throws IOException
     */
    public Image image(int i) throws NoSuchElementException, URISyntaxException, IOException{
        Image im = imageOrNull(i);
        if(im!=null){
            return im;
        }
        else{throw new NoSuchElementException("Aucune image trouvé pour l'index " + i);}
    }
    
    /**
     * Retourne une image dans la collection d'après son index ou null
     * si l'index ne correspond à aucune image
     * @param i l'index de l'image
     * @return l'image pour l'index i dans la collection
     * @throws URISyntaxException
     * @throws IOException
     */
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
