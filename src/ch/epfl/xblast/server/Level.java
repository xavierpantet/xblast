package ch.epfl.xblast.server;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import ch.epfl.xblast.server.graphics.BoardPainter;
import ch.epfl.xblast.server.graphics.BlockImage;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Player;

/**
 * Représente un niveau de jeu XBlast. 
 * Un niveau n'est rien d'autre qu'une paire composée d'un peintre de plateau qui donne le style graphique du niveau 
 * et d'un état de jeu initial — qui donne ses autres paramètres : positions et capacités initiales des joueurs, etc.
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 *
 */
public final class Level {
    private final BoardPainter b;
    private final GameState g;
    public static final Level DEFAULT_LEVEL = createDefaultLevel();


    /**
     * Constructeur par défaut
     * @param b (BoardPainter) le peinture du plateau
     * @param g (GameState) l'etat du jeu
     * @throws NullPointerException
     */
    Level(BoardPainter b, GameState g) throws NullPointerException{
        this.b = Objects.requireNonNull(b);
        this.g= Objects.requireNonNull(g);
    }

    /**
     * Retourne le peintre du plateau
     * @return le peintre du plateau (BoardPainter)
     */
    public BoardPainter b(){
        return b;
    }

    /**
     * Retourne l'état du jeu
     * @return l'état de jeu (GameState)
     */
    public GameState g(){
        return g;
    }


    public static Level createLevelWithBoardID(String file){
        try(InputStreamReader r = new InputStreamReader(new FileInputStream("levels/"+file))){
            int b = 0;
            Block[][] board = new Block[7][6];
            int x = 0;
            int y = 0;
            String toAnalyse = "";
            
            //Tant que le fichier contient des éléments
            while((b = r.read())!=-1){
                
                if(toAnalyse.length()==2){
                    board[x][y]=Block.getcorrespondingWord(toAnalyse);
                    toAnalyse="";
                    x++;
                }
                //Si c'est un retour à la ligne
                if((char)b=='\n'){
                    y++;
                    x=0;
                } 
                //Si c'est un espace
                else if ((char)b == ' '){
                    
                } 
                
                else {
                    toAnalyse+=(char)b;
                    System.out.print((char)b);
                }
                
            }
            
            for(int i=0; i<7; i++){
                for(int j=0; j<6; j++){
                    System.out.print(board[i][j]+" ");
                }
                System.out.println("\n");
                
            }
          
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return null;
    }
    

    /**
     * Méthode qui créer du Level par défaut
     * @return Level (Level) Level par défaut
     */
    private static Level createDefaultLevel(){
        //Création de la palète (Map<Block, BlockImage>)
        Map<Block, BlockImage> pallet = new HashMap<Block, BlockImage>();

        //Ajout du lien entre l'image en le bloc dans la map pallet
        pallet.put(Block.FREE, BlockImage.IRON_FLOOR);
        pallet.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        pallet.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
        pallet.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        pallet.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
        pallet.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
        pallet.put(Block.BONUS_LIFE, BlockImage.BONUS_LIFE);
        pallet.put(Block.POW, BlockImage.POW);
        pallet.put(Block.BONUS_MOVE_BOMB, BlockImage.MOVE_BOMB);

        //Instanciation de BoardPainter
        BoardPainter b = new BoardPainter(pallet, BlockImage.IRON_FLOOR_S);

        //Création de la liste de Players
        List<Player> players = new ArrayList<>();

        players.add(new Player(PlayerID.PLAYER_1, 3, new Cell(1,1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_2, 3, new Cell(13,1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_3, 3, new Cell(13,11), 2, 3));
        players.add(new Player(PlayerID.PLAYER_4, 3, new Cell(1,11), 2, 3));

        //Création du board
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, xx, xx, xx, xx, xx, xx),
                        Arrays.asList(xx, xx, xx, xx, xx, xx, xx),
                        Arrays.asList(xx, xx, xx, xx, xx, xx, xx),
                        Arrays.asList(xx, xx, xx, xx, xx, xx, xx),
                        Arrays.asList(xx, xx, xx, xx, xx, xx, xx),
                        Arrays.asList(xx, xx, xx, xx, xx, xx, xx)));
        GameState g = new GameState(board, players);

        //Création du level
        return new Level(b, g);

    }
}
