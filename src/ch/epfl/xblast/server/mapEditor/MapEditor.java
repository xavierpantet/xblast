package ch.epfl.xblast.server.mapEditor;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import ch.epfl.xblast.menu.View;

/**
 * A custom JFrame representing a map editor
 * 
 * @author Guillaume Michel (258066)
 * @author Adrien Vandenbroucque (258715)
 *
 */
@SuppressWarnings("serial")
public final class MapEditor extends JFrame{
    private GridOfBlocks grid = new GridOfBlocks();
    private BlockChooser blockChooser = new BlockChooser();
    private PlayerChooser playerChooser = new PlayerChooser();
    private Options options = new Options();
    
    /**
     * Constructs a map editor with its different panels
     */
    public MapEditor(){
        super("Map Editor");
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                View.setMainFrameEnabled();
                MapEditor.this.dispose();
            }
        });
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.add(blockChooser, BorderLayout.PAGE_START);
        this.add(grid, BorderLayout.CENTER);
        this.add(options, BorderLayout.PAGE_END);
        this.add(playerChooser, BorderLayout.EAST);
        this.pack();
        this.setVisible(true);
    }
    
    /**
     * Returns the grid of blocks of this map editor
     * 
     * @return
     *      The grid of blocks of this map editor
     */
    public GridOfBlocks grid(){
        return grid;
    }
    
    /**
     * Returns the block chooser of this map editor
     * 
     * @return
     *      The block chooser of this map editor
     */
    public BlockChooser blockChooser(){
        return blockChooser;
    }
    
    /**
     * Returns the player chooser of this map editor
     * 
     * @return
     *      The player chooser of this map editor
     */
    public PlayerChooser playerChooser(){
        return playerChooser;
    }
}
