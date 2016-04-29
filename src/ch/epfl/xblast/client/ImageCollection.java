package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * Classe qui permet d'accéder à un élément d'un répertoire en connaissant son index
 * @author Xavier Pantet (260473)
 */
public final class ImageCollection {
    private static final ArrayList<Image> imageBlock = new ArrayList<>();
    private static final ArrayList<Image> imageExplosion = new ArrayList<>();
    private static final ArrayList<Image> imagePlayer = new ArrayList<>();
    private static final ArrayList<Image> imageScore = new ArrayList<>();
    
    private final String collection;
    
    /**
     * Constructeur qui permet de construire une collection d'accès aux images en connaissant son nom.
     * Il va aussi directement créer les collections dans les attributs statiques correspondants
     * pour que la récupération d'une image n'implique pas de devoir recréer la liste des fichiers à chaque appel
     * @param dir   le répertoire de la collection
     */
    public ImageCollection(String dir) {
        
        // On fait un tableau contenant les 4 collections possibles
        String[] str = new String[]{"block", "explosion", "player", "score"};
        
        // Pour chacune des collections, on crée la liste des images
        for(String s : str){
            try{
                File directory = new File(ImageCollection.class
                    .getClassLoader()
                    .getResource(s)
                    .toURI());
                File[] files = directory.listFiles();
            
                for(File f : files){
                    Image im = ImageIO.read(f);
                    switch(s){
                    case "block":
                        imageBlock.add(im);
                        break;
                    
                    case "explosion":
                        imageExplosion.add(im);
                        break;
                        
                    case "player":
                        imagePlayer.add(im);
                        break;
                        
                    default:
                        imageScore.add(im);
                    }
                }
            }
            catch(Exception e){}
        }
        this.collection = dir;
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
    public Image image(int i) throws NoSuchElementException {
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
     */
    public Image imageOrNull(int i){
        switch(collection){
        case "block":
            if(i<0 || i>imageBlock.size()-1){
                return null;
            }
            else{
                return imageBlock.get(i);
            }
        
        case "explosion":
            if(i<0 || i>imageExplosion.size()-1){
                return null;
            }
            else{
                return imageExplosion.get(i);
            }
            
        case "player":
            if(i<0 || i>imagePlayer.size()-1){
                return null;
            }
            else{
                return imagePlayer.get(i);
            }
            
        default:
            if(i<0 || i>imageScore.size()-1){
                return null;
            }
            else{
                return imageScore.get(i);
            }
        }
    }
}
