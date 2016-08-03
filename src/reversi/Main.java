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

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private final int counDownTime = 30;
    ChessBoardPanel chessBoardPanel = new ChessBoardPanel();
    private JPanel chessBoard;
    private final JLabel message = new JLabel(
            "Chess Champ is ready to play!");
    int seconds = counDownTime ;
	int currentState = Chess.BLACK;
	SocketServer socketServer = null ;
	SocketClient socketClient = null ;
	Timer timer;
    Main() {
    	timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(seconds == 0){
					changState();
				}
				
				if(currentState == Chess.BLACK){
					message.setText("Blacks Turn" + seconds);
				}else{
					message.setText("Whites Turn" + seconds);
				}
				seconds--;
			}
    	  });
    	timer.start();
        initializeGui();
        initializeChess();
        updateCanClick();
        checkBoard();
    }

    public final void initializeChess(){
    	chessBoardPanel.chess[3][3].dropBlack();
    	chessBoardPanel.chess[3][4].dropWhite();
    	chessBoardPanel.chess[4][3].dropWhite();
    	chessBoardPanel.chess[4][4].dropBlack();
    }
    
    public final void initializeGui() {
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        JButton buttonServer = new JButton("Server");
        JButton buttonClient = new JButton("Client");
        
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
				currentState = Chess.BLACK;
				timer.stop();
				socketServer = new SocketServer(chessBoardPanel.chess);
				Thread serverThread = new Thread(socketServer);
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
				currentState = Chess.WHITE;
				timer.stop();
				socketClient = new SocketClient(chessBoardPanel.chess);
				Thread clientThread = new Thread(socketClient);
				clientThread.start();
			}
			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
			}
        	
        });
        tools.add(new JButton("New"));
        tools.add(buttonServer);
        tools.add(buttonClient);
        tools.addSeparator();
        tools.add(new JButton("Resign"));
        tools.addSeparator();
        tools.add(message);

        chessBoard = new JPanel(new GridLayout(0, 8));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(chessBoard);

        Insets buttonMargin = new Insets(0,0,0,0);
        for (int i = 0; i < chessBoardPanel.chess.length; i++) {
            for (int j = 0; j < chessBoardPanel.chess[i].length; j++) {
            	chessBoardPanel.chess[i][j] = new Chess();
            	chessBoardPanel.chess[i][j].jButton.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                chessBoardPanel.chess[i][j].jButton.setIcon(icon);
                chessBoardPanel.chess[i][j].jButton.setBackground(Color.ORANGE);
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
            	initializeListener( i , j);
                chessBoard.add(chessBoardPanel.chess[j][i].jButton);
            }
        }
    }

    public final JComponent getChessBoard() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return gui;
    }

    public void initializeListener(int i , int j){
    	chessBoardPanel.chess[i][j].jButton.addMouseListener(new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent mouseEvent) {}
					@Override
					public void mouseEntered(MouseEvent mouseEvent) {}
					@Override
					public void mouseExited(MouseEvent mouseEvent) {}
					@Override
					public void mousePressed(MouseEvent mouseEvent) {
						updateCanClick();
						//System.out.println(i + " " + j);
						if(chessBoardPanel.chess[i][j].blackCanClick && currentState == Chess.BLACK){
							checkOrFlip(i , j , true);
							chessBoardPanel.chess[i][j].dropBlack();
							changState();
							try {
								if(socketServer!=null){
									socketServer.out.writeObject(new SocketData(i , j));
								}
								if(socketClient!=null){
									socketClient.out.writeObject(new SocketData(i , j));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							}
						if(chessBoardPanel.chess[i][j].whiteCanClick && currentState == Chess.WHITE){
							checkOrFlip(i , j , true);
							chessBoardPanel.chess[i][j].dropWhite();
							changState();
							try {
								if(socketServer!=null){
									socketServer.out.writeObject(new SocketData(i , j));
								}
								if(socketClient!=null){
									socketClient.out.writeObject(new SocketData(i , j));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					@Override
					public void mouseReleased(MouseEvent mouseEvent) {}
            		
            	});
    }
    
    public void updateCanClick(){
    	for(int i = 0 ; i < 8 ; i++){
    		for(int j = 0 ; j < 8 ; j++){
    			chessBoardPanel.chess[i][j].blackCanClick = false;
    			chessBoardPanel.chess[i][j].whiteCanClick = false;
    		}
    	}
    	for(int x = 0 ; x <8 ; x++){
    		for(int y = 0 ; y < 8 ; y++){
    			checkOrFlip(x, y , false);
    		}
    	}
    }
    
    void checkOrFlip(int x , int y , boolean shouldFlip){
    	int nextState = 0;
    	int[][] directions = { {-1 , 0} , {0 , -1} , {-1 , -1} , {1 , 1} , {1 , -1 } , {1 , 0} , {0 , 1} , {-1 , 1}};
    	
		if(chessBoardPanel.chess[x][y].getState() == Chess.BLANK){
			for(int direction = 0 ; direction < 8 ; direction++){
				if(((x + directions[direction][0] >=0) && (x + directions[direction][0]) <8) && ((y + directions[direction][1] >=0) && (y + directions[direction][1] <8)))
					nextState = chessBoardPanel.chess[x + directions[direction][0]][y + directions[direction][1]].getState();
				ArrayList<int []> myList = new ArrayList<int []>();
				for(int i = x + directions[direction][0] * 2 , j = y + directions[direction][1] * 2; (i + directions[direction][0] < 8) && (i + directions[direction][0] >= 0) && (j + directions[direction][1]) < 8 && (j + directions[direction][1]) >= 0; i = i + directions[direction][0] , j = j + directions[direction][1]){
					if(i >=0 && i <8 && j >=0 && j<8){
						if( chessBoardPanel.chess[i][j].getState() == Chess.BLANK){
							break;
						}
						int[] temp={i, j};
						myList.add(temp);
						if( chessBoardPanel.chess[i][j].getState() != nextState){
							if(nextState == Chess.BLACK){

								if(shouldFlip){
									chessBoardPanel.chess[x + directions[direction][0]][y + directions[direction][1]].dropWhite();
									for(int k = 0 ; k <myList.size() ; k++){
										chessBoardPanel.chess[myList.get(k)[0]][myList.get(k)[1]].dropWhite();
									}
								}
								chessBoardPanel.chess[x][y].whiteCanClick = true;
							}
							if(nextState == Chess.WHITE){
								if(shouldFlip){
									chessBoardPanel.chess[x + directions[direction][0]][y + directions[direction][1]].dropBlack();
									for(int k = 0 ; k <myList.size() ; k++){
										chessBoardPanel.chess[myList.get(k)[0]][myList.get(k)[1]].dropBlack();
									}
								}
								chessBoardPanel.chess[x][y].blackCanClick = true;
							}
							break;
						}
					}
				}
			}
		}
		else{
			chessBoardPanel.chess[x][y].blackCanClick = false;
			chessBoardPanel.chess[x][y].whiteCanClick = false;
		}
    	checkBoard();
    }
    
    public void changState(){
    	checkBoard();
		seconds = counDownTime;
		if(currentState == Chess.BLACK){
			message.setText("Whites Turn" + seconds);
			currentState = Chess.WHITE;
		}else{
			message.setText("Blacks Turn" + seconds);
			currentState = Chess.BLACK;
		}
		updateCanClick();
		checkBoard();
    }
    
    public void checkBoard(){
    	for(int i = 0 ; i < 8 ; i++)
    		for(int j = 0 ; j < 8 ; j++){
    			if(chessBoardPanel.chess[i][j].getState() == Chess.BLANK)
    				chessBoardPanel.chess[i][j].initButton();
    			if(chessBoardPanel.chess[i][j].getState() == Chess.BLACK)
    				chessBoardPanel.chess[i][j].dropBlack();
    			if(chessBoardPanel.chess[i][j].getState() == Chess.WHITE)
    				chessBoardPanel.chess[i][j].dropWhite();
    		}	
    			
    	for(int i = 0 ; i < 8 ; i++)
    		for(int j = 0 ; j < 8 ; j++){
    			if(currentState == Chess.BLACK && chessBoardPanel.chess[i][j].blackCanClick == true && chessBoardPanel.chess[i][j].getState() == Chess.BLANK){
    				chessBoardPanel.chess[i][j].dropCanClick();
    			}else if(currentState == Chess.WHITE && chessBoardPanel.chess[i][j].whiteCanClick == true && chessBoardPanel.chess[i][j].getState() == Chess.BLANK){
    				chessBoardPanel.chess[i][j].dropCanClick();
    			}
    		}
    			
    }
    
    public static void main(String[] args) {
            	Main main = new Main();

                JFrame jFrame = new JFrame("Reversi");
                jFrame.add(main.getGui());
                jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jFrame.setLocationByPlatform(true);

                jFrame.pack();

                jFrame.setMinimumSize(jFrame.getSize());
                jFrame.setVisible(true);
    }
}