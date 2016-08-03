package reversi;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

public class Main {

    public static void main(String[] args) {
    			MainPanel mainPanel = new MainPanel();
            	Main main = new Main();

            	mainPanel.initializeGui();
            	mainPanel.chessBoardPanel.initializeChess();
            	mainPanel.chessBoardPanel.checkBoard();
            	
                JFrame jFrame = new JFrame("Reversi");
                jFrame.add(mainPanel);
                jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jFrame.setLocationByPlatform(true);

                jFrame.pack();

                jFrame.setMinimumSize(jFrame.getSize());
                jFrame.setVisible(true);
    }
}