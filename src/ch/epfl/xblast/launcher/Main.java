package ch.epfl.xblast.launcher;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;


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
            LauncherController controleur =  new LauncherController(modele, vue);
        });
        
        
    }
}