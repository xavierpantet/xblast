package ch.epfl.xblast.server.debug;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class GameStatePrinter {
    private GameStatePrinter() {}

    public static void printGameState(GameState s) {
        List<Player> ps = s.alivePlayers();
        Map<Cell, Bomb> bombs = s.bombedCells();
        Set<Cell> blastedCells = s.blastedCells();
        System.out.println("bombs -> "+bombs.size());
        for 
        (int y = 0; y < Cell.ROWS; ++y) {
            for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                if(bombs.containsKey(c)){
                    System.out.println("POSITION: "+bombs.get(c).position()+ "OWNER: "+bombs.get(c).ownerId()+" RANGE: "+bombs.get(c).range()+ "FUSE LENGHT: "+bombs.get(c).fuseLength());
        }}
        }
        
        Board board = s.board();

        for (int y = 0; y < Cell.ROWS; ++y) {
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                
                Block b = board.blockAt(c);
                
                // Si la case est une case spéciale, on l'affiche expressément, sinon voir plus bas
                if(!b.canHostPlayer()){
                    System.out.print(stringForBlock(b));
                    continue xLoop;
                }
                


                
                if(bombs.containsKey(c)){
                    System.out.print(stringForBomb(bombs.get(c)));
                    continue xLoop;
                
                }  
                for (Player p: s.players()) {
                    if (p.position().containingCell().equals(c)) {
                        System.out.print(stringForPlayer(p));
                        continue xLoop;
                    }  
                }
               
                
                
                if(blastedCells.contains(c)){
                    System.out.print(stringForBlastedCells());
                    continue xLoop;
                
                }
                
                System.out.print(stringForBlock(b));
                

            }
        
        
            System.out.println();
        }
        for (Player p: s.players()) {
            if(p.id()==PlayerID.PLAYER_1){
            System.out.println(p.id() +" a "+ p.lives()+ " vies restantes ("+p.lifeState().state()+")");
            /*System.out.println("Next IS : "+p.lifeStates().tail().head().state());
            System.out.println("bombes max : "+p.maxBombs()+", portée : "+p.bombRange());
            System.out.println(p.position().containingCell() + " -> " + p.position().distanceToCentral());*/
            /*System.out.println(p.position());
            System.out.println(p.position().containingCell());
            System.out.println(p.position().distanceToCentral());
            */}
            System.out.println("");
        
        }
        System.out.println("Temps: "+ s.remainingTime());
        System.out.println("Ticks: "+ s.ticks());
    }

    private static String stringForBomb(Bomb b){
        if(b.fuseLength()>50){
            return "\u001b[41m" + "\u001b[37m" + "ÒÒ" + "\u001b[m" + "\u001b[m";
        }
        
        else {
            return "\u001b[41m" + "\u001b[37m" + "oo" + "\u001b[m" + "\u001b[m";
        }
        
        
    }
    
    private static String stringForBlastedCells(){
     
            return "\u001b[41m" + "\u001b[37m" + "**" + "\u001b[m" + "\u001b[m";
    
    }
    
    
    private static String stringForPlayer(Player p) {
        StringBuilder b = new StringBuilder();
        b.append(p.id().ordinal() + 1);
        switch (p.direction()) {
        case N: b.append('↑'); break;
        case E: b.append('→'); break;
        case S: b.append('↓'); break;
        case W: b.append('←'); break;
        }
        return "\u001b[43m" + b.toString() + "\u001b[m";
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        /*case FREE: return "  ";
        case INDESTRUCTIBLE_WALL: return "##";
        case DESTRUCTIBLE_WALL: return "??";
        case CRUMBLING_WALL: return "¿¿";
        case BONUS_BOMB: return "+b";
        case BONUS_RANGE: return "+r";
        default: throw new Error();*/
        case FREE: return "  ";
        case INDESTRUCTIBLE_WALL: return "\u001b[40m" + "XX" + "\u001b[m";
        case DESTRUCTIBLE_WALL: return "\u001b[47m" + "xx" + "\u001b[m";
        case CRUMBLING_WALL: return "\u001b[43m" + "~~" + "\u001b[m";
        case BONUS_BOMB: return "\u001b[45m" + "+b" + "\u001b[m";
        case BONUS_RANGE: return "\u001b[46m" + "+r" + "\u001b[m";
        default: throw new Error();
        }
    }
}
