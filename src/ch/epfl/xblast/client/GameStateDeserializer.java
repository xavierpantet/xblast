package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;
import ch.epfl.xblast.server.GameState;

/**
 * Représente un désérialiseur d'état. Elle constitue, en quelque sorte, l'inverse de la classe GameStateSerializer
 * @author timotheedu
 *
 */
public final class GameStateDeserializer {
    
    /**
     * Constructeur par défaut privé pour quôn ne puisse pas créer d'instance. La classe est publique, finale et non instanciable.
     */
    private GameStateDeserializer(){};
    
    public static GameState deserialize(List<Byte> givenList) throws IllegalArgumentException, URISyntaxException, IOException{
        
        //Création des différentes sublists
        int givenListSize = givenList.size();
        List<Byte> bytesForBoard = givenList.subList(1, givenList.get(0)+1);
        int nbOfBoardBytes = givenList.get(0);
        int nbOfPlayerbytes = givenList.get(nbOfBoardBytes)+1;
        List<Byte> bytesForBombs =  givenList.subList(givenList.get(0)+2, nbOfBoardBytes+nbOfPlayerbytes+3);
        List<Byte> bytesForPlayers = givenList.subList(givenListSize-17, givenListSize-1);
        Byte byteForTime = givenList.get(givenListSize-1);  
        byte time = givenList.get(givenListSize-1);
        
        //GameState(List<Player> players, List<Image> boardImages, List<Image> explosivesImages, List<Image> scoreImages, List<Image> timeLineImages)
        //Création de la liste de player
        List<Player> players = listForPlayers(bytesForPlayers);
        
        //#########IVENRSER LA LISTE EN MODE ORDRE DE LECTURE###########
        List<Image> boardImages = listForBoard(bytesForBoard);
        List<Image> explosivesImages = listForBombs(bytesForBombs);
        List<Image> scoreImages = listForScore(bytesForPlayers);
        List<Image> timeLineImages = listForTime(time);
        
        return null;
    }
    
    //Plateau
    public static List<Image> listForBoard(List<Byte> givenListSub) throws URISyntaxException, IOException{
        //Il faut parcourir le tableau en tenant compte du fait que on doit retourner une liste dans 'ordre de lecture et que on une liste en spirale
        
        
        //Création du tableau d'image normal
        int givenListSubSize = givenListSub.size();
        boolean nextIsCompressed = false;
        int compressedNumber = 0;
        List<Image> listImage = new LinkedList<Image>();
        ImageCollection imageCollection = new ImageCollection("block");
        
        for (int i=1; i<givenListSubSize; i++){
            
            byte number = givenListSub.get(i);
            if(!nextIsCompressed){
                
                //Si l'entier est négatif, cela veut dire que l'on a affaire à une indication sur le nombre de répétition du chiffre suivant. Sinon, c'est simplement le chiffre en question
                if(number<0){
                    //On récupère le nombre de répétion du nombre suivant et on indique avec "nextIsCompressed" que le chiffre suivant sera compressé
                   compressedNumber = (int)((-1*number)+2);
                   nextIsCompressed = true; 
                } else {
                    //Le nombre est a prendre tel quel
                    listImage.add(imageCollection.imageOrNull(number));
                }
            }else{
                //le nombre est compressé d'une valeur de "compressedNumber"
                    listImage.addAll(Collections.nCopies(compressedNumber, imageCollection.imageOrNull(number)));
                    compressedNumber=0;
                    nextIsCompressed=false;
                    
            }
            
        }
        
        //Conversion en odre de lecture
        return listImage;
    }
    //Bombes et explosions
    private static List<Image> listForBombs(List<Byte> givenListSub) throws URISyntaxException, IOException{
        int givenListSubSize = givenListSub.size();
        boolean nextIsCompressed = false;
        int compressedNumber = 0;
        List<Image> listImage = new LinkedList<Image>();
        ImageCollection imageCollection = new ImageCollection("explosion");
        
        for (int i=1; i<givenListSubSize; i++){
            
            byte number = givenListSub.get(i);
            if(!nextIsCompressed){
                
                //Si l'entier est négatif, cela veut dire que l'on a affaire à une indication sur le nombre de répétition du chiffre suivant. Sinon, c'est simplement le chiffre en question
                if(number<0){
                    //On récupère le nombre de répétion du nombre suivant et on indique avec "nextIsCompressed" que le chiffre suivant sera compressé
                   compressedNumber = (int)((-1*number)+2);
                   nextIsCompressed = true; 
                } else {
                    //Le nombre est a prendre tel quel
                    listImage.add(imageCollection.imageOrNull(number));
                }
            }else{
                //le nombre est compressé d'une valeur de "compressedNumber"
                    listImage.addAll(Collections.nCopies(compressedNumber, imageCollection.imageOrNull(number)));
                    compressedNumber=0;
                    nextIsCompressed=false;
                    
            }
            
        }
        
        return listImage;
    }
    //Joueurs
    private static List<Player> listForPlayers(List<Byte> givenListSub) throws IllegalArgumentException, URISyntaxException, IOException{
        if(givenListSub.size()!=16){
            throw new IllegalArgumentException("La liste de joueur ne contient pas 16 éléments (4x4)");
        } else {
            List<Player> players = new LinkedList<Player>();
            PlayerID playerIDs [] = PlayerID.values();
            for(int i=0; i<4; i++){
                int lives = givenListSub.get(0+4*i);
                SubCell position = new SubCell(1+4*i, 2+4*i);
                Image image = new ImageCollection("player").imageOrNull(3+4*i);
                
                players.add(new Player(playerIDs[i], lives, position, image));  
            }
        }
        
        return null;
    }
    //Score 
    private static List<Image> listForScore(List<Byte> givenListSub) throws URISyntaxException, IOException{
        ImageCollection imageCollection = new ImageCollection("score");
        List<Image> list = new LinkedList<Image>();
        
        for(int i=0; i<4; i++){
           //l'image montrant le visage du joueur, de face, soit vivant soit mort
            if(givenListSub.get(0+4*i)>0){
                //le joueur est vivant
                list.add(imageCollection.image(i*2));
            } else {
                list.add(imageCollection.image(i*2+1));
            }
           //une image de remplissage qui donne au rectangle correspondant au joueur la taille nécessaire pour qu'il soit possible d'y afficher son nombre de vies
            list.add(imageCollection.image(10));
           //une image terminant ce rectangle
            list.add(imageCollection.image(11));
            
            //Si on est au milieu alors on doit placer l'image de remplissage central
            if(i==1){
                list.add(imageCollection.image(12));
            }
        }
        
        return null;
    }

    //Temps restant
    private static List<Image> listForTime(byte time) throws URISyntaxException, IOException{
        ImageCollection imageCollection = new ImageCollection("score");
        List<Image> list = new LinkedList<Image>();
        
        //allumé
        list.addAll(Collections.nCopies(time, imageCollection.imageOrNull(21)));
        
        //éteind
        list.addAll(Collections.nCopies(60-time, imageCollection.imageOrNull(20)));
        
        System.out.println(list.size());
        return list;
    }
    
}
