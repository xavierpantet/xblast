package ch.epfl.xblast.launcher;

import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.net.SocketException;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public final class ServerView implements Observer {
    private ServerModel model;
    
    JButton startButton = new JButton();
    Label ipLabel = new Label();
    ImageIcon logo = new ImageIcon(new ImageIcon("images/icon.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT)); 
    JLabel image = new JLabel(logo);
    Panel panel = new Panel();
    JFrame window = new JFrame("XBlast");
    
    public ServerView(ServerModel model) throws SocketException{
        this.model = Objects.requireNonNull(model);
        this.ipLabel.setText(model.getIP());
    }
    
    
    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        
    }
}
