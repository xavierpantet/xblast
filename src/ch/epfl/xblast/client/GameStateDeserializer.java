package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameStateClient.Player;
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
    
    public static GameStateClient deserialize(List<Byte> givenList) throws IllegalArgumentException, URISyntaxException, IOException{
        
        //Création des différentes sublists
        int givenListSize = givenList.size();
        List<Byte> bytesForBoard = givenList.subList(1, givenList.get(0)+1);
        int nbOfBoardBytes = givenList.get(0);
        int nbOfPlayerbytes = givenList.get(nbOfBoardBytes)+1;
        List<Byte> bytesForBombs =  givenList.subList(givenList.get(0)+2, nbOfBoardBytes+nbOfPlayerbytes+3);
        List<Byte> bytesForPlayers = givenList.subList(givenListSize-17, givenListSize-1);
        byte time = givenList.get(givenListSize-1);
        
        //GameState(List<Player> players, List<Image> boardImages, List<Image> explosivesImages, List<Image> scoreImages, List<Image> timeLineImages)
        //Création de la liste de player
        List<GameStateClient.Player> players = listForPlayers(bytesForPlayers);
        List<Image> boardImages = listForBoard(bytesForBoard);
        List<Image> explosivesImages = listForBombs(bytesForBombs);
        List<Image> scoreImages = listForScore(bytesForPlayers);
        List<Image> timeLineImages = listForTime(time);
        

        return new GameStateClient(players, boardImages, explosivesImages, scoreImages, timeLineImages);

        
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
        
        //Conversion en odre de lecture et retour
         
        Map<Cell, Image>map=new HashMap<Cell, Image>();
        for(int i=0; i<listImage.size(); i++){
            map.put(Cell.SPIRAL_ORDER.get(i), listImage.get(i));
        }
        
        List<Image> result = new LinkedList<Image>();
        for(Map.Entry<Cell, Image> e: map.entrySet()){
            result.add(e.getValue());
        }
        
        return result;
    }
    //Bombes et explosions
    private static List<Image> listForBombs(List<Byte> givenListSub){
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
    private static List<GameStateClient.Player> listForPlayers(List<Byte> givenListSub){
        if(givenListSub.size()!=16){
            throw new IllegalArgumentException("La liste de joueur ne contient pas 16 éléments (4x4)");
        } else {
            List<GameStateClient.Player> players = new LinkedList<GameStateClient.Player>();
            PlayerID playerIDs [] = PlayerID.values();
            for(int i=0; i<4; i++){
                int lives = givenListSub.get(0+4*i);
                SubCell position = new SubCell(1+4*i, 2+4*i);
                Image image = new ImageCollection("player").image(3+4*i);
                
                players.add(new GameStateClient.Player(playerIDs[i], lives, position, image)); 
                
            }
            return players;
        }
        
      
    }
    //Score 
    private static List<Image> listForScore(List<Byte> givenListSub){
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
        
        return list;
    }

    //Temps restant
    private static List<Image> listForTime(byte time) throws URISyntaxException, IOException{
        ImageCollection imageCollection = new ImageCollection("score");
        List<Image> list = new LinkedList<Image>();
        
        //allumé
        list.addAll(Collections.nCopies(time, imageCollection.imageOrNull(21)));
        
        //éteind
        list.addAll(Collections.nCopies(60-time, imageCollection.imageOrNull(20)));
        
   
        return list;
    }
    
    /**
     * Cette méthode tranforme une liste qui décrit un tableau décrit d'un ordre en spirale (en partant du coin en haut a gauche) en un tableau décrit en ordre de lecture 
     * @param givenList (List<Integer>) la liste a convertir
     * @param width (int) la largeur du tableau décrit
     * @param height (int) la longeur du tableau décrit
     * @return convertedList (List<Integer>) le tableau converti
     */
    private static List<Image> convertToRowMajorOrder(List<Image> givenList, int width, int height){
        int NextOp = 0;
        int i = 0;
        int j = 0;
        Image[][] tab = new Image[width][height];
        int loop = 0;
        List<Image> result = new ArrayList<Image>();
        
        //Pour autant d'éléments qu'il y a dans la liste donnée on parcourt le tableau
        for(int k=0; k<givenList.size(); k++){
            switch (NextOp){
            //cas de départ
            case 0:
                tab[0][0]=givenList.get(0);
                NextOp=1;
                break;
                
            //droite
            case 1:
                //On avance de 1 colonne
                i+=1;
                
                tab[i][j]=givenList.get(k);
                
                //On regarde si on doit changer d'opération pour la prochaine itération
                if(i==width-1-loop){
                    NextOp=2;
                }
                break;
                
            //descend
            case 2:
                //On reste à la même colonne mais on avance d'une ligne
                j+=1;
                
                tab[i][j]=givenList.get(k);
                
                //On regarde si on doit changer d'opération pour la prochaine itération
                if(j==height-1-loop){
                    NextOp=3;
                }
                break;
                
            //gauche
            case 3:
                //On reste à la même ligne mais on recule d'une colone
                i-=1;
                
                tab[i][j]=givenList.get(k);
                
                //On regarde si on doit changer d'opération pour la prochaine itération
                if(i==loop){
                    NextOp=4;
                    //On ajoute 1 au compteur de loop car c'est à partir de la qu'il faut rétrécir la boucle
                    loop+=1;
                }
                break;
                
            //haut
            case 4:
                //On reste à la même colonne mais on recule d'une ligne
                j-=1;
              
                tab[i][j]=givenList.get(k);
                
              //On regarde si on doit changer d'opération pour la prochaine itération
                if(j==loop){
                    NextOp=1;
                }
            }
        }
        
        //On boucle sur le tableau pour en créer un liste
        for(int y=0; y<height; y++){
            for(int x=0; x<width; x++){
                result.add(tab[x][y]);
            }
        }
        return result;
    }
    
}
