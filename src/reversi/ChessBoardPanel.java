package reversi;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ChessBoardPanel extends JPanel{
    Chess[][] chess = new Chess[8][8];
    int currentState = Chess.BLACK;
    
	SocketServer socketServer = null ;
	SocketClient socketClient = null ;
    
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
    
    public final void initializeChess(){
    	this.chess[3][3].dropBlack();
    	this.chess[3][4].dropWhite();
    	this.chess[4][3].dropWhite();
    	this.chess[4][4].dropBlack();
        this.updateCanClick();
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
						//System.out.println(i + " " + j);
						if(chess[i][j].blackCanClick && currentState == Chess.BLACK){
							checkOrFlip(i , j , true);
							chess[i][j].dropBlack();
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
						if(chess[i][j].whiteCanClick && currentState == Chess.WHITE){
							checkOrFlip(i , j , true);
							chess[i][j].dropWhite();
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
    
    public void changState(){
    	this.checkBoard();
		//seconds = counDownTime;
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
