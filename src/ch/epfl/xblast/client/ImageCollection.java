package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import ch.epfl.xblast.server.Bonus;

/**
 * Classe qui permet d'accéder à un élément d'un répertoire en connaissant son index.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 */
public final class ImageCollection {
    private final Map<Byte, Image> collectionMap;
    
    public static ImageCollection IMAGE_COLLECTION_BLOCK = new ImageCollection("block");
    public static ImageCollection IMAGE_COLLECTION_EXPLOSION = new ImageCollection("explosion");
    public static ImageCollection IMAGE_COLLECTION_PLAYER = new ImageCollection("player");
    public static ImageCollection IMAGE_COLLECTION_SCORE = new ImageCollection("score");
    public static ImageCollection IMAGE_COLLECTION_MAPS = new ImageCollection("samplemaps");

    /**
     * Constructeur qui permet de construire une collection d'accès aux images en connaissant son nom.
     * On ne créera qu'une collection par répertoire pour ne pas les recharger à chaque tour de boucle.
     * @param dir (String) le répertoire de la collection
     */
    public ImageCollection(String dir) {
        Map<Byte, Image> temp = new TreeMap<>();
        try{
            // On ouvre le répertoire et on récupère la liste des fichiers
            File directory = new File(ImageCollection.class
                    .getClassLoader()
                    .getResource(dir)
                    .toURI());
            File[] files = directory.listFiles();

            // Pour chacun des fichiers...
            for(File f : files){
                Image im = ImageIO.read(f); // ... on le lit ...
                try{
                    temp.put(Byte.parseByte(f.getName().substring(0, 3)), im); // ... et on ajoute son numéro dans la collection
                }
                catch(Exception e){}
            }
        }
        catch(Exception e){}
        
        // On rend la collection immuable
        this.collectionMap=Collections.unmodifiableMap(temp);
    }

    /**
     * Retourne une image dans la collection d'après son index ou lève NoSuchElementException
     * si l'index ne correspond à aucune image
     * @param i (int) l'index de l'image
     * @return l'image pour l'index i dans la collection (Image)
     * @throws NoSuchElementException
     * @throws URISyntaxException
     * @throws IOException
     */
    public Image image(int i) throws NoSuchElementException {
        Image im = imageOrNull(i);
        if(im!=null){
            return im;
        }
        else{throw new NoSuchElementException("Aucune image trouvée pour l'index " + i + " dans la collection");}
    }

    /**
     * Retourne une image dans la collection d'après son index ou null
     * si l'index ne correspond à aucune image
     * @param i (int) l'index de l'image
     * @return l'image pour l'index i dans la collection (Image)
     */
    public Image imageOrNull(int i){
        if(collectionMap.containsKey((byte) i)){
            return collectionMap.get((byte) i);
        }
        else{return null;}
    }
}