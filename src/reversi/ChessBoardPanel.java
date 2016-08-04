package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChessBoardPanel extends JPanel{
    Chess[][] chess = new Chess[8][8];
    int currentState = Chess.BLACK;
    final int counDownTime = 30;
    int seconds = counDownTime ;
    ArrayList<Chess[][]> undoChess = new ArrayList<Chess[][]>();
    
	SocketServer socketServer = null ;
	SocketClient socketClient = null ;
	
	boolean isMyTurn = true;;
	int timeOfUndo = 0;
    
	JPanel chatPanel;
	JTextField chatTextField ;
	JTextArea chatTextArea;
	JScrollPane scrollPane;
    public ChessBoardPanel(){
    	super(new GridLayout(0, 8));
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int i = 0; i < this.chess.length; i++) {
            for (int j = 0; j < this.chess[i].length; j++) {
            	this.chess[i][j] = new Chess();
            	this.chess[i][j].jButton.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                this.chess[i][j].jButton.setIcon(icon);
                this.chess[i][j].jButton.setBackground(Color.ORANGE);
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
            	initializeListener( i , j);
                this.add(chess[j][i].jButton);
            }
        }
        
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel , BoxLayout.Y_AXIS));
        chatTextField = new JTextField(20); 
        
        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        scrollPane = new JScrollPane( chatTextArea );
        scrollPane.setPreferredSize(new Dimension(50,50));

        
        chatPanel.add(scrollPane);
        chatPanel.add(chatTextField);
        
        chatTextField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if(keyEvent.getKeyCode() == keyEvent.VK_ENTER){
					if(socketServer != null){
						try {
							socketServer.out.writeObject(new SocketData(chatTextField.getText()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(socketClient != null){
						try {
							socketClient.out.writeObject(new SocketData(chatTextField.getText()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					chatTextArea.setText(chatTextArea.getText() + "\n" + chatTextField.getText());
					chatTextField.setText("");
				}
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
			}

			@Override
			public void keyTyped(KeyEvent keyEvent) {
			}
        	
        });
    }
    
    public void checkBoard(){
    	for(int i = 0 ; i < 8 ; i++)
    		for(int j = 0 ; j < 8 ; j++){
    			if(this.chess[i][j].getState() == Chess.BLANK)
    				this.chess[i][j].initButton();
    			if(this.chess[i][j].getState() == Chess.BLACK)
    				this.chess[i][j].dropBlack();
    			if(this.chess[i][j].getState() == Chess.WHITE)
    				this.chess[i][j].dropWhite();
    		}	
    			
    	for(int i = 0 ; i < 8 ; i++)
    		for(int j = 0 ; j < 8 ; j++){
    			if(this.currentState == Chess.BLACK && this.chess[i][j].blackCanClick == true && this.chess[i][j].getState() == Chess.BLANK){
    				this.chess[i][j].dropCanClick();
    			}else if(this.currentState == Chess.WHITE && this.chess[i][j].whiteCanClick == true && this.chess[i][j].getState() == Chess.BLANK){
    				this.chess[i][j].dropCanClick();
    			}
    		}
    			
    }
    
    public void dropChess(int x, int y , boolean isBlack){
    	checkOrFlip(x , y, true);
		Chess[][] tempChess = new Chess[8][8];
		for(int i = 0 ; i < 8 ; i++){
			for(int j = 0 ; j < 8 ; j++){
				tempChess[i][j] = (Chess) chess[i][j].clone();
			}
		}
		undoChess.add(tempChess);
		
    	if(isBlack){
    		chess[x][y].dropBlack();
    	}else{
    		chess[x][y].dropWhite();
    	}
		//checkOrFlip(x , y, true);
    }
    
    public final void initializeChess(){
    	this.chess[3][3].dropBlack();
    	this.chess[3][4].dropWhite();
    	this.chess[4][3].dropWhite();
    	this.chess[4][4].dropBlack();
        this.updateCanClick();
    }
    
    public void undo() throws IOException{
    	if(socketServer != null){
    		socketServer.out.writeObject(new SocketData(SocketData.WAITFORCOMFIRM));
    		return;
    	}
    	else if(socketClient != null){
    		socketClient.out.writeObject(new SocketData(SocketData.WAITFORCOMFIRM));
    		return;
    	}
    	else if(undoChess.size()>=2){
    		if(timeOfUndo <2){
            	this.chess = undoChess.get(undoChess.size()-2);
            	undoChess.remove(undoChess.size()-1);	
    		}
    		timeOfUndo++;
    	}
    	updateCanClick();
    }
    
    public void undo(int state) throws IOException{
    	if(state == SocketData.COMFIRM){
        	this.chess = undoChess.get(undoChess.size()-2);
        	undoChess.remove(undoChess.size()-1);	
    	}
    	else if(state == SocketData.DENIED) {
    		
    	}else if(state == SocketData.WAITFORCOMFIRM) {
        	int dialogResult = JOptionPane.showConfirmDialog (null, "Undo one step?","Undo",JOptionPane.YES_NO_OPTION);
    		if(dialogResult == JOptionPane.YES_OPTION){
    	    	if(socketServer != null){
    	    		socketServer.out.writeObject(new SocketData(SocketData.COMFIRM));
    	    	}
    	    	else if(socketClient != null){
    	    		socketClient.out.writeObject(new SocketData(SocketData.COMFIRM));
    	    	}
            	this.chess = undoChess.get(undoChess.size()-2);
            	undoChess.remove(undoChess.size()-1);		
    		}else{
    	    	if(socketServer != null){
    	    		socketServer.out.writeObject(new SocketData(SocketData.DENIED));
    	    	}
    	    	else if(socketClient != null){
    	    		socketClient.out.writeObject(new SocketData(SocketData.DENIED));
    	    	}
    		}
    	}
    	updateCanClick();
    }
    
    public void initializeListener(int i , int j){
    	this.chess[i][j].jButton.addMouseListener(new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent mouseEvent) {}
					@Override
					public void mouseEntered(MouseEvent mouseEvent) {}
					@Override
					public void mouseExited(MouseEvent mouseEvent) {}
					@Override
					public void mousePressed(MouseEvent mouseEvent) {
						updateCanClick();
						/*Chess[][] tempChess = new Chess[8][8];
						for(int i = 0 ; i < 8 ; i++){
							for(int j = 0 ; j < 8 ; j++){
								tempChess[i][j] = (Chess) chess[i][j].clone();
							}
						}*/
						if(chess[i][j].blackCanClick && currentState == Chess.BLACK && isMyTurn){

							
							/*undoChess.add(tempChess);
							checkOrFlip(i , j , true);
							chess[i][j].dropBlack();*/
							dropChess( i , j , true);
							changState();
							try {
								if(socketServer!=null){
									isMyTurn = false;
									socketServer.out.writeObject(new SocketData(i , j));
								}
								if(socketClient!=null){
									isMyTurn = false;
									socketClient.out.writeObject(new SocketData(i , j));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							}
						if(chess[i][j].whiteCanClick && currentState == Chess.WHITE && isMyTurn){
							/*undoChess.add(tempChess);
							checkOrFlip(i , j , true);
							chess[i][j].dropWhite();*/
							dropChess( i , j , false);
							changState();
							try {
								if(socketServer!=null){
									isMyTurn = false;
									socketServer.out.writeObject(new SocketData(i , j));
								}
								if(socketClient!=null){
									isMyTurn = false;
									socketClient.out.writeObject(new SocketData(i , j));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						updateCanClick();
						if(!hasNext()){
							JOptionPane.showConfirmDialog (null, "Game Over!","Game Over!",JOptionPane.YES_NO_OPTION);
							changState();
						}
					}
					@Override
					public void mouseReleased(MouseEvent mouseEvent) {}
            		
            	});
    }
    
    void checkOrFlip(int x , int y , boolean shouldFlip){
    	int nextState = 0;
    	int[][] directions = { {-1 , 0} , {0 , -1} , {-1 , -1} , {1 , 1} , {1 , -1 } , {1 , 0} , {0 , 1} , {-1 , 1}};
    	
		if(this.chess[x][y].getState() == Chess.BLANK){
			for(int direction = 0 ; direction < 8 ; direction++){
				if(((x + directions[direction][0] >=0) && (x + directions[direction][0]) <8) && ((y + directions[direction][1] >=0) && (y + directions[direction][1] <8)))
					nextState = this.chess[x + directions[direction][0]][y + directions[direction][1]].getState();
				ArrayList<int []> myList = new ArrayList<int []>();
				for(int i = x + directions[direction][0] * 2 , j = y + directions[direction][1] * 2; (i + directions[direction][0] < 8) && (i + directions[direction][0] >= 0) && (j + directions[direction][1]) < 8 && (j + directions[direction][1]) >= 0; i = i + directions[direction][0] , j = j + directions[direction][1]){
					if(i >=0 && i <8 && j >=0 && j<8){
						if( this.chess[i][j].getState() == Chess.BLANK){
							break;
						}
						int[] temp={i, j};
						myList.add(temp);
						if( this.chess[i][j].getState() != nextState){
							if(nextState == Chess.BLACK){

								if(shouldFlip){
									this.chess[x + directions[direction][0]][y + directions[direction][1]].dropWhite();
									for(int k = 0 ; k <myList.size() ; k++){
										this.chess[myList.get(k)[0]][myList.get(k)[1]].dropWhite();
									}
								}
								this.chess[x][y].whiteCanClick = true;
							}
							if(nextState == Chess.WHITE){
								if(shouldFlip){
									this.chess[x + directions[direction][0]][y + directions[direction][1]].dropBlack();
									for(int k = 0 ; k <myList.size() ; k++){
										this.chess[myList.get(k)[0]][myList.get(k)[1]].dropBlack();
									}
								}
								this.chess[x][y].blackCanClick = true;
							}
							break;
						}
					}
				}
			}
		}
		else{
			this.chess[x][y].blackCanClick = false;
			this.chess[x][y].whiteCanClick = false;
		}
		this.checkBoard();
    }
    
    public boolean hasNext(){
    	for(int i = 0 ; i < 8 ; i++)
    		for(int j = 0 ; j < 8 ; j++){
    			if(chess[i][j].blackCanClick && currentState == Chess.BLACK)
    				return true;
    			if(chess[i][j].whiteCanClick && currentState == Chess.WHITE)
    				return true;
    		}
    	return false;
    }
    
    public void changState(){
    	if(socketServer != null || socketClient != null){
    		seconds = counDownTime;
    		return;
    	}
    	seconds = counDownTime;
		if(this.currentState == Chess.BLACK){
			//message.setText("Whites Turn" + seconds);
			this.currentState = Chess.WHITE;
		}else{
			//message.setText("Blacks Turn" + seconds);
			this.currentState = Chess.BLACK;
		}
		updateCanClick();
		this.checkBoard();
    }
    
    public void updateCanClick(){
    	for(int i = 0 ; i < 8 ; i++){
    		for(int j = 0 ; j < 8 ; j++){
    			this.chess[i][j].blackCanClick = false;
    			this.chess[i][j].whiteCanClick = false;
    		}
    	}
    	for(int x = 0 ; x <8 ; x++){
    		for(int y = 0 ; y < 8 ; y++){
    			checkOrFlip(x, y , false);
    		}
    	}
    }
}
