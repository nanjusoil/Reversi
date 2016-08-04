package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MainPanel extends JPanel{

    ChessBoardPanel chessBoardPanel = new ChessBoardPanel();
	
    private final JLabel message = new JLabel(
            "Chess is ready to play!");
    
	Timer timer;
    

    MainPanel(){
		super(new BorderLayout(3, 3));
		MusicPlayer musicPlayer = new MusicPlayer();

    	timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chessBoardPanel.seconds == 0){
					chessBoardPanel.changState();
				}
				
				if(chessBoardPanel.currentState == Chess.BLACK){
					message.setText("Blacks Turn" + chessBoardPanel.seconds);
				}else{
					message.setText("Whites Turn" + chessBoardPanel.seconds);
				}
				chessBoardPanel.seconds--;
			}
    	  });
    	timer.start();
	}
	
    public final void initializeGui() {
    	this.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        this.add(tools, BorderLayout.PAGE_START);
        

        
        JButton buttonServer = new JButton("Server");
        JButton buttonClient = new JButton("Client");
        JButton buttonUndo = new JButton("Undo");
        
        buttonServer.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
			}
			@Override
			public void mouseEntered(MouseEvent mouseEvent) {
			}
			@Override
			public void mouseExited(MouseEvent mouseEvent) {
			}
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				chessBoardPanel.currentState = Chess.BLACK;
				chessBoardPanel.isMyTurn = true;
				//timer.stop();
				chessBoardPanel.socketServer = new SocketServer(chessBoardPanel);
				Thread serverThread = new Thread(chessBoardPanel.socketServer);
				serverThread.start();
			}
			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
			}
        	
        });
        
        buttonClient.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
			}
			@Override
			public void mouseEntered(MouseEvent mouseEvent) {
			}
			@Override
			public void mouseExited(MouseEvent mouseEvent) {
			}
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				chessBoardPanel.currentState = Chess.WHITE;
				chessBoardPanel.isMyTurn = false;
				//timer.stop();
				chessBoardPanel.socketClient = new SocketClient(chessBoardPanel);
				Thread clientThread = new Thread(chessBoardPanel.socketClient);
				clientThread.start();
			}
			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
			}
        	
        });
        
        buttonUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				try {
					//chessBoardPanel.undo(false , -1);
					chessBoardPanel.undo();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
        	
        });
        tools.add(buttonUndo);
        tools.add(buttonServer);
        tools.add(buttonClient);
        tools.addSeparator();
        tools.add(new JButton("Resign"));
        tools.addSeparator();
        tools.add(message);

        chessBoardPanel = new ChessBoardPanel();
        chessBoardPanel.setBorder(new LineBorder(Color.BLACK));
        this.add(chessBoardPanel);
        this.add(chessBoardPanel.chatPanel, BorderLayout.PAGE_END);
    }
}
