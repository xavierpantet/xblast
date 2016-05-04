package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
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
            try{
                File directory = new File(ImageCollection.class
                    .getClassLoader()
                    .getResource(dir)
                    .toURI());
                File[] files = directory.listFiles();
            
                //On créer une liste contenu d'éléments vides de la taille du plus grand identifiant d'image de la collection
                if (imageExplosion.isEmpty()){
                    //On sait que l'identifiant le plus grand est 21
                    imageExplosion.addAll(Collections.nCopies(22, null));  
                }
                
                if (imagePlayer.isEmpty()){
                    //On sait que l'identifiant le plus grand est 21
                    imagePlayer.addAll(Collections.nCopies(92, null));  
                }
                if (imageScore.isEmpty()){
                    //On sait que l'identifiant le plus grand est 21
                    imageScore.addAll(Collections.nCopies(22, null));  
                }
                for(File f : files){
                    Image im = ImageIO.read(f);
                    switch(dir){
                    case "block":
                        imageBlock.add(Integer.parseInt(f.getName().substring(0, 3)), im);
                        break;
                    
                    case "explosion":
                        
                        imageExplosion.set(Integer.parseInt(f.getName().substring(0, 3)), im);
                        break;
                        
                    case "player":
                        imagePlayer.set(Integer.parseInt(f.getName().substring(0, 3)), im);
                        break;
                        
                    case "score":
                        imageScore.set(Integer.parseInt(f.getName().substring(0, 3)), im);
                    }
                }
            }
            catch(Exception e){}
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
        else{throw new NoSuchElementException("Aucune image trouvée pour l'index " + i + " dans la collection " + collection);}
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
            if(i<0 || (i>15&&i<20) || i>imageExplosion.size()-1){
                
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
            if(i<0 || (i>12 && i<20) || (i>7 && i<10) || i>imageScore.size()-1){
                return null;
            }
            else{
                return imageScore.get(i);
            }
        }
    }
}