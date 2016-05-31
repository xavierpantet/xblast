package ch.epfl.xblast.launcher;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;


/**
 * Cette classe lance une fenêtre java qui permet de lancer le jeu
 * @author Xavier Pantet (260473), Timothée Duran (258683)
 *
 */
public class Main {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        
        SwingUtilities.invokeAndWait(()->{
            Thread.currentThread().setName("UI");
            LauncherModel modele = new  LauncherModel();
            LauncherView vue = new  LauncherView(modele);
            
            @SuppressWarnings("unused")
            LauncherController controleur =  new LauncherController(modele, vue);
        });
        
        
    }
}