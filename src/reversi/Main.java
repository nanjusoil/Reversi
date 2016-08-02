package reversi;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

public class Main {

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private Chess[][] chess = new Chess[8][8];
    private JPanel chessBoard;
    private final JLabel message = new JLabel(
            "Chess Champ is ready to play!");
	int currentState = Chess.BLACK;
    Main() {
        initializeGui();
        initializeChess();
        updateCanlick();
    }

    public final void initializeChess(){
    	chess[3][3].dropBlack();
    	chess[3][4].dropWhite();
    	chess[4][3].dropWhite();
    	chess[4][4].dropBlack();
    }
    
    public final void initializeGui() {
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        tools.add(new JButton("New"));
        tools.add(new JButton("Save"));
        tools.add(new JButton("Restore"));
        tools.addSeparator();
        tools.add(new JButton("Resign"));
        tools.addSeparator();
        tools.add(message);

        chessBoard = new JPanel(new GridLayout(0, 8));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(chessBoard);

        Insets buttonMargin = new Insets(0,0,0,0);
        for (int i = 0; i < chess.length; i++) {
            for (int j = 0; j < chess[i].length; j++) {
            	chess[i][j] = new Chess();
            	chess[i][j].jButton.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                chess[i][j].jButton.setIcon(icon);
                chess[i][j].jButton.setBackground(Color.ORANGE);
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
            	initializeListener( i , j);
                chessBoard.add(chess[j][i].jButton);
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
    	chess[i][j].jButton.addMouseListener(new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent mouseEvent) {}
					@Override
					public void mouseEntered(MouseEvent mouseEvent) {}
					@Override
					public void mouseExited(MouseEvent mouseEvent) {}
					@Override
					public void mousePressed(MouseEvent mouseEvent) {
						updateCanlick();
						if(chess[i][j].blackCanClick && currentState == Chess.BLACK){
							checkOrFlip(i , j , true);
							chess[i][j].dropBlack();
							currentState = Chess.WHITE;
						}
						if(chess[i][j].whiteCanClick && currentState == Chess.WHITE){
							checkOrFlip(i , j , true);
							chess[i][j].dropWhite();
							currentState = Chess.BLACK;
						}
					}
					@Override
					public void mouseReleased(MouseEvent mouseEvent) {}
            		
            	});
    }
    
    public void updateCanlick(){
    	for(int x = 0 ; x <8 ; x++){
    		for(int y = 0 ; y < 8 ; y++){
    			checkOrFlip(x, y , false);
    		}
    	}
    }
    
    void checkOrFlip(int x , int y , boolean shouldFlip){
    	int nextState = 0;
		if(chess[x][y].getState() == Chess.BLANK){
			if( x + 1 < 8 )
			nextState = chess[x +1][y].getState();
			for(int i = x + 2; i < 8 ; i++){
				if(i<8){
					if(chess[ i ][ y ].getState() ==Chess.BLANK)
						break;
					if(chess[ i ][ y ].getState() != nextState){
						if(nextState == Chess.BLACK){
							for(int j = x + 1 ; j <= i ; j ++)
								chess[ x ][ y ].dropWhite();
    						chess[ x ][ y ].whiteCanClick = true;
						}
						if(nextState == Chess.WHITE){
    						chess[ x ][ y ].blackCanClick = true;
						}

					}
				}
			}
			
			if( x - 1 >= 0 )
			nextState = chess[x - 1 ][y].getState();
			for(int i = x - 2; i < 8 ; i++){
				if(i >= 0){
					if(chess[ i ][ y ].getState() ==Chess.BLANK)
						break;
					if(chess[ i ][ y ].getState() != nextState){
						if(nextState == Chess.BLACK){
							for(int j = x -1 ; j >= i ; j --)
								chess[ x ][ y ].dropWhite();
    						chess[ x ][ y ].whiteCanClick = true;
						}
						if(nextState == Chess.WHITE){
    						chess[ x ][ y ].blackCanClick = true;
						}

					}
				}
			}
			
			if( y + 1 < 8 )
			nextState = chess[ x ][y + 1].getState();
			for(int j = y + 2; j < 8 ; j++){
				if(j < 8){
					if(chess[ x ][ j ].getState() ==Chess.BLANK)
						break;
					if(chess[ x ][ j ].getState() != nextState){
						if(nextState == Chess.BLACK){
							for(int i = y + 1 ; i <= j ; i ++)
								chess[ x ][ y ].dropWhite();
    						chess[ x ][ y ].whiteCanClick = true;
						}
						if(nextState == Chess.WHITE){
    						chess[ x ][ y ].blackCanClick = true;
						}
					}
				}
			}
			
			if( y - 1 >= 0 )
			nextState = chess[ x ][y - 1 ].getState();
			for(int j = y - 2; j >= 0 ; j--){
				if(j >= 0){
					if(chess[ x ][ j ].getState() ==Chess.BLANK)
						break;
					if(chess[ x ][ j ].getState() != nextState){
						if(nextState == Chess.BLACK){
							for(int i = y + 1 ; j <= i ; j ++)
								chess[ x ][ y ].dropWhite();
    						chess[ x ][ y ].whiteCanClick = true;
						}
						if(nextState == Chess.WHITE){
    						chess[ x ][ y ].blackCanClick = true;
						}
					}
				}
			}
		}
		else{
			chess[x][y].blackCanClick = false;
			chess[x][y].whiteCanClick = false;
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